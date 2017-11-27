package by.unit.bsu.scsm.domain;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.dao.DataAccessException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CollectionManager implements CollectionDAO {
    
    private JdbcTemplate jdbcTemplate;
    
    private static final SimpleDateFormat dbDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private static final SimpleDateFormat viewDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");

    @Autowired
    public void init(DataSource cmDataSource)
    {
        this.jdbcTemplate = new JdbcTemplate(cmDataSource);
    }    
    
    @Override
    public List<TRData> getTRByIp(String ip, String start, String end)
    {
        try {
            List<TRData> datalist = this.jdbcTemplate.query(
                "SELECT time_stamp, subscriber_id, service_id, protocol_id, "
                    + "INET_NTOA(peer_ip) as peer_ip, peer_port, access_string, info_string, "
                    + "INET_NTOA(source_ip) as source_ip, source_port, "
                    + "downstream_volume, upstream_volume FROM RPT_TR "
                    + "WHERE (source_ip = INET_ATON(?) OR peer_ip = INET_ATON(?)) AND (time_stamp > ? AND time_stamp < ?)",
                new Object[] {ip, ip, start, end},
                new RowMapper<TRData>() 
                {
                    @Override
                    public TRData mapRow(ResultSet rs, int rowNum) throws SQLException
                    {
                        TRData tr = new TRData(rs.getString("time_stamp"), rs.getString("subscriber_id"), 
                                rs.getInt("service_id"), rs.getInt("protocol_id"), 
                                rs.getString("peer_ip"), rs.getInt("peer_port"), 
                                rs.getString("access_string"), rs.getString("info_string"), 
                                rs.getString("source_ip"), rs.getInt("source_port"), 
                                rs.getInt("downstream_volume"), rs.getInt("upstream_volume"));
                        return tr;
                    }
                });
            return datalist;
        } catch (DataAccessException e) {
            //System.out.println(e.toString());
            return null;
        }        
    }
    
    @Override
    public List<TRData> getTRByUsername(String username, String start, String end)
    {
        try {
            List<TRData> datalist = this.jdbcTemplate.query(
                "SELECT time_stamp, subscriber_id, service_id, protocol_id, "
                    + "INET_NTOA(peer_ip) as peer_ip, peer_port, access_string, info_string, "
                    + "INET_NTOA(source_ip) as source_ip, source_port, "
                    + "downstream_volume, upstream_volume FROM RPT_TR "
                    + "WHERE (subscriber_id = ?) AND (time_stamp > ? AND time_stamp < ?)",
                new Object[] {username, start, end},
                new RowMapper<TRData>() 
                {
                    @Override
                    public TRData mapRow(ResultSet rs, int rowNum) throws SQLException
                    {
                        TRData tr = new TRData(rs.getString("time_stamp"), rs.getString("subscriber_id"), 
                                rs.getInt("service_id"), rs.getInt("protocol_id"), 
                                rs.getString("peer_ip"), rs.getInt("peer_port"), 
                                rs.getString("access_string"), rs.getString("info_string"), 
                                rs.getString("source_ip"), rs.getInt("source_port"), 
                                rs.getInt("downstream_volume"), rs.getInt("upstream_volume"));
                        return tr;
                    }
                });
            return datalist;
        } catch (DataAccessException e) {
            return null;
        }           
    }
    
    @Override
    public List<TRData> getTRByHostString(String host, String start, String end)
    {
        try {
            List<TRData> datalist = this.jdbcTemplate.query(
                "SELECT time_stamp, subscriber_id, service_id, protocol_id, "
                    + "INET_NTOA(peer_ip) as peer_ip, peer_port, access_string, info_string, "
                    + "INET_NTOA(source_ip) as source_ip, source_port, "
                    + "downstream_volume, upstream_volume FROM RPT_TR "
                    + "WHERE (access_string LIKE ?) AND (end_time > UNIX_TIMESTAMP(?) AND end_time < UNIX_TIMESTAMP(?))",
                new Object[] {host, start, end},
                new RowMapper<TRData>() 
                {
                    @Override
                    public TRData mapRow(ResultSet rs, int rowNum) throws SQLException
                    {
                        TRData tr = new TRData(rs.getString("time_stamp"), rs.getString("subscriber_id"), 
                                rs.getInt("service_id"), rs.getInt("protocol_id"), 
                                rs.getString("peer_ip"), rs.getInt("peer_port"), 
                                rs.getString("access_string"), rs.getString("info_string"), 
                                rs.getString("source_ip"), rs.getInt("source_port"), 
                                rs.getInt("downstream_volume"), rs.getInt("upstream_volume"));
                        return tr;
                    }
                });
            return datalist;
        } catch (DataAccessException e) {
            return null;
        }           
    }
    
    @Override
    public List<SubscriberTotalTopUsage> getTodayTotalTop(Integer count)
    {
        try {
            List<SubscriberTotalTopUsage> toplist = this.jdbcTemplate.query(
                "SELECT subscriber_id, SUM(consumption) AS traf "
                    + "FROM RPT_TOPS_PERIOD0 "
                    + "WHERE metric_id = 2 AND ip_type = -1 AND subs_usg_cnt_id = -1 "
                    + " AND time_stamp > (DATE(NOW()) + INTERVAL 1 HOUR) GROUP BY subscriber_id ORDER BY traf DESC LIMIT ?",
                new Object[] {count},
                new RowMapper<SubscriberTotalTopUsage>() 
                {
                    @Override
                    public SubscriberTotalTopUsage mapRow(ResultSet rs, int rowNum) throws SQLException
                    {
                        SubscriberTotalTopUsage tr = new SubscriberTotalTopUsage(rs.getString("subscriber_id"), 
                                                                            rs.getLong("traf"), 1440); 
                        return tr;
                    }
                });
            return toplist;
        } catch (DataAccessException e) {
            return null;
        }          
    }
    
    @Override
    public List<SubscriberTotalTopUsage> getYesterdayTotalTop(Integer count)
    {
        try {
            List<SubscriberTotalTopUsage> toplist = this.jdbcTemplate.query(
                "SELECT subscriber_id, consumption "
                    + "FROM RPT_TOPS_PERIOD1 "
                    + "WHERE metric_id = 2 AND ip_type = -1 AND subs_usg_cnt_id = -1 "
                    + " AND time_stamp > DATE(NOW()) ORDER BY consumption DESC LIMIT ?",
                new Object[] {count},
                new RowMapper<SubscriberTotalTopUsage>() 
                {
                    @Override
                    public SubscriberTotalTopUsage mapRow(ResultSet rs, int rowNum) throws SQLException
                    {
                        SubscriberTotalTopUsage tr = new SubscriberTotalTopUsage(rs.getString("subscriber_id"), 
                                                                            rs.getLong("consumption"), 1440); 
                        return tr;
                    }
                });
            return toplist;
        } catch (DataAccessException e) {
            return null;
        }          
    }
    
    @Override
    public List<SubscriberTotalTopUsage> getThisWeekTotalTop(Integer count)
    {
        try {
            List<SubscriberTotalTopUsage> toplist = this.jdbcTemplate.query(
                "SELECT subscriber_id, SUM(consumption) AS traf "
                    + "FROM RPT_TOPS_PERIOD1 "
                    + "WHERE metric_id = 2 AND ip_type = -1 AND subs_usg_cnt_id = -1 "
                    + " AND time_stamp > (CURDATE() - INTERVAL WEEKDAY(CURDATE()) DAY) GROUP BY subscriber_id ORDER BY traf DESC LIMIT ?",
                new Object[] {count},
                new RowMapper<SubscriberTotalTopUsage>() 
                {
                    @Override
                    public SubscriberTotalTopUsage mapRow(ResultSet rs, int rowNum) throws SQLException
                    {
                        SubscriberTotalTopUsage tr = new SubscriberTotalTopUsage(rs.getString("subscriber_id"), 
                                                                            rs.getLong("traf"), 1440); 
                        return tr;
                    }
                });
            return toplist;
        } catch (DataAccessException e) {
            return null;
        }            
    }
    
    @Override
    public List<SubscriberTotalTopUsage> getLast7DaysTotalTop(Integer count)
    {
        try {
            List<SubscriberTotalTopUsage> toplist = this.jdbcTemplate.query(
                "SELECT subscriber_id, SUM(consumption) AS traf "
                    + "FROM RPT_TOPS_PERIOD1 "
                    + "WHERE metric_id = 2 AND ip_type = -1 AND subs_usg_cnt_id = -1 "
                    + " AND time_stamp > (DATE(NOW()) - INTERVAL 7 DAY) GROUP BY subscriber_id ORDER BY traf DESC LIMIT ?",
                new Object[] {count},
                new RowMapper<SubscriberTotalTopUsage>() 
                {
                    @Override
                    public SubscriberTotalTopUsage mapRow(ResultSet rs, int rowNum) throws SQLException
                    {
                        SubscriberTotalTopUsage tr = new SubscriberTotalTopUsage(rs.getString("subscriber_id"), 
                                                                            rs.getLong("traf"), 1440); 
                        return tr;
                    }
                });
            return toplist;
        } catch (DataAccessException e) {
            return null;
        }          
    }
    
    @Override
    public List<SubscriberTotalTopUsage> getThisMonthTotalTop(Integer count)
    {
        try {
            List<SubscriberTotalTopUsage> toplist = this.jdbcTemplate.query(
                "SELECT subscriber_id, SUM(consumption) AS traf "
                    + "FROM RPT_TOPS_PERIOD1 "
                    + "WHERE metric_id = 2 AND ip_type = -1 AND subs_usg_cnt_id = -1 "
                    + " AND time_stamp > (CURDATE() - INTERVAL DAYOFMONTH(CURDATE()) DAY) GROUP BY subscriber_id ORDER BY traf DESC LIMIT ?",
                new Object[] {count},
                new RowMapper<SubscriberTotalTopUsage>() 
                {
                    @Override
                    public SubscriberTotalTopUsage mapRow(ResultSet rs, int rowNum) throws SQLException
                    {
                        SubscriberTotalTopUsage tr = new SubscriberTotalTopUsage(rs.getString("subscriber_id"), 
                                                                            rs.getLong("traf"), 1440); 
                        return tr;
                    }
                });
            return toplist;
        } catch (DataAccessException e) {
            return null;
        }            
    }    
    
    @Override
    public List<SubscriberTotalTopUsage> getLast30DaysTotalTop(Integer count)
    {
        try {
            List<SubscriberTotalTopUsage> toplist = this.jdbcTemplate.query(
                "SELECT subscriber_id, SUM(consumption) AS traf "
                    + "FROM RPT_TOPS_PERIOD1 "
                    + "WHERE metric_id = 2 AND ip_type = -1 AND subs_usg_cnt_id = -1 "
                    + " AND time_stamp > (DATE(NOW()) - INTERVAL 30 DAY) GROUP BY subscriber_id ORDER BY traf DESC LIMIT ?",
                new Object[] {count},
                new RowMapper<SubscriberTotalTopUsage>() 
                {
                    @Override
                    public SubscriberTotalTopUsage mapRow(ResultSet rs, int rowNum) throws SQLException
                    {
                        SubscriberTotalTopUsage tr = new SubscriberTotalTopUsage(rs.getString("subscriber_id"), 
                                                                            rs.getLong("traf"), 1440); 
                        return tr;
                    }
                });
            return toplist;
        } catch (DataAccessException e) {
            return null;
        }          
    }    
}
