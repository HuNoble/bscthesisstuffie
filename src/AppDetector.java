
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;

public class AppDetector implements Runnable {
    private Thread t;
    private SSLDetector ssldetector;
    private DNSDetector dnsdetector;
    private TCPDetector tcpdetector;
    private Applications apps = new Applications();
    private HashSet<TempTCPTable> tempvalue = new HashSet<TempTCPTable>();
    private boolean def=true;

    public AppDetector(SSLDetector s, DNSDetector d, TCPDetector t) {
        ssldetector = s;
        dnsdetector = d;
        tcpdetector = t;
    }

    @Override
    public void run() {
    	long start_time=System.currentTimeMillis();
        while (true) {
        	dnsdetector.dnslock.readLock().lock();
        	for (Map<String,String> temp : dnsdetector.DNSList) {
        		for(int i=0;i<apps.getAppSize();i++){
        			if(temp.get("domain").contains(apps.getApps(i).getDns())){
        				ucdet(temp.get("address"), apps.getApps(i).getAppId());
        				def=false;
        			}
        		}
        		if(def==true){
        			ucdet(temp.get("address"), 0);
        		}
			}
        	dnsdetector.dnslock.readLock().unlock();
            if(System.currentTimeMillis()-start_time%250==0){
				
			}
			
        }
    }
    
	public void ucdet(String address, int ID){
		tcpdetector.tcplock.readLock().lock();
		for(Map<String,Object> temp : tcpdetector.TCPList){
			if(temp.get("a_address").equals(address)){
				TempTCPTable advt = new TempTCPTable();
				tempvalue.add(advt);
			}
		}
		tcpdetector.tcplock.readLock().unlock();
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
