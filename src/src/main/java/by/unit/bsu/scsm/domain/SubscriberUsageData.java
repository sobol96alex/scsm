package by.unit.bsu.scsm.domain;

public class SubscriberUsageData {

    private final String username;
    private final Integer down_kb;
    private final Integer up_kb;
    private final Integer sessions;
    private final Integer duration;
    
    public SubscriberUsageData(String username, Integer down_kb, Integer up_kb, Integer sessions, Integer duration)
    {
        this.username = username;
        this.down_kb = down_kb;
        this.up_kb = up_kb;
        this.sessions = sessions;
        this.duration = duration;
    }
    
    public String getUsername()
    {
        return this.username;
    }
    
    public Integer getDownKb()
    {
        return this.down_kb;
    }
    
    public Integer getDownMb()
    {
        return this.getDownKb() / 1024;
    }
    
    public Integer getUpKb()
    {
        return this.up_kb;
    }
    
    public Integer getUpMb()
    {
        return this.getUpKb() / 1024;
    }
    
    public Integer getSessionsCount()
    {
        return this.sessions;
    }
    
    public Integer getDurationSeconds()
    {
        return this.duration;
    }
    
    public Integer getDurationMinutes()
    {
        return this.getDurationSeconds() / 60;
    }
}
