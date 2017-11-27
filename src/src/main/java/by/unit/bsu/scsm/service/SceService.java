package by.unit.bsu.scsm.service;

import java.util.List;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PreDestroy;

import com.scms.api.sce.prpc.PRPC_SCESubscriberApi;
//import com.scms.api.sce.ConnectionListener;
import com.scms.api.sce.LogoutListener;
import com.scms.common.*;

import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Scope;

@Service
@Scope("singleton")
public class SceService {
    
    private PRPC_SCESubscriberApi api = null;
    
    public SceService() 
    {
        
    }
    
    public void init(String scehost) throws Exception
    {
        if(this.api == null) 
        {
            try {
                this.api = new PRPC_SCESubscriberApi("SceBsuSMApi0.0.1", scehost, 10000);
            } catch (UnknownHostException ex) {
                Logger.getLogger(SceService.class.getName()).log(Level.SEVERE, null, ex);
            }
	}
        this.api.connect();
    }

    @PreDestroy
    public void finish()
    {
        if(this.api != null) 
        {        
            try {
                if(this.api.isConnected())
                {
                    this.api.unregisterLogoutListener();
                    this.api.disconnect();
                }
            } catch (Exception ex) {
                Logger.getLogger(SceService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void loginSubscriber(String username, Integer packageId, String ip, boolean add)
    {
        try {
            this.api.login(username, new NetworkID(ip, NetworkID.TYPE_IP), add, new PolicyProfile(new String[]{"packageId=" + packageId.toString(), "monitor=1"}), null, null);
        } catch (Exception ex) {
            Logger.getLogger(SceService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void loginSubscriber(String username, Integer packageId, List<String> ip, boolean add)
    {
        String[] networkId = (String[]) ip.toArray();
        short[] networkType = new short[ip.size()];
        for(int i=0; i<ip.size(); i++)
        {
            networkType[i] = NetworkID.TYPE_IP;
        }
        try {
            this.api.login(username, new NetworkID(networkId, networkType), add, new PolicyProfile(new String[]{"packageId=" + packageId.toString(), "monitor=1"}), null, null);
        } catch (Exception ex) {
            Logger.getLogger(SceService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void loginSubscriber(String username, Integer packageId, String ip)
    {
        try {
            this.api.login(username, new NetworkID(ip, NetworkID.TYPE_IP), false, new PolicyProfile(new String[]{"packageId=" + packageId.toString(), "monitor=1"}), null, null);
        } catch (Exception ex) {
            Logger.getLogger(SceService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void loginSubscriber(String username, Integer packageId, List<String> ip)
    {
        String[] networkId = (String[]) ip.toArray();
        short[] networkType = new short[ip.size()];
        for(int i=0; i<ip.size(); i++)
        {
            networkType[i] = NetworkID.TYPE_IP;
        }
        try {
            this.api.login(username, new NetworkID(networkId, networkType), false, new PolicyProfile(new String[]{"packageId=" + packageId.toString(), "monitor=1"}), null, null);
        } catch (Exception ex) {
            Logger.getLogger(SceService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
    public void loginSubscriber(String username, String ip)
    {
        try {
            this.api.login(username, new NetworkID(ip, NetworkID.TYPE_IP), true, null, null, null);
        } catch (Exception ex) {
            Logger.getLogger(SceService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    // TODO
    //reconnect used at this time
    /*
    public void loginSubscribersBulk()
    {
        
    }*/
    
    public void changePackage(String username, Integer packageId)
    {
        try {
            this.api.policyProfileUpdate(username, new PolicyProfile(new String[]{"packageId=" + packageId.toString(), "monitor=1"}), null);
        } catch (Exception ex) {
            Logger.getLogger(SceService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void logoutSubscriber(String username)
    {
        try {
            this.api.logout(username, null, null);
        } catch (Exception ex) {
            Logger.getLogger(SceService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void logoutSusbcriber(String username, String ip)
    {
        try {
            this.api.logout(username, new NetworkID(ip, NetworkID.TYPE_IP), null);
        } catch (Exception ex) {
            Logger.getLogger(SceService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void logoutSubscriberIp(String ip)
    {
        try {
            this.api.logout(null, new NetworkID(ip, NetworkID.TYPE_IP), null);
        } catch (Exception ex) {
            Logger.getLogger(SceService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //Listeners
    public void registerLogoutListener(LogoutListener listener)
    {
        try {
            this.api.registerLogoutListener(listener);
        } catch (Exception ex) {
            Logger.getLogger(SceService.class.getName()).log(Level.SEVERE, null, ex);
        }            
    }
}
