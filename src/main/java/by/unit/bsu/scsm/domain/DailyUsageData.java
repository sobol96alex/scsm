package by.unit.bsu.scsm.domain;

public class DailyUsageData {
    private final String username;
    private final String date;
    private final Integer down_usage;
    private final Integer up_usage;
    
    public DailyUsageData(String username, String date, Integer down_usage, Integer up_usage)
    {
        this.username = username;
        this.date = date;
        this.down_usage = down_usage;
        this.up_usage = up_usage;
    }
    
    public String getUsername() 
    {
        return this.username;
    }
    
    public String getDate()
    {
        return this.date;
    }
    
    public Integer getDownUsage()
    {
        return this.down_usage;
    }    
    
    public Integer getKDownUsage()
    {
        return this.down_usage;
    }
    
    public float getMDownUsage()
    {
        return (this.down_usage / 1024);
    }
    
    public float getGDownUsage()
    {
        return (this.down_usage / 1024 / 1024);
    }
    
    public Integer getUpUsage() 
    {
        return this.up_usage;
    }
    
    public Integer getKUpUsage() 
    {
        return this.up_usage;
    }    
    
    public float getMUpUsage() 
    {
        return (this.up_usage / 1024);
    }
    
    public float getGUpUsage() 
    {
        return (this.up_usage / 1024 / 1024);
    }    
    
    public Integer getTotalUsage()
    {
        return this.up_usage + this.down_usage;
    }
    
    public Integer getKTotalUsage()
    {
        return this.up_usage + this.down_usage;
    }
    
    public float getMTotalUsage()
    {
        return (this.up_usage + this.down_usage) / 1024;
    }
    
    public Integer getGTotalUsage()
    {
        return (this.up_usage + this.down_usage) / 1024 / 1024;
    }    
}
