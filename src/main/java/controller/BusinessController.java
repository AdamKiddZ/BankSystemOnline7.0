package controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import pojo.User;
import service.BusinessService;
import util.exception.BankException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Controller
public class BusinessController{

    @Resource(name="businessService")
    private BusinessService businessService;

    @RequestMapping("/getLogList.do")
    public ModelAndView getLogList(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user=(User)request.getSession().getAttribute("user");
        List logs=new ArrayList();
        try {
            logs=businessService.getLogByUser(user);
        } catch (BankException e) {
            e.printStackTrace();
        }
        request.getSession().setAttribute("logs",logs);
//        response.sendRedirect("home.jsp#listLogs");
        return new ModelAndView(new RedirectView("home.jsp#listLogs"));
    }

    @RequestMapping("/inquiry.do")
    public String inquiry(HttpServletRequest request,HttpServletResponse response) throws IOException, BankException {
        User user=(User) request.getSession().getAttribute("user");
        user.setBalance(businessService.inquiry(user));
        return "redirect:/home.jsp";
    }

    @RequestMapping("/withdraw.do")
    public void withdraw(HttpServletRequest request,HttpServletResponse response) throws IOException {
        User user=(User) request.getSession().getAttribute("user");
        BigDecimal amount=null;
        try{
            amount=new BigDecimal(request.getParameter("amount"));
//            System.out.println("取款的金额为："+amount);
        }catch (Exception e){
            String message="取款金额格式错误！";
            message= URLEncoder.encode(message,"utf-8");
            response.getWriter().print(message);
            return;
        }
        try {
            String message=businessService.withdraw(user, amount);
            message=URLEncoder.encode(message,"utf-8");
            response.getWriter().print(message);
            return;
        } catch (BankException e) {
//            e.printStackTrace();
//            response.getWriter().print(false);
            String message=e.getMessage();
            message=URLEncoder.encode(message,"utf-8");
            response.getWriter().print(message);
        }
    }
    
    @RequestMapping("/deposit.do")
    public void deposit(HttpServletRequest request,HttpServletResponse response) throws IOException {
        User user=(User) request.getSession().getAttribute("user");
        BigDecimal amount=null;
        try{
            amount=new BigDecimal(request.getParameter("amount"));
        }catch (Exception e){
            String message="存款金额格式错误！";
            message=URLEncoder.encode(message,"utf-8");
            response.getWriter().print(message);
            return;
        }
        try {
            String message=businessService.deposit(user, amount);
            message=URLEncoder.encode(message,"utf-8");
            response.getWriter().print(message);
            return;
        } catch (BankException e) {
//            e.printStackTrace();
//            response.getWriter().print(false);
            String message=e.getMessage();
            message=URLEncoder.encode(message,"utf-8");
            response.getWriter().print(message);
        }
    }

    @RequestMapping("/transfer.do")
    public void transfer(HttpServletRequest request,HttpServletResponse response) throws IOException {
        User user=(User) request.getSession().getAttribute("user");
        int receiverID=Integer.parseInt(request.getParameter("receiverID"));
        BigDecimal amount=null;
        try{
            amount=new BigDecimal(request.getParameter("amount"));
        }catch (Exception e){
            String message="转账金额格式错误！";
            message=URLEncoder.encode(message,"utf-8");
            response.getWriter().print(message);
            return;
        }
        try {
            String message=businessService.transfer(user,receiverID, amount);
            message=URLEncoder.encode(message,"utf-8");
            response.getWriter().print(message);
            return;
        } catch (BankException e) {
            e.printStackTrace();
//            response.getWriter().print(false);
            String message=e.getMessage();
            message=URLEncoder.encode(message,"utf-8");
            response.getWriter().print(message);
        }
    }

}
