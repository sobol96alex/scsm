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
import org.springframework.web.bind.annotation.PathVariable;
        
import org.springframework.ui.Model;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import by.unit.bsu.scsm.service.SubscriberService;
import by.unit.bsu.scsm.domain.Subscriber;
import by.unit.bsu.scsm.domain.SubscriberIp;
import by.unit.bsu.scsm.domain.SubscriberLoggedRecord;
import by.unit.bsu.scsm.domain.SubscriberTotalTopUsage;
import by.unit.bsu.scsm.domain.TRData;

@Controller
@RequestMapping("/admin")
public class AdminController {
    
    @Autowired
    private SubscriberService ss;
    
    @RequestMapping("/")
    public String index(HttpSession httpSession) {
        return "forward:/admin/online";
    }
    
    @RequestMapping(value = "/users", produces = "text/plain;charset=UTF-8")
    public String users(Model model) 
    {   
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Subscriber subscriber = ss.getValidSubscriber(auth.getName());
        model.addAttribute("subscriber", subscriber);        
        List<Subscriber> subs = ss.getAllSubscribers();
        model.addAttribute("subs", subs);
        return "admin/users";
    }
    
    @RequestMapping(value = "/online", produces = "text/plain;charset=UTF-8")
    public String onlineusers(Model model) 
    {   
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Subscriber subscriber = ss.getValidSubscriber(auth.getName());
        model.addAttribute("subscriber", subscriber);        
        List<Subscriber> subs = ss.getAllIntroducedSubscribers();
        model.addAttribute("subs", subs);
        return "admin/onlineusers";
    }    
    
    @RequestMapping(value = "/user/show/{username}/", produces = "text/plain;charset=UTF-8")
    public String usershow(@PathVariable("username") String username, Model model) 
    {   
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Subscriber subscriber = ss.getValidSubscriber(auth.getName());
        model.addAttribute("subscriber", subscriber);    
        Subscriber sub = ss.getSubscriber(username);
        model.addAttribute("sub", sub);
        return "admin/usershow";
    }    
    
    @RequestMapping(value = "/user/add", produces = "text/plain;charset=UTF-8", method=RequestMethod.GET)
    public String useraddget(Model model) 
    {   
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Subscriber subscriber = ss.getValidSubscriber(auth.getName());
        model.addAttribute("subscriber", subscriber);    
        Subscriber sub = ss.getSubscriber(subscriber.getUsername());
        model.addAttribute("sub", sub);
        return "admin/useradd";
    }     
    
    @RequestMapping(value = "/user/add", method=RequestMethod.POST)
    public String useraddpost(@RequestParam("username") String username,
                            @RequestParam("comments") String comments,
                            @RequestParam("sessions") Integer sessions,
                            @RequestParam("time") Integer time,
                            @RequestParam("package") Integer packageId,
                            @RequestParam("type") Integer type,
                            @RequestParam(value="enabled", required=false) boolean enabled
                            ) 
    {   
        int setEnable = 0;
        if(enabled)
        {
            //System.out.println(username);
            setEnable = 1;
        }
        ss.createSubscriber(username, packageId, sessions, time, type, setEnable, comments);
        return "redirect:/admin/user/show/" + username + "/";
    }       
    
    @RequestMapping(value = "/user/edit", method=RequestMethod.POST)
    public String usereditpost(@RequestParam("username") String username,
                            @RequestParam("comments") String comments,
                            @RequestParam("sessions") Integer sessions,
                            @RequestParam("time") Integer time,
                            @RequestParam("package") Integer packageId,
                            @RequestParam("type") Integer type,
                            @RequestParam(value="enabled", required=false) boolean enabled
                            ) 
    {   
        int setEnable = 0;
        if(enabled)
        {
            //System.out.println(username);
            setEnable = 1;
        }        
        ss.updateSubscriber(username, packageId, sessions, time, type, setEnable, comments);
        return "redirect:/admin/user/show/" + username + "/";
    }      
    
    @RequestMapping(value = "/user/enable/{username}/")
    public String userenable(@PathVariable("username") String username) 
    {   
        ss.setEnabled(username);
        return "redirect:/admin/user/show/" + username + "/";
    }      
    
    @RequestMapping(value = "/user/disable/{username}/")
    public String userdisable(@PathVariable("username") String username) 
    {   
        ss.setDisabled(username);
        return "redirect:/admin/user/show/" + username + "/";
    }    
    
    @RequestMapping(value = "/user/delete/{username}/")
    public String userdelete(@PathVariable("username") String username) 
    {   
        ss.deleteSubscriber(username);
        return "redirect:/admin/users";
    }       
    
    @RequestMapping(value = "/user/package/{username}/")
    public String userdelete(@PathVariable("username") String username, @RequestParam("pid") Integer packageId) 
    {   
        ss.setPackage(username, packageId);
        return "redirect:/admin/user/show/" + username + "/";
    }      
    
    @RequestMapping(value = "/user/ipadd/", method=RequestMethod.POST)
    public String useripadd(@RequestParam("username") String username, @RequestParam("ip") String ip) 
    {   
        ss.connect(username, ip, 0);
        return "redirect:/admin/user/show/" + username + "/";
    }      
    
    @RequestMapping(value = "/user/ipremove/{username}/")
    public String useripremove(@PathVariable("username") String username, @RequestParam("ip") String ip) 
    {   
        ss.disconnect(username, ip, 2);
        return "redirect:/admin/user/show/" + username + "/";
    }        
    
    @RequestMapping(value = "/user/allipremove/{username}/")
    public String userallipremove(@PathVariable("username") String username) 
    {   
        ss.disconnect(username, 2);
        return "redirect:/admin/online";
    }      
    
    @RequestMapping(value = "/search", method=RequestMethod.GET)
    public String search(Model model)
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Subscriber subscriber = ss.getValidSubscriber(auth.getName());
        model.addAttribute("subscriber", subscriber);         
        return "admin/search";
    }    
    
    @RequestMapping(value = "/search/username/{username}/", method=RequestMethod.GET)
    public String searchusername(Model model, @PathVariable("username") String username)
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Subscriber subscriber = ss.getValidSubscriber(auth.getName());
        model.addAttribute("subscriber", subscriber);         
        return "admin/search";
    }
    
    @RequestMapping(value = "/search/ip", method=RequestMethod.POST)
    public String searchbyip(@RequestParam("ip") String ip, 
            @RequestParam("startdate") String startdate, @RequestParam("enddate") String enddate, 
            @RequestParam("shh") String shh, @RequestParam("ehh") String ehh,
            @RequestParam("smm") String smm, @RequestParam("emm") String emm,
            Model model) 
    {   
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Subscriber subscriber = ss.getValidSubscriber(auth.getName());
        model.addAttribute("subscriber", subscriber);         
        model.addAttribute("ip", ip);        
        model.addAttribute("username", "");
        model.addAttribute("startdate", startdate);        
        model.addAttribute("enddate", enddate);        
        model.addAttribute("shh", shh);
        model.addAttribute("smm", smm);
        model.addAttribute("ehh", ehh);
        model.addAttribute("emm", emm);
        List<TRData> trdata = ss.searchByIp(ip, startdate, shh, smm, enddate, ehh, emm);
        model.addAttribute("data", trdata);
        return "admin/searchshow";
    }       
    
    @RequestMapping(value = "/search/username", method=RequestMethod.POST)
    public String searchbyusername(@RequestParam("username") String username, 
            @RequestParam("startdate") String startdate, @RequestParam("enddate") String enddate, 
            @RequestParam("shh") String shh, @RequestParam("ehh") String ehh,
            @RequestParam("smm") String smm, @RequestParam("emm") String emm,            
            Model model) 
    {   
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Subscriber subscriber = ss.getValidSubscriber(auth.getName());
        model.addAttribute("subscriber", subscriber); 
        model.addAttribute("ip", ""); 
        model.addAttribute("username", username);        
        model.addAttribute("startdate", startdate);        
        model.addAttribute("enddate", enddate);     
        model.addAttribute("shh", shh);
        model.addAttribute("smm", smm);
        model.addAttribute("ehh", ehh);
        model.addAttribute("emm", emm);        
        List<TRData> trdata = ss.searchByUsername(username, startdate, shh, smm, enddate, ehh, emm);
        model.addAttribute("data", trdata);
        return "admin/searchshow";
    }
    
    @RequestMapping(value = "/search/lastauth", method=RequestMethod.POST)
    public String searchlastauth(@RequestParam("username") String username, Model model) 
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Subscriber subscriber = ss.getValidSubscriber(auth.getName());
        model.addAttribute("subscriber", subscriber);
        model.addAttribute("username", username);
        List<SubscriberLoggedRecord> slr = ss.getSubscriberLastLoggedRecords(username, 10);
        model.addAttribute("last10loging", slr);        
        return "admin/lastloginshow";
    }
    
    @RequestMapping(value = "/search/host", method=RequestMethod.POST)
    public String searchbyhost(@RequestParam("host") String host, 
            @RequestParam("startdate") String startdate, @RequestParam("enddate") String enddate, 
            @RequestParam("shh") String shh, @RequestParam("ehh") String ehh,
            @RequestParam("smm") String smm, @RequestParam("emm") String emm,
            Model model) 
    {   
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Subscriber subscriber = ss.getValidSubscriber(auth.getName());
        model.addAttribute("subscriber", subscriber);         
        model.addAttribute("host", host);        
        model.addAttribute("startdate", startdate);        
        model.addAttribute("enddate", enddate);        
        model.addAttribute("shh", shh);
        model.addAttribute("smm", smm);
        model.addAttribute("ehh", ehh);
        model.addAttribute("emm", emm);
        List<TRData> trdata = ss.searchByHost(host, startdate, shh, smm, enddate, ehh, emm);
        model.addAttribute("data", trdata);
        return "admin/searchhostshow";
    }      
    
    @RequestMapping(value = "/home", produces = "text/plain;charset=UTF-8")
    public String home(Model model) 
    {   
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Subscriber subscriber = ss.getValidSubscriber(auth.getName());
        model.addAttribute("subscriber", subscriber);         
        return "admin/home";
    }    
    
    @RequestMapping(value = "/top", produces = "text/plain;charset=UTF-8")
    public String top(Model model) 
    {   
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Subscriber subscriber = ss.getValidSubscriber(auth.getName());
        model.addAttribute("subscriber", subscriber);         
        List<SubscriberTotalTopUsage> toptoday = ss.getTodayTotalTop();
        List<SubscriberTotalTopUsage> topyesterday = ss.getYesterdayTotalTop();
        List<SubscriberTotalTopUsage> topweek = ss.getThisWeekTotalTop();
        List<SubscriberTotalTopUsage> toplast7 = ss.getLast7DaysTotalTop();
        List<SubscriberTotalTopUsage> topmonth = ss.getThisMonthTotalTop();
        List<SubscriberTotalTopUsage> toplast30 = ss.getLast30DaysTotalTop();
        model.addAttribute("toptoday", toptoday);
        model.addAttribute("topyesterday", topyesterday);
        model.addAttribute("topweek", topweek);
        model.addAttribute("toplast7", toplast7);
        model.addAttribute("topmonth", topmonth);
        model.addAttribute("toplast30", toplast30);
        return "admin/top";
    }        
    
    @RequestMapping(value = "/help", produces = "text/plain;charset=UTF-8")
    public String help(Model model) 
    {   
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Subscriber subscriber = ss.getValidSubscriber(auth.getName());
        model.addAttribute("subscriber", subscriber);         
        return "admin/help";
    }      
    
    @RequestMapping(value = "/authlog", produces = "text/plain;charset=UTF-8")
    public String authlog(Model model) 
    {   
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Subscriber subscriber = ss.getValidSubscriber(auth.getName());
        model.addAttribute("subscriber", subscriber);   
        List<SubscriberLoggedRecord> clog = ss.getLastLoginSubscribersRecords();
        List<SubscriberLoggedRecord> dlog = ss.getLastLogoutSubscribersRecords();
        model.addAttribute("clog", clog);   
        model.addAttribute("dlog", dlog);   
        return "admin/authlog";
    }      
}
