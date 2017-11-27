package by.unit.bsu.scsm.domain;

public class TRData { //version 1
    private final String time_stamp;
    private final String subscriber_id;
    private final Integer service_id;
    private final Integer protocol_id;
    private final String peer_ip;
    private final Integer peer_port;
    private final String host_string;
    private final String info_string;
    private final String source_ip;
    private final Integer source_port;
    private final Integer down;
    private final Integer up;
    
    public TRData(String time_stamp, String subscriber_id, Integer service_id, Integer protocol_id, 
            String peer_ip, Integer peer_port, String host_string, String info_string, String source_ip, Integer source_port, 
            Integer down, Integer up)
    {
        this.time_stamp = time_stamp;
        this.subscriber_id = subscriber_id;
        this.service_id = service_id;
        this.protocol_id = protocol_id;
        this.peer_ip = peer_ip;
        this.peer_port = peer_port;
        this.host_string = host_string;
        this.info_string = info_string;
        this.source_ip = source_ip;
        this.source_port = source_port;
        this.down = down;
        this.up = up;
    }
    
    public String getTime_stamp()
    {
        return this.time_stamp;
    }
    
    public String getSubscriber_id()
    {
        return this.subscriber_id;
    }
    
    public Integer getService_id()
    {
        return this.service_id;
    }
    
    public Integer getProtocol_id()
    {
        return this.protocol_id;
    }
    
    public String getPeer_ip()
    {
        return this.peer_ip;
    }
    
    public Integer getPeer_port()
    {
        return this.peer_port;
    }
    
    public String getHost_string()
    {
        return this.host_string;
    }
    
    public String getInfo_string()
    {
        return this.info_string;
    }
    
    public String getSource_ip()
    {
        return this.source_ip;
    }
    
    public Integer getSource_port()
    {
        return this.source_port;
    }
    
    public Integer getDown()
    {
        return this.down;
    }
    
    public Integer getUp()
    {
        return this.up;
    }
}
