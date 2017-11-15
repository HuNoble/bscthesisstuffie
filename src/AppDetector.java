
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;

public class AppDetector implements Runnable {
    private Thread t;
    private SSLDetector ssldetector;
    private DNSDetector dnsdetector;
    private TCPDetector tcpdetector;
    static Applications apps = new Applications();
    private HashSet<AppDetectorValueTable> appvalue = new HashSet<AppDetectorValueTable>();

    public AppDetector(SSLDetector s, DNSDetector d, TCPDetector t) {
        ssldetector = s;
        dnsdetector = d;
        tcpdetector = t;
    }

    @Override
    public void run() {
    	long start_time=System.currentTimeMillis();
        while (true) {
        	int i=0;
        	//System.out.println("dns");
			for (Map<String,String> temp : dnsdetector.DNSList) {
				if(temp.get("domain").contains(apps.getApps(i).getDns()));
				i++;
			   System.out.println(temp.get("domain") + " " + temp.get("address") + " "+apps.getApps(i).getDns());
			}
			//System.out.println("ssl");
			for (Map<String,String> temp : ssldetector.SSLList) {
			    //System.out.println(temp.get("domain") + " " + temp.get("address"));
			}
            if(System.currentTimeMillis()-start_time==250){
				
			}
			
        }
    }

    public void start () {
        if (t == null) {
            t = new Thread(this);
            t.start ();
        }
    }
    
    public SSLDetector getSslDetector(){
    	return ssldetector;
    }
    
    public DNSDetector getDnsDetector(){
    	return dnsdetector;
    }
    
    public TCPDetector getTcpDetector(){
    	return tcpdetector;
    }
}
