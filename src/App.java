
import java.util.ArrayList;

import org.json.*;


public class App {
	private int appId;
	private String name;
	private String dns;
	private String ipRange1;
	private String ipRange2;
	private ArrayList<Usecase> ucId = new ArrayList<Usecase>();

	public App(){
	}
	
	public App(JSONObject obj){
		try {
			appId=obj.getInt("app_id");
			name=obj.getString("name");
			dns=obj.getString("dns");
			ipRange1=obj.getString("ip_range_from");
			ipRange2=obj.getString("ip_range_to");
			JSONArray arr = obj.getJSONArray("ucs");
			for(int i=0;i<arr.length();i++){
				ucId.add(new Usecase(arr.getJSONObject(i)));
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public int getAppId(){
		return appId;
	}
	
	public String getName(){
		return name;
	}
	
	public String getDns(){
		return dns;
	}
	
	public String getIpRange1(){
		return ipRange1;
	}
	
	public String getIpRange2(){
		return ipRange2;
	}

	public void getUcs(){
		
	}

}
