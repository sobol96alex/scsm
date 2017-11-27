package by.unit.bsu.scsm.domain;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.dao.DataAccessException;

import org.springframework.stereotype.Repository;

@Repository
public class SubscriberJdbcDAO implements SubscriberDAO {
    
    private JdbcTemplate jdbcTemplate;
    
    private static final SimpleDateFormat dbDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private static final SimpleDateFormat viewDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");

    @Autowired
    public void init(DataSource subDataSource)
    {
        this.jdbcTemplate = new JdbcTemplate(subDataSource);
    }
    
    @Override
    public void addSubscriber(String username, Integer packageId, Integer maxsessioncount, Integer maxsessiontime, Integer type, Integer active, String comments)
    {
        try { 
            this.jdbcTemplate.update("INSERT INTO PORTAL_subscribers (username, packageid, maxsessioncount, maxsessiontime, type, active, comments, dt) values (?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP())", 
                    new Object[] {username, packageId, maxsessioncount, maxsessiontime, type, active, comments});
        } catch (DataAccessException e) {
        }
    }
    
    @Override
    public void removeSubscriber(String username)
    {
        this.removeAllSubscriberIp(username, 2);
        try {
            this.jdbcTemplate.update("DELETE FROM PORTAL_subscribers WHERE username = ?", new Object[] {username});
        } catch (DataAccessException e) {
        }
    }
    
    @Override 
    public void updateSubscriber(Subscriber subscriber)
    {
        try {
            this.jdbcTemplate.update(
                    "UPDATE PORTAL_subscribers SET packageId =?, maxsessioncount =?, maxsessiontime =?, type =?, active =?, comments =?, dt = CURRENT_TIMESTAMP() WHERE username = ?", 
                    new Object[] {subscriber.getPackageId(), subscriber.getSubscriberMaxSessionCount(), subscriber.getSubscriberMaxSessionTime(), 
                        subscriber.getTypeId(), subscriber.getActive(), subscriber.getComments(), subscriber.getUsername()});
        } catch (DataAccessException e) {
        }
    }
    
    @Override 
    public void updateSubscriber(String username, Integer packageId, Integer maxsessioncount, Integer maxsessiontime, Integer type, Integer active, String comments)
    {
        try {
            this.jdbcTemplate.update(
                    "UPDATE PORTAL_subscribers SET packageId =?, maxsessioncount =?, maxsessiontime =?, type =?, active =?, comments =?, dt = CURRENT_TIMESTAMP() WHERE username = ?", 
                    new Object[] {packageId, maxsessioncount, maxsessiontime, 
                        type, active, comments, username});
        } catch (DataAccessException e) {
        }
    }    
    
    @Override
    public void updateSubscriberPackage(String username, Integer packageId)
    {
        try {
            this.jdbcTemplate.update(
                    "UPDATE PORTAL_subscribers SET packageId = ?, dt = CURRENT_TIMESTAMP() WHERE username = ?", 
                    new Object[] {packageId, username});
        }  catch (DataAccessException e) {
        }
    }
    
    @Override
    public void setActive(String username, Integer active)
    {
        try {
            this.jdbcTemplate.update(
                    "UPDATE PORTAL_subscribers SET active = ?, dt = CURRENT_TIMESTAMP() WHERE username = ?",
                    new Object[] {active, username});
        }  catch (DataAccessException e) {
        }
    }
    
    @Override
    public void setComments(String username, String comments)
    {
        try {
            this.jdbcTemplate.update(
                    "UPDATE PORTAL_subscribers SET comments = ?, dt = CURRENT_TIMESTAMP() WHERE username = ?",
                    new Object[] {comments, username});
        }  catch (DataAccessException e) {
        }
    }
    
    @Override
    public void setMaxSessionCount(String username, Integer count)
    {
        try {
            this.jdbcTemplate.update(
                    "UPDATE PORTAL_subscribers SET maxsessioncount = ?, dt = CURRENT_TIMESTAMP() WHERE username = ?",
                    new Object[] {count, username});
        }  catch (DataAccessException e) {
        }
    }
    
    @Override
    public void setMaxSessionTime(String username, Integer time)
    {
        try {
            this.jdbcTemplate.update(
                    "UPDATE PORTAL_subscribers SET maxsessiontime = ?, dt = CURRENT_TIMESTAMP() WHERE username = ?",
                    new Object[] {time, username});
        }  catch (DataAccessException e) {
        }
    }     
    
    @Override
    public void setUserType(String username, Integer type)
    {
        try {
            this.jdbcTemplate.update(
                    "UPDATE PORTAL_subscribers SET type = ?, dt = CURRENT_TIMESTAMP() WHERE username = ?",
                    new Object[] {type, username});
        }  catch (DataAccessException e) {
        }
    }            
        
    @Override
    public void addSubscriberIp(String username, String ip, Integer package_id, Integer sessiontime)
    {
        try {
        this.jdbcTemplate.update("INSERT INTO PORTAL_subscriberip (username, ip, sessiontime, dt) values (?, ?, ?, CURRENT_TIMESTAMP())", 
                new Object[] {username, ip, sessiontime});
        }  catch (DataAccessException e) {
        }
        this.addConnectedLoggedSubscriber(username, ip, package_id);
    }
    
    @Override
    public List<SubscriberIp> getSubscriberIpList(String username)
    {
        try {
            List<SubscriberIp> iplist = this.jdbcTemplate.query(
                "SELECT ip, sessiontime, dt FROM PORTAL_subscriberip WHERE username = ?",
                new Object[] {username},
                new RowMapper<SubscriberIp>() 
                {
                    @Override
                    public SubscriberIp mapRow(ResultSet rs, int rowNum) throws SQLException
                    {
                        SubscriberIp ip = new SubscriberIp(rs.getString("ip"), rs.getString("dt"), rs.getInt("sessiontime"));
                        return ip;
                    }
                });
            return iplist;
        } catch (DataAccessException e) {
            return null;
        }
    }
    
    @Override
    public Integer getSusbcriberIpCount(String username)
    {
        try {
            Integer ipCount = this.jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM PORTAL_subscriberip WHERE username = ?", new Object[] {username}, Integer.class);
            return ipCount;
        } catch (DataAccessException e) {
            return null;
        }
    }
    
    @Override
    public void removeSubscriberIp(String username, String ip, Integer reason)
    {
        try {
            this.jdbcTemplate.update("DELETE FROM PORTAL_subscriberip WHERE username = ? AND ip = ?", new Object[] {username, ip});
            this.addDisconnectedLoggedSubscriber(username, ip, reason);
        } catch (DataAccessException e) {
        }    
    }
    
    @Override
    public void removeSubscriberIp(String ip, Integer reason)
    {
        try {
            this.jdbcTemplate.update("DELETE FROM PORTAL_subscriberip WHERE ip = ?", new Object[] {ip});
            this.addDisconnectedLoggedSubscriberIp(ip, reason);
        } catch (DataAccessException e) {
        }                
    }
    
    @Override
    public void removeAllSubscriberIp(String username, Integer reason)
    {
        try {
            this.jdbcTemplate.update("DELETE FROM PORTAL_subscriberip WHERE username = ?", new Object[] {username});
            this.addDisconnectedLoggedSubscriber(username, reason);
        } catch (DataAccessException e) {    
        } 
    }
    
    @Override
    public List<Subscriber> getAllSubscribers()
    {
        try {
            final List<Subscriber> sl = new ArrayList<Subscriber>();
            this.jdbcTemplate.query(
                "SELECT  PORTAL_subscribers.username, PORTAL_subscriberip.ip, PORTAL_subscriberip.sessiontime, PORTAL_subscriberip.dt, "
                    + "PORTAL_subscribers.packageid, PORTAL_subscribers.maxsessioncount, PORTAL_subscribers.maxsessiontime, "
                    + "PORTAL_subscribers.type, PORTAL_subscribers.active, PORTAL_subscribers.comments, PORTAL_subscribers.dt "
                    + "FROM PORTAL_subscriberip "
                    + "RIGHT JOIN PORTAL_subscribers ON PORTAL_subscriberip.username = PORTAL_subscribers.username "
                    + "ORDER BY PORTAL_subscribers.username, PORTAL_subscriberip.dt", 
                new ResultSetExtractor()
                {
                    @Override
                    public Object extractData(ResultSet rs) throws SQLException, DataAccessException 
                    {
                        if(rs.next())
                        {
                            String username = rs.getString("PORTAL_subscribers.username");
                            Integer packageid = rs.getInt("PORTAL_subscribers.packageid");
                            Integer maxsessioncount = rs.getInt("PORTAL_subscribers.maxsessioncount");
                            Integer maxsessiontime = rs.getInt("PORTAL_subscribers.maxsessiontime");
                            Integer type = rs.getInt("PORTAL_subscribers.type");
                            Integer active = rs.getInt("PORTAL_subscribers.active");
                            String comments = rs.getString("PORTAL_subscribers.comments");
                            String dt = rs.getString("PORTAL_subscribers.dt");
                            List<SubscriberIp> iplist = new ArrayList<SubscriberIp>();
                            String ip = rs.getString("PORTAL_subscriberip.ip");
                            Integer sessiontime = rs.getInt("PORTAL_subscriberip.sessiontime");
                            String ipdt = rs.getString("PORTAL_subscriberip.dt");
                            if(ip != null)
                            {
                                SubscriberIp sip = new SubscriberIp(ip, ipdt, sessiontime);
                                iplist.add(sip);
                            }
                            while (rs.next()) 
                            {
                                if(username.equals(rs.getString("PORTAL_subscribers.username")))
                                {
                                    ip = rs.getString("PORTAL_subscriberip.ip");
                                    sessiontime = rs.getInt("PORTAL_subscriberip.sessiontime");
                                    ipdt = rs.getString("PORTAL_subscriberip.dt");
                                    SubscriberIp sip = new SubscriberIp(ip, ipdt, sessiontime);
                                    iplist.add(sip);                                
                                }
                                else
                                {
                                    Subscriber subscriber = new Subscriber(username, packageid, maxsessioncount, maxsessiontime, iplist, type, comments, active, dt);
                                    sl.add(subscriber);   
                                    username = rs.getString("PORTAL_subscribers.username");
                                    packageid = rs.getInt("PORTAL_subscribers.packageid");
                                    maxsessioncount = rs.getInt("PORTAL_subscribers.maxsessioncount");
                                    maxsessiontime = rs.getInt("PORTAL_subscribers.maxsessiontime");
                                    type = rs.getInt("PORTAL_subscribers.type");
                                    active = rs.getInt("PORTAL_subscribers.active");
                                    comments = rs.getString("PORTAL_subscribers.comments");                                    
                                    dt = rs.getString("PORTAL_subscribers.dt");
                                    iplist.clear();
                                    ip = rs.getString("PORTAL_subscriberip.ip");
                                    sessiontime = rs.getInt("PORTAL_subscriberip.sessiontime");
                                    ipdt = rs.getString("PORTAL_subscriberip.dt");
                                    if(ip != null)
                                    {
                                        SubscriberIp sip = new SubscriberIp(ip, ipdt, sessiontime);
                                        iplist.add(sip);
                                    }                                
                                }
                            }
                            Subscriber subscriber = new Subscriber(username, packageid, maxsessioncount, maxsessiontime, iplist, type, comments, active, dt);
                            sl.add(subscriber);
                        }
                    return sl;
                    }
                }
            );
            return sl;
        } catch (DataAccessException e) {    
            return null;
        }
    }
    
    @Override //TODO
    public List<Subscriber> getAllIntroducedSubscribers()
    {
        try {
            final List<Subscriber> sl = new ArrayList<Subscriber>();
            this.jdbcTemplate.query(
                "SELECT  PORTAL_subscribers.username, PORTAL_subscriberip.ip, PORTAL_subscriberip.sessiontime, PORTAL_subscriberip.dt, "
                    + "PORTAL_subscribers.packageid, PORTAL_subscribers.maxsessioncount, PORTAL_subscribers.maxsessiontime, "
                    + "PORTAL_subscribers.type, PORTAL_subscribers.active, PORTAL_subscribers.comments, PORTAL_subscribers.dt "
                    + "FROM PORTAL_subscriberip "
                    + "RIGHT JOIN PORTAL_subscribers ON PORTAL_subscriberip.username = PORTAL_subscribers.username "
                    + "ORDER BY PORTAL_subscribers.username, PORTAL_subscriberip.dt", 
                new ResultSetExtractor()
                {
                    @Override
                    public Object extractData(ResultSet rs) throws SQLException, DataAccessException 
                    {
                        if(rs.next())
                        {
                            String username = rs.getString("PORTAL_subscribers.username");
                            Integer packageid = rs.getInt("PORTAL_subscribers.packageid");
                            Integer maxsessioncount = rs.getInt("PORTAL_subscribers.maxsessioncount");
                            Integer maxsessiontime = rs.getInt("PORTAL_subscribers.maxsessiontime");
                            Integer type = rs.getInt("PORTAL_subscribers.type");
                            Integer active = rs.getInt("PORTAL_subscribers.active");
                            String comments = rs.getString("PORTAL_subscribers.comments");
                            String dt = rs.getString("PORTAL_subscribers.dt");
                            List<SubscriberIp> iplist = new ArrayList<SubscriberIp>();
                            String ip = rs.getString("PORTAL_subscriberip.ip");
                            Integer sessiontime = rs.getInt("PORTAL_subscriberip.sessiontime");
                            String ipdt = rs.getString("PORTAL_subscriberip.dt");
                            if(ip != null)
                            {
                                SubscriberIp sip = new SubscriberIp(ip, ipdt, sessiontime);
                                iplist.add(sip);
                            }
                            while (rs.next()) 
                            {
                                if(username.equals(rs.getString("PORTAL_subscribers.username")))
                                {
                                    ip = rs.getString("PORTAL_subscriberip.ip");
                                    sessiontime = rs.getInt("PORTAL_subscriberip.sessiontime");
                                    ipdt = rs.getString("PORTAL_subscriberip.dt");
                                    SubscriberIp sip = new SubscriberIp(ip, ipdt, sessiontime);
                                    iplist.add(sip);                                
                                }
                                else
                                {
                                    if(iplist.size() > 0)
                                    {
                                        Subscriber subscriber = new Subscriber(username, packageid, maxsessioncount, maxsessiontime, iplist, type, comments, active, dt);
                                        sl.add(subscriber);   
                                    }
                                    username = rs.getString("PORTAL_subscribers.username");
                                    packageid = rs.getInt("PORTAL_subscribers.packageid");
                                    maxsessioncount = rs.getInt("PORTAL_subscribers.maxsessioncount");
                                    maxsessiontime = rs.getInt("PORTAL_subscribers.maxsessiontime");
                                    type = rs.getInt("PORTAL_subscribers.type");
                                    active = rs.getInt("PORTAL_subscribers.active");
                                    comments = rs.getString("PORTAL_subscribers.comments");                                    
                                    dt = rs.getString("PORTAL_subscribers.dt");
                                    iplist.clear();
                                    ip = rs.getString("PORTAL_subscriberip.ip");
                                    sessiontime = rs.getInt("PORTAL_subscriberip.sessiontime");
                                    ipdt = rs.getString("PORTAL_subscriberip.dt");
                                    if(ip != null)
                                    {
                                        SubscriberIp sip = new SubscriberIp(ip, ipdt, sessiontime);
                                        iplist.add(sip);
                                    }                                
                                }
                            }
                            if(iplist.size() > 0)
                            {
                                Subscriber subscriber = new Subscriber(username, packageid, maxsessioncount, maxsessiontime, iplist, type, comments, active, dt);
                                sl.add(subscriber);
                            }
                        }
                    return sl;
                    }
                }
            );
            return sl;
        } catch (DataAccessException e) {    
            return null;
        }
    }
    
    @Override
    public Subscriber getSubscriber(String username)
    {
        try {
            final List<SubscriberIp> iplist = this.getSubscriberIpList(username);
            Subscriber subscriber = this.jdbcTemplate.queryForObject(
                "SELECT username, packageid, maxsessioncount, maxsessiontime, "
                        + "type, active, comments, dt FROM PORTAL_subscribers WHERE username = ?",
                new Object[] {username},
                new RowMapper<Subscriber>() 
                {
                    @Override
                    public Subscriber mapRow(ResultSet rs, int rowNum) throws SQLException
                    {
                        Subscriber subscriber = new Subscriber(rs.getString("username"), rs.getInt("packageid"), rs.getInt("maxsessioncount"), 
                                rs.getInt("maxsessiontime"), iplist, rs.getInt("type"), rs.getString("comments"), rs.getInt("active"), rs.getString("dt"));
                        return subscriber;
                    }
                });
            return subscriber;
        } catch (DataAccessException e) {    
            return null;
        }
    }
    
    @Override
    public Subscriber getSubscriberByIp(String ip)
    {
        try {
            String username = (String)this.jdbcTemplate.queryForObject(
                "SELECT username FROM PORTAL_subscriberip WHERE ip = ?",
                new Object[] {ip}, String.class);
            return this.getSubscriber(username);
        } catch (DataAccessException e) {    
            return null;
        }            
    }

    @Override
    public boolean isConnected(String username)
    {
        try {
            return this.getSusbcriberIpCount(username) > 0;
        } catch (DataAccessException e) {    
            return false;
        }
    }
    
    @Override
    public boolean isConnected(String username, String ip)
    {
        try {
            Integer ipCount = this.jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM PORTAL_subscriberip WHERE username = ? AND ip = ?", new Object[] {username, ip}, Integer.class);
            return ipCount > 0; 
        } catch (DataAccessException e) {    
            return false;
        }            
    }
    
    @Override
    public List<String> getExpiredSubscribersIp()
    {
        try {
            List<String> iplist = this.jdbcTemplate.query(
                "SELECT ip FROM PORTAL_subscriberip WHERE CURRENT_TIMESTAMP()  > DATEADD('mi', sessiontime, dt)  AND sessiontime > 0",
                new RowMapper<String>() 
                {
                    @Override
                    public String mapRow(ResultSet rs, int rowNum) throws SQLException
                    {
                        String ip = rs.getString("ip");
                        return ip;
                    }
                });
            return iplist;        
        } catch (DataAccessException e) {    
            return null;
        }       
    }

    @Override
    public void addConnectedLoggedSubscriber(String username, String ip, Integer package_id)
    {
        try {
        this.jdbcTemplate.update("INSERT INTO PORTAL_logged (id, username, ip, dt, package, ddt, reason) values (NULL, ?, ?, CURRENT_TIMESTAMP(), ?, '0000-00-00 00:00:00', 0)", 
                new Object[] {username, ip, package_id});
        }  catch (DataAccessException e) {
        }            
    }
    
    @Override
    public void addDisconnectedLoggedSubscriber(String username, Integer reason)
    {
        try {
        this.jdbcTemplate.update("UPDATE PORTAL_logged SET ddt = CURRENT_TIMESTAMP(), reason = ? WHERE username = ? AND ddt = '0000-00-00 00:00:00'", 
                new Object[] {reason, username});
        }  catch (DataAccessException e) {
        }             
    }
    
    @Override
    public void addDisconnectedLoggedSubscriber(String username, String ip, Integer reason)
    {
        try {
        this.jdbcTemplate.update("UPDATE PORTAL_logged SET ddt = CURRENT_TIMESTAMP(), reason = ? WHERE username = ? AND ip = ? AND ddt = '0000-00-00 00:00:00'", 
                new Object[] {reason, username, ip});
        }  catch (DataAccessException e) {
        }            
    }
    
    @Override
    public void addDisconnectedLoggedSubscriberIp(String ip, Integer reason)
    {
        try {
        this.jdbcTemplate.update("UPDATE PORTAL_logged SET ddt = CURRENT_TIMESTAMP(), reason = ? WHERE ip = ? AND ddt = '0000-00-00 00:00:00'", 
                new Object[] {reason, ip});
        }  catch (DataAccessException e) {
        }            
    }            
    
    @Override
    public List<SubscriberLoggedRecord> getLoggedSubscriberRecords(String username)
    {
        return this.getLoggedSubscriberRecords(username, 5);
    }
    
    @Override
    public List<SubscriberLoggedRecord> getLoggedSubscriberRecords(String username, int count)
    {
        try {
            List<SubscriberLoggedRecord> slr = this.jdbcTemplate.query(
                "SELECT username, ip, dt, package, ddt, reason FROM PORTAL_logged WHERE username = ? ORDER BY dt DESC LIMIT ?",
                new Object[] {username, count},
                new RowMapper<SubscriberLoggedRecord>() 
                {
                    @Override
                    public SubscriberLoggedRecord mapRow(ResultSet rs, int rowNum) throws SQLException
                    {
                        String ddt; //временно убираем значение null, однако оно может быть полезным
                        if(rs.getString("ddt") == null)
                            ddt = "0000-00-00 00:00:00";
                        else
                            ddt = rs.getString("ddt");
                        SubscriberLoggedRecord record = new SubscriberLoggedRecord(rs.getString("username"), rs.getString("ip"), rs.getString("dt"),
                                                                                rs.getInt("package"), ddt, rs.getInt("reason"));
                        return record;
                    }
                });
            return slr;
        } catch (DataAccessException e) {
            return null;
        }       
    }
    
    @Override
    public List<SubscriberLoggedRecord> getLastLoginSubscribersRecords(int count)
    {
        try {
            List<SubscriberLoggedRecord> slr = this.jdbcTemplate.query(
                "SELECT username, ip, dt, package, ddt, reason FROM PORTAL_logged ORDER BY dt DESC LIMIT ?",
                new Object[] {count},
                new RowMapper<SubscriberLoggedRecord>() 
                {
                    @Override
                    public SubscriberLoggedRecord mapRow(ResultSet rs, int rowNum) throws SQLException
                    {
                        String ddt; //временно убираем значение null, однако оно может быть полезным
                        if(rs.getString("ddt") == null)
                            ddt = "0000-00-00 00:00:00";
                        else
                            ddt = rs.getString("ddt");
                        SubscriberLoggedRecord record = new SubscriberLoggedRecord(rs.getString("username"), rs.getString("ip"), rs.getString("dt"),
                                                                                rs.getInt("package"), ddt, rs.getInt("reason"));
                        return record;
                    }
                });
            return slr;
        } catch (DataAccessException e) {
            return null;
        }          
    }
    
    @Override
    public List<SubscriberLoggedRecord> getLastLogoutSubscribersRecords(int count)
    {
        try {
            List<SubscriberLoggedRecord> slr = this.jdbcTemplate.query(
                "SELECT username, ip, dt, package, ddt, reason FROM PORTAL_logged ORDER BY ddt DESC LIMIT ?",
                new Object[] {count},
                new RowMapper<SubscriberLoggedRecord>() 
                {
                    @Override
                    public SubscriberLoggedRecord mapRow(ResultSet rs, int rowNum) throws SQLException
                    {
                        String ddt; //временно убираем значение null, однако оно может быть полезным
                        if(rs.getString("ddt") == null)
                            ddt = "0000-00-00 00:00:00";
                        else
                            ddt = rs.getString("ddt");
                        SubscriberLoggedRecord record = new SubscriberLoggedRecord(rs.getString("username"), rs.getString("ip"), rs.getString("dt"),
                                                                                rs.getInt("package"), ddt, rs.getInt("reason"));
                        return record;
                    }
                });
            return slr;
        } catch (DataAccessException e) {
            return null;
        }          
    }
}
