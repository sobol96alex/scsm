package by.unit.bsu.scsm.web;

import java.util.List;
import java.util.ArrayList;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.authentication.BadCredentialsException;

import by.unit.bsu.scsm.service.SubscriberService;
import by.unit.bsu.scsm.domain.Subscriber;
import by.unit.bsu.scsm.domain.SubscriberIp;
import by.unit.bsu.scsm.domain.SubscriberLoggedRecord;

@Controller
public class HomeController {

    @Autowired
    private SubscriberService ss;
    
    @RequestMapping("/")
    public String index(HttpSession httpSession) { //HttpServletResponse response //response.sendRedirect
        return "forward:/home";
    }
    
    @RequestMapping(value = "/home", produces = "text/plain;charset=UTF-8")
    public String home(Model model) 
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        WebAuthenticationDetails details = (WebAuthenticationDetails)auth.getDetails();
        List<GrantedAuthority> authList = (List<GrantedAuthority>) auth.getAuthorities();
        boolean granted = false;
        boolean admin = false;
        for (GrantedAuthority ga : authList) 
        {
            if(ga.toString().equalsIgnoreCase("Internet")) //проверка на принадлежность группе
            {
                granted = true;
            }
            if(ga.toString().equalsIgnoreCase("sce")) //проверка на принадлежность группе
            {
                admin = true;
            }            
        }      
        Subscriber subscriber = ss.getValidSubscriber(auth.getName());
        
        List<SubscriberLoggedRecord> slr = ss.getSubscriberLastLoggedRecords(subscriber.getUsername());
        model.addAttribute("last5loging", slr);
        model.addAttribute("subscriber", subscriber);
        model.addAttribute("isGranted", granted);
        model.addAttribute("isAdmin", admin);
        model.addAttribute("ip", details.getRemoteAddress());

        return "home";
    }
    
    @RequestMapping("/logouted")
    public String logedout() {
        return "logouted";
    }    
        
    @RequestMapping("/denied") //если запрещено
    public String denied() {
        return "denied"; //возращаем форму
    }	   
    
    @RequestMapping("/accessdenied") //если запрещено
    public String accessdenied() {
        return "accessdenied"; //возращаем форму
    }	         
}
