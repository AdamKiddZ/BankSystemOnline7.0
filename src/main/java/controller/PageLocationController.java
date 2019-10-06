package controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class PageLocationController {
    @RequestMapping("/loginPage.do")
    public ModelAndView loginPage(){
        return new ModelAndView("login");
    }
}
