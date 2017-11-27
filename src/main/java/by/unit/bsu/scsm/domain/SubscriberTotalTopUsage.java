package by.unit.bsu.scsm.domain;

public class SubscriberTotalTopUsage {
    private final String username;
    private final Long kbytes;
    private final Integer period;
    
    SubscriberTotalTopUsage(String username, Long kbytes)
    {
        this.username = username;
        this.kbytes = kbytes;
        this.period = 1440;
    }    
    
    SubscriberTotalTopUsage(String username, Long kbytes, Integer period)
    {
        this.username = username;
        this.kbytes = kbytes;
        this.period = period;
    }
    
    public String getUsername()
    {
        return this.username;
    }
    
    public Long getKbytes()
    {
        return this.kbytes;
    }           
    
    public Long getMbytes()
    {
        return this.kbytes / 1024;
    }
    
    public Integer getPeriod()
    {
        return this.period;
    }
}
