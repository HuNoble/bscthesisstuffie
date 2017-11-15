

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;


public class KPImodul {
	KPIValueTable kvt=new KPIValueTable();
	private HashSet<KPIValueTable> valueT = new HashSet<KPIValueTable>();
	
	public KPImodul(HashSet<AppDetectorValueTable> appdetval){
		for(Iterator<AppDetectorValueTable> i = appdetval.iterator();i.hasNext();){
			AppDetectorValueTable tmp=i.next();
			if(tmp.app_id!=0 &&tmp.time_last_seen-tmp.time_created<=250 && tmp.tcp_number<tmp.tcp)
				addElement(tmp);
			else{
				i.remove();
			}
		}
	}
	
	public void addElement(AppDetectorValueTable t){
		kvt.a_address=t.a_address;
		kvt.app_id=t.app_id;
		kvt.uc_id=t.uc_id;
		kvt.kpi_speed_ref1=t.kpi_speed_ref1;
		kvt.kpi_speed_ref2=t.kpi_speed_ref2;
		kvt.kpi_value=t.data_a_to_b+t.data_b_to_a;
		valueT.add(kvt);
	}
}
