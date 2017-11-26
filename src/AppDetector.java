
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
    private HashSet<TempTCPTable> tempvalue = new HashSet<TempTCPTable>();
    private HashSet<AppDetectorValueTable> tempappdetvalue = new HashSet<AppDetectorValueTable>();
    //private HashSet<AppDetectorValueTable> appdetvalue = new HashSet<AppDetectorValueTable>();
    private boolean def=true; //default: true
    private UseCaseDetector ucd = new UseCaseDetector();

    public AppDetector(SSLDetector s, DNSDetector d, TCPDetector t) {
        ssldetector = s;
        dnsdetector = d;
        tcpdetector = t;
    }

    @Override
    public void run() {
        while (true) {
        	dnsdetector.dnslock.readLock().lock();
        	for (Map<String,String> temp : dnsdetector.DNSList) {
        		def=true;
        		//System.out.println(apps.getApps(0).getDns());
        		for(int i=0;i<apps.getAppSize();i++){
        			if(temp.get("domain").contains(apps.getApps(i).getName())){
        				tempvalue.add(new TempTCPTable(temp.get("address"), i));
        				//System.out.println("siekrültvalamitazonosítani");
        				def=false;
        			}
        		}
        		if(def==true){
        			tempvalue.add(new TempTCPTable(temp.get("address"), 0));
        		}
			}
        	dnsdetector.dnslock.readLock().unlock();
        	ucdet(tempvalue);
        	ucd.UCDetection(tempappdetvalue);
        	tempvalue.clear(); //ne tolja át az összes adatot újra és újra?
        	tempappdetvalue.clear();
        }
    }
    
	public void ucdet(HashSet<TempTCPTable> ttt){
		int tcpnumber = 1;
		tcpdetector.tcplock.readLock().lock();
		for(Map<String,Object> temp : tcpdetector.TCPList){
			for(Iterator<TempTCPTable> i=ttt.iterator();i.hasNext();){
				TempTCPTable tmp = i.next();
				System.out.println(temp.get("b_address"));
				if(temp.get("b_address").equals(tmp.address)){
					AppDetectorValueTable advt = new AppDetectorValueTable((String)temp.get("a_address"), (String)temp.get("b_address"),tmp.ID, (Timestamp)temp.get("time_created"), (Timestamp)temp.get("time_last_seen"), (int)temp.get("data_a_to_b"), (int)temp.get("data_b_to_a"), tcpnumber);
					tcpnumber++;
					//System.out.println();
					tempappdetvalue.add(advt);
				}
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
