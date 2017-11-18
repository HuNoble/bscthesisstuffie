import java.util.HashSet;
import java.util.Iterator;


public class UseCaseDetector {
	//private Thread t;
	public Applications apps = new Applications();
	private HashSet<AppDetectorValueTable> appvalue = new HashSet<AppDetectorValueTable>();
	long start_time=System.currentTimeMillis();
	
	public UseCaseDetector(){}
	/*
	public UseCaseDetector(HashSet<AppDetectorValueTable> tempappdetvalue){
		//appvalue=tempappdetvalue;
	}*/
	
	public void UCDetection(HashSet<AppDetectorValueTable> tempappdetvalue){
		for(Iterator<AppDetectorValueTable> i=tempappdetvalue.iterator();i.hasNext();){
			AppDetectorValueTable temp= i.next();
			if(temp.app_id==0){
				temp.uc_id=0;
				temp.kpi_speed_ref1=temp.data_a_to_b+temp.data_b_to_a;
				temp.kpi_speed_ref2=temp.data_a_to_b+temp.data_b_to_a;
				temp.tcp=1000;
				appvalue.add(temp);
			}
			//System.out.println(temp.a_address);
		}
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
