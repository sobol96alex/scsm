package by.unit.bsu.scsm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//import org.springframework.scheduling.annotation.Scheduled;

import by.unit.bsu.scsm.domain.SubscriberDAO;
import by.unit.bsu.scsm.domain.Subscriber;
import by.unit.bsu.scsm.domain.SubscriberIp;
import by.unit.bsu.scsm.domain.CollectionDAO;
import by.unit.bsu.scsm.domain.SubscriberLoggedRecord;
import by.unit.bsu.scsm.domain.SubscriberTotalTopUsage;
import by.unit.bsu.scsm.domain.TRData;

import com.scms.common.SubscriberID_BULK;

@Service
public class SubscriberService implements com.scms.api.sce.LogoutListener {
    
    @Autowired
    private SubscriberDAO sb;
    
    @Autowired
    private CollectionDAO cm;
    
    @Autowired
    private SceService sce;
    
    public SubscriberService()
    {
        
    }
    
    /*
    @Scheduled(initialDelay=180000, fixedRate = 60000)
    public void disconnectExpired() {
        //System.out.println("Sheduler time");
        disconnectExpiredSubscribers(); 
    }*/    
    
    public void createSubscriber(String username)
    {
        this.createSubscriber(username, 0);
    }
    
    public void createSubscriber(String username, Integer type)
    {
        this.createSubscriber(username, 1, 2, 0, type, 1, "");
    }    
    
    public void createSubscriber(String username, Integer packageId, Integer maxsessioncount, Integer maxsessiontime, Integer type, Integer active, String comments)
    {
        sb.addSubscriber(username, packageId, maxsessioncount, maxsessiontime, type, active, comments);
    }
    
    public void deleteSubscriber(String username)
    {
        this.disconnect(username, 2);
        sb.removeSubscriber(username);
    }
    
    public void connect(String username, String ip, Integer sessiontime)
    {
        Subscriber subscriber = sb.getSubscriber(username);
        if(subscriber == null)
            return;
        if((subscriber.getSubscriberMaxSessionCount() == 0) || ((subscriber.getSubscriberMaxSessionCount() - subscriber.getSubscriberIpList().size()) > 0))
        {
            if(sessiontime > subscriber.getSubscriberMaxSessionTime()) 
                sessiontime=subscriber.getSubscriberMaxSessionTime();
            sb.addSubscriberIp(username, ip, subscriber.getPackageId(), sessiontime);
            sce.loginSubscriber(username.toUpperCase(), subscriber.getPackageId(), ip, true);
        }
    }
    
    public void reconnect(String username, String ip)
    {
        Subscriber subscriber = sb.getSubscriber(username);
        if(subscriber == null)
            return;
        sce.loginSubscriber(username.toUpperCase(), subscriber.getPackageId(), ip, true);
    }    
    
    public void reconnectAll()
    {
        List<Subscriber> subs = this.getAllSubscribers();
        for (Subscriber sub : subs) 
        {
            String username = sub.getUsername();
            List<SubscriberIp> iplist = sub.getSubscriberIpList();
            if(!iplist.isEmpty())
            {
                for (SubscriberIp sip : iplist)
                {
                    String ip = sip.getIp();
                    sce.loginSubscriber(username.toUpperCase(), sub.getPackageId(), ip, true);
                }
            }
        }
    }
    
    public void disconnect(String username, String ip, Integer reason)
    {
        sb.removeSubscriberIp(username, ip, reason);
        sce.logoutSusbcriber(username.toUpperCase(), ip);
    }
    
    public void disconnect(String username, Integer reason)
    {
        sb.removeAllSubscriberIp(username, reason);
        sce.logoutSubscriber(username.toUpperCase());
    }
    
    public void disconnectExpiredSubscribers()
    {
        List<String> iplist = sb.getExpiredSubscribersIp();
        if(iplist != null)
        {
            for(String ip : iplist)
            {
                sb.removeSubscriberIp(ip, 3);
                sce.logoutSubscriberIp(ip);
            }
        }
    }    
    
    public List<SubscriberIp> getCurrentConnections(String username)
    {
        return sb.getSubscriberIpList(username);
    }
    
    public Subscriber getValidSubscriber(String username)
    {
        Subscriber subscriber = this.getSubscriber(username);
        if(subscriber == null)
        {
            this.createSubscriber(username, 2);
            subscriber = this.getSubscriber(username);
        }
        return subscriber;
    }
    
    public Subscriber getSubscriber(String username)
    {
        return sb.getSubscriber(username);
    }
    
    public boolean hasConnectedIp(String username, String ip)
    {
        return sb.isConnected(username, ip);
    }
    
    public List<Subscriber> getAllSubscribers()
    {
        return sb.getAllSubscribers();
    }
    
    public List<Subscriber> getAllIntroducedSubscribers()
    {
        return sb.getAllIntroducedSubscribers();
    }    
    
    public void setEnabled(String username)
    {
        sb.setActive(username, 1);
    }
    
    public void setDisabled(String username)
    {
        this.disconnect(username, 2);
        sb.setActive(username, 0);
    }
    
    public void updateSubscriber(String username, Integer packageId, Integer maxsessioncount, Integer maxsessiontime, Integer type, Integer active, String comments)
    {
        sb.updateSubscriber(username, packageId, maxsessioncount, maxsessiontime, type, active, comments);
        this.setPackage(username, packageId);
    }
    
    public void setPackage(String username, Integer packageId)
    {
        sb.updateSubscriberPackage(username, packageId);
        sce.changePackage(username.toUpperCase(), packageId);
    }
    
    public List<SubscriberLoggedRecord> getSubscriberLastLoggedRecords(String username)
    {
        return getSubscriberLastLoggedRecords(username, 5);
    }
    
    public List<SubscriberLoggedRecord> getSubscriberLastLoggedRecords(String username, int n)
    {
        return sb.getLoggedSubscriberRecords(username, n);
    }    
    
    //get data from cm
    public List<TRData> searchByIp(String ip, String sdate, String sh, String sm, String edate, String eh, String em)
    {
        return cm.getTRByIp(ip, sdate + " " + sh + ":" + sm + ":00", edate + " " + eh + ":" + em + ":00");
    }
    
    public List<TRData> searchByUsername(String username, String sdate, String sh, String sm, String edate, String eh, String em)
    {
        return cm.getTRByUsername(username, sdate + " " + sh + ":" + sm + ":00", edate + " " + eh + ":" + em + ":00");
    }
    
    public List<TRData> searchByHost(String host, String sdate, String sh, String sm, String edate, String eh, String em)
    {
        return cm.getTRByHostString("%" + host + "%", sdate + " " + sh + ":" + sm + ":00", edate + " " + eh + ":" + em + ":00");
    }    
    
    @Override
    public void logoutBulkIndication(SubscriberID_BULK subs)
    {
        //Not impl
    }
    
    @Override //callback for SCE
    public void logoutIndication(java.lang.String subscriberID)
    {
        //callback on the last IP
        //note that is small ping from disconnect to listener invoke
        //remove from db. 
        //sb.removeAllSubscriberIp(subscriberID);
        //patched. disconnect again via listener
        this.disconnect(subscriberID, 3);
        //System.out.println(subscriberID);
    }    
    
    public List<SubscriberTotalTopUsage> getTodayTotalTop()
    {
        return cm.getTodayTotalTop(20);
    }
    
    public List<SubscriberTotalTopUsage> getYesterdayTotalTop()
    {
        return cm.getYesterdayTotalTop(20);
    }

    public List<SubscriberTotalTopUsage> getThisWeekTotalTop()
    {
        return cm.getThisWeekTotalTop(20);
    }
    
    public List<SubscriberTotalTopUsage> getLast7DaysTotalTop()
    {
        return cm.getLast7DaysTotalTop(20);
    }
    
    public List<SubscriberTotalTopUsage> getThisMonthTotalTop()
    {
        return cm.getThisMonthTotalTop(20);
    }
    
    public List<SubscriberTotalTopUsage> getLast30DaysTotalTop()
    {
        return cm.getLast30DaysTotalTop(20);
    }
    
    public List<SubscriberLoggedRecord> getLastLoginSubscribersRecords()
    {
        return sb.getLastLoginSubscribersRecords(20);
    }
    
    public List<SubscriberLoggedRecord> getLastLogoutSubscribersRecords()
    {
        return sb.getLastLogoutSubscribersRecords(20);
    }
}
