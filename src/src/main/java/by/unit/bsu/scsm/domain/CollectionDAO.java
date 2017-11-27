package by.unit.bsu.scsm.domain;

import java.util.List;

public interface CollectionDAO {
    
    public List<TRData> getTRByIp(String ip, String start, String end);
    public List<TRData> getTRByUsername(String username, String start, String end);
    public List<TRData> getTRByHostString(String host, String start, String end);
    
    //public DailyUsageData getDailyUsage(String username, String date);
    //public List<DailyUsageData> getWeekDailyUsage(String username, String today);
    
    public List<SubscriberTotalTopUsage> getTodayTotalTop(Integer count);
    public List<SubscriberTotalTopUsage> getYesterdayTotalTop(Integer count);
    //public List<SubscriberTotalTopUsage> getDailyTotalTop(Integer count, String datetime);
    //@Cacheable("thisweektop")    
    public List<SubscriberTotalTopUsage> getThisWeekTotalTop(Integer count);
    //public List<SubscriberTotalTopUsage> getLastWeekTotalTop(Integer count);
    //@Cacheable("last7daystop")
    public List<SubscriberTotalTopUsage> getLast7DaysTotalTop(Integer count);
    //public List<SubscriberTotalTopUsage> get7DaysTotalTop(Integer count, String datetime);
    //@Cacheable("thismonthtop")    
    public List<SubscriberTotalTopUsage> getThisMonthTotalTop(Integer count);
    //public List<SubscriberTotalTopUsage> getLastMonthTotalTop(Integer count);
    //@Cacheable("last30daystop")
    public List<SubscriberTotalTopUsage> getLast30DaysTotalTop(Integer count);
    //public List<SubscriberTotalTopUsage> get30DaysTotalTop(Integer count, String datetime);
}
