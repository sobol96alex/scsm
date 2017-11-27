package by.unit.bsu.scsm.domain;

public class SubscriberLoggedRecord {
    private final String username;
    private final String ip;
    private final String datetime;
    private final Integer package_id;
    private final String ddatetime;
    private final Integer reason;
    
    private final Integer REASON_CONNECT = 0;
    private final Integer REASON_USER = 1;
    private final Integer REASON_ADMIN = 2;
    private final Integer REASON_TIMEOUT = 3;
    
    public SubscriberLoggedRecord(String username, String ip, String datetime, Integer package_id, String ddatetime, Integer reason)
    {
        this.username = username;
        this.ip = ip;
        this.datetime = datetime;
        this.package_id = package_id;
        this.ddatetime = ddatetime;
        this.reason = reason;
    }  
    
    public String getUsername()
    {
        return this.username;
    }
    
    
    public String getIp()
    {
        return this.ip;
    }
    
    public String getDatetime()
    {
        return this.datetime;
    }
    
    public Integer getPackage_id()
    {
        return this.package_id;
    }
    
    public String getDdatetime()
    {
        return this.ddatetime;
    }
    
    public Integer getReason()
    {
        return this.reason;
    }
}
