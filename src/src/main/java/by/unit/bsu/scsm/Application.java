package by.unit.bsu.scsm;

import java.util.logging.Logger;
import java.util.logging.Level;

import org.apache.tomcat.jdbc.pool.DataSource;

import org.springframework.boot.SpringApplication;
import org.springframework.core.env.Environment;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.PropertySource;
//import org.springframework.scheduling.annotation.EnableScheduling;

import by.unit.bsu.scsm.domain.SubscriberJdbcDAO;
import by.unit.bsu.scsm.domain.CollectionManager;
import by.unit.bsu.scsm.service.SceService;
import by.unit.bsu.scsm.service.SubscriberService;

@SpringBootApplication
//@EnableScheduling
@PropertySource("classpath:jdbc.properties")
public class Application {

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(Application.class, args);

        Environment env = ctx.getEnvironment();
        
        DataSource ds = new DataSource();
        ds.setDriverClassName(env.getProperty("cm.driverClassName"));
        ds.setUrl(env.getProperty("cm.url"));
        ds.setUsername(env.getProperty("cm.username"));
        ds.setPassword(env.getProperty("cm.password"));   
        
        ds.setJmxEnabled(true);
        ds.setInitialSize(5);
        ds.setMaxActive(50);
        ds.setMinIdle(5);
        ds.setMaxIdle(25);
        ds.setMaxWait(10000);
        
        ds.setValidationQuery("SELECT 1");
        ds.setValidationQueryTimeout(3);
        ds.setValidationInterval(15000);
        ds.setTestOnBorrow(true);
        ds.setTestWhileIdle(true);
        
        //start sce
        SceService ss = (SceService) ctx.getBean(by.unit.bsu.scsm.service.SceService.class);
        try {
            ss.init(env.getProperty("sce.ip"));
        } catch (Exception ex) {
            Logger.getLogger(Application.class.getName()).log(Level.SEVERE, null, ex);
            //System.out.println("SCE connect exception");
        }        
        
        //INIT JDBC
        SubscriberJdbcDAO sjd = (SubscriberJdbcDAO) ctx.getBean(by.unit.bsu.scsm.domain.SubscriberJdbcDAO.class);
        sjd.init(ds);
        
        //INIT CM
        CollectionManager cm = (CollectionManager) ctx.getBean(by.unit.bsu.scsm.domain.CollectionManager.class);
        cm.init(ds);
        
        SubscriberService subs = (SubscriberService) ctx.getBean(by.unit.bsu.scsm.service.SubscriberService.class);
        ss.registerLogoutListener(subs); //for clear autologout after timeout
        //subs.reconnectAll(); //relogging disable now for autologout
        
        //System.out.println("Started.");
    }
}