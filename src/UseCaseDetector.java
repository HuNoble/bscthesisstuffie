import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Iterator;


public class UseCaseDetector {
	//private Thread t;
	public Applications apps = new Applications();
	private KPImodul kpi;
	private HashSet<AppDetectorValueTable> appvalue = new HashSet<AppDetectorValueTable>();
	long start_time=System.currentTimeMillis();
	
	public UseCaseDetector(){}
	
	public void UCDetection(HashSet<AppDetectorValueTable> tempappdetvalue){
		for(Iterator<AppDetectorValueTable> i=tempappdetvalue.iterator();i.hasNext();){
			AppDetectorValueTable temp= i.next();
			if(temp.app_id==0){
				temp.uc_id=0;
				temp.kpi_speed_ref1=temp.data_a_to_b+temp.data_b_to_a;
				temp.kpi_speed_ref2=temp.data_a_to_b+temp.data_b_to_a;
				temp.tcp=1000;
				appvalue.add(temp);
				//System.out.println(temp.app_id);
			}
			if(temp.app_id==1){
				temp.uc_id=0;
				temp.kpi_speed_ref1=temp.data_a_to_b+temp.data_b_to_a;
				temp.kpi_speed_ref2=temp.data_a_to_b+temp.data_b_to_a;
				temp.tcp=1000;
				//System.out.println(temp.data_a_to_b);
				Wordpress(temp);
			}
			
			//System.out.println("kpiiii");
			
			
		}
		kpi.KPImodulGenerator(appvalue);
		appvalue.clear();
	}
	
	public void Wordpress(AppDetectorValueTable temp){
		if(temp.data_a_to_b>30000){
			if(temp.data_b_to_a>temp.data_a_to_b){
				temp.uc_id=2;
				temp.kpi_speed_ref1=apps.getApp(temp.app_id).getUcs(2).getKpiSpeedRef1();
				temp.kpi_speed_ref2=apps.getApp(temp.app_id).getUcs(2).getKpiSpeedRef2();
				temp.tcp=apps.getApp(temp.app_id).getUcs(2).getTcp();
				appvalue.add(temp);
			}
			else {
				boolean Amazon=false;
				boolean Softlayer=false;
				String a_iprange1="50.18.0.0"; //amazon: helpshift
				String a_iprange2="50.18.255.255 "; // 184.169.128.0; 184.169.255.254
				String s_iprange1="159.122.19.0"; //mixplayer: softlayer
				String s_iprange2="159.122.19.255";
				
				for(Iterator<AppDetectorValueTable> i=appvalue.iterator();i.hasNext();){
					AppDetectorValueTable dtmp=i.next();
					if(dtmp.time_last_seen.getTime()>=System.currentTimeMillis()-2000)
					{
						if(ipRange(dtmp.b_address, a_iprange1, a_iprange2)){
							Amazon=true;
						}
						if(ipRange(dtmp.b_address, s_iprange1, s_iprange2)){
							Softlayer=true;
						}
						if(Softlayer || Amazon){
							temp.uc_id=1;
							temp.kpi_speed_ref1=apps.getApp(temp.app_id).getUcs(1).getKpiSpeedRef1();
							temp.kpi_speed_ref2=apps.getApp(temp.app_id).getUcs(1).getKpiSpeedRef2();
							temp.tcp=apps.getApp(temp.app_id).getUcs(1).getTcp();
							appvalue.add(temp);
						}
					}
					
				}
				appvalue.add(temp);
			}
		}
	}
	
	public long ipSize(String ipstring){
		try {
			InetAddress i=InetAddress.getByName(ipstring);
			byte[] octets=i.getAddress();
			long result =0;
			for(byte octet:octets){
				result<<=8;
				result |= octet & 0xff;
			}
			
			return result;
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	
	public boolean ipRange(String ipstring, String iprange1, String iprange2){
		 if(ipSize(ipstring)>=ipSize(iprange1)&&ipSize(ipstring)<=ipSize(iprange2))
			 return true;
		 else return false;
	}
	/*
    public void run() {
    	long start_time=System.currentTimeMillis();
        while (true) {
            if(System.currentTimeMillis()-start_time%250==0){
				KPImodul kpi = new KPImodul(appvalue);
			}
			
        }
    }
	
    public void start () {
        if (t == null) {
            t = new Thread(this);
            t.start ();
        }
    }
    
	*/
}
