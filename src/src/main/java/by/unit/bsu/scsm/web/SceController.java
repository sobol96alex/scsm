package by.unit.bsu.scsm.web;

import by.unit.bsu.scsm.domain.Subscriber;
import by.unit.bsu.scsm.service.SubscriberService;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SceController {

    @Autowired
    private SubscriberService ss;
    
    /*
    @RequestMapping(value="/test.html", method=RequestMethod.GET)
    public void testGet(HttpServletResponse response, @RequestParam(value="url", required=false) String url, @RequestParam(value="host", required=false) String host, @RequestParam(value="params", required=false) String params, Model model) 
    {    
                    String redirect = "/";
                    if(host != null)
                        redirect = "http://" + host;
                    if(url != null)
                        redirect = redirect + url;
                    if(params != null)
                        redirect = redirect + params;
        try {
            response.sendRedirect(redirect);
        } catch (IOException ex) {
            Logger.getLogger(SceController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }*/
    
    @RequestMapping(value="/connect.html", method=RequestMethod.GET)
    public String connectGet(@RequestParam(value="url", required=false) String url, @RequestParam(value="host", required=false) String host, @RequestParam(value="params", required=false) String params, Model model) 
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        WebAuthenticationDetails details = (WebAuthenticationDetails)auth.getDetails();
        String ip = details.getRemoteAddress();
        if(ip.equals("10.0.0.250"))
        {
            return "proxy";
        }
        if(ip.equals("10.0.0.251"))
        {
            return "proxy";
        }        
        List<GrantedAuthority> authList = (List<GrantedAuthority>) auth.getAuthorities();
        boolean granted = false;
        for (GrantedAuthority ga : authList) 
        {
            if(ga.toString().equalsIgnoreCase("Internet")) //проверка на принадлежность группе
            {
                granted = true;
                break;
            }
        }
        
        if(granted)
        {
            Subscriber subscriber = ss.getValidSubscriber(auth.getName());
            if(subscriber.isActive())
            {
                Integer sessioncapacity;
                if(subscriber.getSubscriberMaxSessionCount() == 0)
                    sessioncapacity = -1;
                else
                    sessioncapacity = subscriber.getSubscriberMaxSessionCount() - subscriber.getSubscriberIpList().size();
                if(!subscriber.isActive()) sessioncapacity = 0;
                if(sessioncapacity != 0)
                {
                    ss.connect(subscriber.getUsername(), ip, subscriber.getSubscriberMaxSessionTime());
                    String redirect = "/";
                    if(host != null)
                        redirect = "http://" + host;
                    if(url != null)
                        redirect = redirect + url;
                    if(params != null)
                        redirect = redirect + params;
                    return "redirect:" + redirect;
                }
                else
                {
                    if(ss.hasConnectedIp(subscriber.getUsername(), ip))
                    {
                        ss.reconnect(subscriber.getUsername(), ip);
                        String redirect = "/";
                        if(host != null)
                            redirect = "http://" + host;
                        if(url != null)
                            redirect = redirect + url;
                        if(params != null)
                            redirect = redirect + "?" + params;
                        return "redirect:" + redirect;                    
                    }
                    else
                    {
                        return "hasnomoreconnection";
                    }
                }
            }
            else
            {
                return "forward:/denied";
            }
        }
        else
        {
            return "forward:/denied";
        }
    }	

    @RequestMapping(value="/disconnect")
    public String disconnect(@RequestParam(value="ip", required=false) String ip) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<GrantedAuthority> authList = (List<GrantedAuthority>) auth.getAuthorities();
        boolean granted = false;
        for (GrantedAuthority ga : authList) 
        {
            if(ga.toString().equals("Internet")) //проверка на принадлежность группе
            {
                granted = true;
                break;
            }
        }
        if(granted)
        {
            if(null == ip)
            {
                ss.disconnect(auth.getName(), 1);
            }
            else
            {
                ss.disconnect(auth.getName(), ip, 1);
            }
            //actions, set session param
            return "forward:/home";
        }
        else
        {
            return "forward:/home";
        }
    }	
    
    @RequestMapping("/dologout")
    public String logout() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        ss.disconnect(auth.getName(), 1);
        return "forward:/logout";
    }         
}
