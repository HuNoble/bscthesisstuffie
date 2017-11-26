


import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONObject;

public class KPImodul {
	KPIValueTable kvt=new KPIValueTable();
	private Map<String, Object> valueT = new HashMap<>();
	private HashSet<JSONObject> jsonTable = new HashSet<>();
	public KPImodul(){}
	public void KPImodulGenerator(HashSet<AppDetectorValueTable> appdetval){
		for(Iterator<AppDetectorValueTable> i = appdetval.iterator();i.hasNext();){
			AppDetectorValueTable tmp=i.next();
			System.out.println("történikvmi");
			if(tmp.app_id!=0 &&tmp.time_last_seen.getTime()-tmp.time_created.getTime()<=250 && tmp.tcp_number<tmp.tcp)
				addElement(tmp);
			else{
				i.remove();
			}
		}
		
		
	}
	
	public void addElement(AppDetectorValueTable t){
		double kpi_value=t.data_a_to_b+t.data_b_to_a;
		valueT.put("a_address", t.a_address);
		valueT.put("a_address", t.app_id);
		valueT.put("a_address", t.uc_id);
		valueT.put("a_address", t.kpi_speed_ref1);
		valueT.put("a_address", t.kpi_speed_ref2);
		valueT.put("a_address", kpi_value);
		JSONObject temp=new JSONObject(valueT);
		jsonTable.add(temp);
	}
	
	public void addJSONOutput(HashSet<AppDetectorValueTable>appdetval){
		Map<String, String> jsonoutput = new HashMap<String, String>();
		
	}
}
