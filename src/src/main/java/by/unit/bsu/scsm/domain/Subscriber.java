package by.unit.bsu.scsm.domain;

import java.util.List;
import java.util.ArrayList;

import by.unit.bsu.scsm.domain.types.UserType;

public class Subscriber {
    private final String username;
    private final Integer packageId;
    private List<SubscriberIp> iplist = new ArrayList<SubscriberIp>();
    private final String datetime;
    private final Integer maxsessioncount;
    private final Integer maxsessiontime;
    private final UserType type;
    private final String comments;
    private final Integer active;
    
    public Subscriber(String username, Integer packageId, Integer maxsessioncount, Integer maxsessiontime, UserType type, String comments)
    {//code
        this.username = username;
        this.packageId = packageId;
        this.maxsessioncount = maxsessioncount;
        this.maxsessiontime = maxsessiontime;
        this.iplist = new ArrayList<SubscriberIp>();
        this.datetime = "";
        this.type = type;
        this.comments = comments;
        this.active = 1;
    }
    
    public Subscriber(String username, Integer packageId, Integer maxsessioncount, Integer maxsessiontime, List<SubscriberIp> iplist, Integer type, String comments, Integer active, String datetime)
    {//jdbc
        this.username = username;
        this.packageId = packageId;
        this.iplist = new ArrayList<SubscriberIp>();
        this.iplist.addAll(iplist);
        this.datetime = datetime;
        this.maxsessioncount = maxsessioncount;
        this.maxsessiontime = maxsessiontime;
        this.type = UserType.forId(type);
        this.comments = comments;
        this.active = active;
    }
    
    /*
    @Override
    public String toString() {
        return "Subscriber [username=" + this.username + ", paclageId=" + this.packageId
                + ", datetime=" + this.datetime + ", iplist=" + iplist + "]";
    }*/
    
    public String getUsername()
    {
        return this.username;
    }
    
    public Integer getPackageId()
    {
        return this.packageId;
    }
    
    public List<SubscriberIp> getSubscriberIpList()
    {
        return this.iplist;
    }
    
    public String getSubscriberUpdateDateTime()
    {
        return this.datetime;
    }
    
    public Integer getSubscriberMaxSessionCount()
    {
        return this.maxsessioncount;
    }
    
    public Integer getSubscriberCurrentSessionCount()
    {
        return this.iplist.size();
    }
    
    public Integer getSubscriberMaxSessionTime()
    {
        return this.maxsessiontime;
    }
    
    public UserType getType()
    {
        return this.type;
    }
    
    public Integer getTypeId()
    {
        return this.type.getId();
    }
    
    public String getTypeName()
    {
        return this.type.getName();
    }
    
    public String getComments()
    {
        return this.comments;
    }
    
    public boolean isActive()
    {
        return active > 0;
    }
    
    public Integer getActive()
    {
        return this.active;
    }
}
