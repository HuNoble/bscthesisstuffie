package app.det;

import java.util.ArrayList;
import org.json.*;


public class App {
	private int appId;
	private String name;
	private String dns;
	private String ipRange1;
	private String ipRange2;
	private ArrayList<UseCase> ucId = new ArrayList<UseCase>();

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
				ucId.add(new UseCase(arr.getJSONObject(i)));
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

	public UseCase getUcs(int ID){
		for(int i=0;i<ucId.size();i++)
			if(ucId.get(i).getUcId()==ID)
				return ucId.get(i);
		return null;
	}

}
