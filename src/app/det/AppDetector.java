package app.det;


import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

public class AppDetector implements Runnable {
    private Thread t;
    private SSLDetector ssldetector;
    private DNSDetector dnsdetector;
    private TCPDetector tcpdetector;
    private Applications apps = new Applications();
    private HashSet<TempTCPTable> tempvalue_domain = new HashSet<TempTCPTable>();
    private HashSet<AppDetectorValueTable> tempvalue_app = new HashSet<AppDetectorValueTable>();
    private boolean def=true; //default: true
    private UseCaseDetector ucd = new UseCaseDetector();
    private KPImodul kpi = new KPImodul();
    private long starttime;

    public AppDetector(SSLDetector s, DNSDetector d, TCPDetector t) {
        ssldetector = s;
        dnsdetector = d;
        tcpdetector = t;
        starttime=System.currentTimeMillis();
    }

    @Override
    public void run() {
        while (true) {
        	dnsdetector.dnslock.readLock().lock();
        	for (Map<String,String> temp : dnsdetector.DNSList) {
        		nameEquals(temp);
			}
        	dnsdetector.dnslock.readLock().unlock();
        	ucdet(tempvalue_domain);
        	if(tempvalue_app.size()!=0){
        		try {
					tempvalue_app=ucd.UCDetection(tempvalue_app);
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        		if(System.currentTimeMillis()-starttime>=500){
	        		kpi.KPImodulGenerator(tempvalue_app);
	        		//kpi.addJSONOutput(tempvalue_app);
	        		starttime=System.currentTimeMillis();
	        	}
        	}
        }
    }
    
	public void ucdet(HashSet<TempTCPTable> ttt){
		int tcpnumber = 1;
		tcpdetector.tcplock.readLock().lock();
		for(Map<String,Object> temp : tcpdetector.TCPList){
			for(Iterator<TempTCPTable> i=ttt.iterator();i.hasNext();){
				TempTCPTable tmp = i.next();
				DomainEquals(temp, tcpnumber, tmp);
			}
		}
		tcpdetector.tcplock.readLock().unlock();
	}

	
	public void nameEquals(Map<String,String> temp){
		def=true;
		for(int i=0;i<apps.getAppSize();i++){
			if(temp.get("domain").contains(apps.getApps(i).getName())){
				tempvalue_domain.add(new TempTCPTable(temp.get("address"), i));
				def=false;
			}
		}
		if(def==true){
			tempvalue_domain.add(new TempTCPTable(temp.get("address"), -1));
		}
	}
	
	
    public void start () {
        if (t == null) {
            t = new Thread(this);
            t.start ();
        }
    }
    
    public void DomainEquals(Map<String,Object> temp, int tcpnumber, TempTCPTable tmp){
    	if(temp.get("b_address").equals(tmp.address)){
			AppDetectorValueTable advt = new AppDetectorValueTable((String)temp.get("a_address"), (String)temp.get("b_address"),tmp.ID, (Timestamp)temp.get("time_created"), (Timestamp)temp.get("time_last_seen"), (int)temp.get("data_a_to_b"), (int)temp.get("data_b_to_a"), tcpnumber);
			tempvalue_app.add(advt);
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
    
    public int getTempSize(){
    	return tempvalue_app.size();
    }
    
    public int getTmpValueID(){
    	int j=0;
    	for(Iterator<TempTCPTable> i=tempvalue_domain.iterator();i.hasNext();){
    		TempTCPTable temp= i.next();
    		j=temp.ID;
    	}
    	return j;
    	}
}
