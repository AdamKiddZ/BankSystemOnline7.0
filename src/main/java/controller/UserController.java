package controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.view.RedirectView;
import pojo.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import service.UserService;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Controller
//@SessionAttributes({"user"})
public class UserController {

    @Resource(name = "userService")
    private UserService userService;

    @RequestMapping("/register.do")
    public ModelAndView register(String username, String password1) {
        //userid已由前端判断出是否符合要求
        int res = 0;
        try {
            res = userService.register(username, password1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (res > 0) {
            ModelAndView modelAndView = new ModelAndView("register_succeeded");
            modelAndView.addObject("userid", res);
            return modelAndView;
        } else {
            ModelAndView modelAndView = new ModelAndView("register_failed");
            return modelAndView;
        }
    }

    @RequestMapping("/login.do")
    public ModelAndView login(String username, String password, String admin, String isRemember,HttpServletRequest httpServletRequest) {
        //userid已由前端判断出是否符合要求
//        int userid=Integer.parseInt(request.getParameter("userid"));
//        String password=request.getParameter("password");
        User user = null;
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        try {
            if ("1".equals(isRemember)) {
//                记住账户
                token.setRememberMe(true);
//                Cookie userIDCookie = new Cookie("userid",String.valueOf(userid));
//                Cookie userPassCookie = new Cookie("password",password);
//                userIDCookie.setMaxAge(60*60*1);
//                userPassCookie.setMaxAge(60*60*1);
//                response.addCookie(userIDCookie);
//                response.addCookie(userPassCookie);
            }
            subject.login(token);
            if (subject.isAuthenticated()) {
                if ("1".equals(admin)) {
                    user = userService.adminLogin(username, password);
                } else {
                    user = userService.login(username, password);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (user != null) {
//            request.getSession().setAttribute("user", user);
            HttpSession session=httpServletRequest.getSession();
            session.setAttribute("user", user);
            ModelAndView modelAndView = new ModelAndView("login_succeeded");
            return modelAndView;
        } else {
//            登录失败
            subject.logout();
            ModelAndView modelAndView = new ModelAndView("login_failed");
            return modelAndView;
        }
    }

    @RequestMapping("/logout.do")
    public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) {
        return new ModelAndView("logout");
    }

    @RequestMapping("/getUserList.do")
    public ModelAndView getUserList(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = (User) request.getSession().getAttribute("user");
        List users = new ArrayList();
        try {
            users = userService.getAllUsers();
        } catch (Exception e) {
            e.printStackTrace();
        }
        request.getSession().setAttribute("users", users);
        ModelAndView modelAndView = new ModelAndView(new RedirectView("home.jsp#listUsers"));
//        response.sendRedirect();
        return modelAndView;
    }


    @RequestMapping("/checkUser.do")
    public void checkUser(String username, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            response.getWriter().print(userService.isUserExisted(username));
        } catch (Exception e) {
            e.printStackTrace();
//            response.getWriter().print(true);
        }
    }


    @RequestMapping("/lock.do")
    public void lock(int userid, HttpServletRequest request, HttpServletResponse response) throws IOException {
//        int userid=Integer.parseInt(request.getParameter("userid"));
        try {
            if (userService.lockAccount(userid)) {
                String message = "冻结成功！";
                //对返回信息进行编码
                message = URLEncoder.encode(message, "utf-8");
                response.getWriter().print(message);
            } else {
                String message = "冻结失败！";
                message = URLEncoder.encode(message, "utf-8");
                response.getWriter().print(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/unlock.do")
    public void unlock(int userid, HttpServletRequest request, HttpServletResponse response) throws IOException {
//        int userid=Integer.parseInt(request.getParameter("userid"));
        try {
            if (userService.unlockAccount(userid)) {
                String message = "解冻成功！";
                message = URLEncoder.encode(message, "utf-8");
                response.getWriter().print(message);
            } else {
                String message = "解冻失败！";
                message = URLEncoder.encode(message, "utf-8");
                response.getWriter().print(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
