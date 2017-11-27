package by.unit.bsu.scsm.domain;

import java.util.List;

public interface SubscriberDAO {
    
    public void addSubscriber(String username, Integer packageId, Integer maxsessioncount, Integer maxsessiontime, Integer type, Integer active, String comments);
    public void removeSubscriber(String username);
    public void updateSubscriber(String username, Integer packageId, Integer maxsessioncount, Integer maxsessiontime, Integer type, Integer active, String comments);
    public void updateSubscriber(Subscriber subscriber);
    public void updateSubscriberPackage(String username, Integer packageId);
    public void setActive(String username, Integer active);
    public void setComments(String username, String comments);
    public void setMaxSessionCount(String username, Integer count);
    public void setMaxSessionTime(String username, Integer time);
    public void setUserType(String username, Integer type);
    
    public void addSubscriberIp(String username, String ip, Integer package_id, Integer sessiontime);
    public List<SubscriberIp> getSubscriberIpList(String username);
    public Integer getSusbcriberIpCount(String username);
    public void removeSubscriberIp(String username, String ip, Integer reason);
    public void removeSubscriberIp(String ip, Integer reason);
    public void removeAllSubscriberIp(String username, Integer reason);
    
    public List<Subscriber> getAllSubscribers();
    public List<Subscriber> getAllIntroducedSubscribers();
    public Subscriber getSubscriber(String username);
    public Subscriber getSubscriberByIp(String ip);
    public boolean isConnected(String username);
    public boolean isConnected(String username, String ip); 

    public List<String> getExpiredSubscribersIp();
    
    public void addConnectedLoggedSubscriber(String username, String ip, Integer package_id);
    public void addDisconnectedLoggedSubscriber(String username, Integer reason);
    public void addDisconnectedLoggedSubscriber(String username, String ip, Integer reason);
    public void addDisconnectedLoggedSubscriberIp(String ip, Integer reason);

    public List<SubscriberLoggedRecord> getLoggedSubscriberRecords(String username);
    public List<SubscriberLoggedRecord> getLoggedSubscriberRecords(String username, int count);
    public List<SubscriberLoggedRecord> getLastLoginSubscribersRecords(int count);
    public List<SubscriberLoggedRecord> getLastLogoutSubscribersRecords(int count);
    //getLoggedSubscribersByDate(String day)
    //getLoggedSubscribersByDate(String after, String before)
    //and other admin staff
}
