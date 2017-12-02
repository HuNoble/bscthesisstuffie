


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import org.json.simple.JSONObject;

public class KPImodul {
	private Map<String, Object> valueT = new HashMap<>();
	private HashSet<JSONObject> jsonTable = new HashSet<>();
	public KPImodul(){}
	
	public void KPImodulGenerator(HashSet<AppDetectorValueTable> appdetval){
		for(Iterator<AppDetectorValueTable> i = appdetval.iterator();i.hasNext();){
			AppDetectorValueTable tmp=i.next();
			if(tmp.app_id!=0 &&tmp.time_last_seen.getTime()-tmp.time_created.getTime()<=500)
				addElement(tmp);
			else{
				i.remove();
				
			}
		}
		addJSONOutput(jsonTable);
	}
	
	public void addElement(AppDetectorValueTable t){
		//if(t.time_last_seen.getTime()-t.time_created.getTime()<=0) 
		long time=t.time_last_seen.getTime()-t.time_created.getTime();
		double kpi_value;
		if(time<=500)
			kpi_value=(t.data_a_to_b+t.data_b_to_a);
		else kpi_value=(t.data_a_to_b+t.data_b_to_a)/time*500;
		valueT.put("a_address", t.a_address);
		valueT.put("app_id", t.app_id);
		valueT.put("uc_id", t.uc_id);
		valueT.put("kpi_speed_ref1", t.kpi_speed_ref1);
		valueT.put("kpi_speed_ref2", t.kpi_speed_ref2);
		valueT.put("kpi_value", kpi_value);
		JSONObject temp=new JSONObject(valueT);
		jsonTable.add(temp);
	}
	
	public void addJSONOutput(HashSet<JSONObject> appdetval){
		String fname = "output-"+System.currentTimeMillis()+".txt";
		File f = new File(fname);
		JSONObject tmp;
		try {
			FileOutputStream fos = new FileOutputStream(f);
			PrintWriter pw = new PrintWriter(fos);
			if(!f.exists())
				f.createNewFile();
			for(Iterator<JSONObject> i = appdetval.iterator();i.hasNext();){
				tmp=i.next();
				String s = tmp.toJSONString();
				pw.write(s);
			}
			pw.flush();
			pw.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
}
