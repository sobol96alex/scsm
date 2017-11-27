package by.unit.bsu.scsm.domain;

public class SubscriberIp {
    private final String ip;
    private final String datetime;
    private final Integer sessiontime;
    
    public SubscriberIp(String ip, String datetime, Integer sessiontime)
    {
        this.ip = ip;
        this.datetime = datetime;
        this.sessiontime = sessiontime;
    }    
    
    public String getIp()
    {
        return this.ip;
    }
    
    public String getDateTime()
    {
        return this.datetime;
    }	
    
    public Integer getSessionTime()
    {
        return this.sessiontime;
    }
}
