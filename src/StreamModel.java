import java.util.Map;

public class StreamModel {
    public String id;
    public String source_address;
    public String dest_address;
    public String source_port;
    public String dest_port;
    public String timestamp;
    public TCPModel tcp;
    public Map<String,String>[] dns;
    public Map<String,String> ssl;
}
