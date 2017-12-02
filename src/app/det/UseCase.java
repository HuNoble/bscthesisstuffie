package app.det;


import org.json.*;

public class UseCase {
	private int ucId;
	private double data;
	private int tcp;
	private double kpiSpeedRef1;
	private double kpiSpeedRef2;
	
	
	public UseCase(){
		
	}
	public UseCase(JSONObject jobj){
		try {
			ucId=jobj.getInt("uc_id");
			data=jobj.getDouble("data");
			tcp=jobj.getInt("tcp");
			kpiSpeedRef1=jobj.getDouble("kpi_speed_ref1");
			kpiSpeedRef2=jobj.getDouble("kpi_speed_ref2");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public int getUcId(){
		return ucId;
	}
	
	public double getData(){
		return data;
	}
	
	public int getTcp(){
		return tcp;
	}
	
	public double getKpiSpeedRef1(){
		return kpiSpeedRef1;
	}
	
	public double getKpiSpeedRef2(){
		return kpiSpeedRef2;
	}
	
}
