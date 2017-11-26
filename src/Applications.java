

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

import org.json.*;

public class Applications {
	private ArrayList<App> apps = new ArrayList<App>();
	
	public Applications(){  
		String jstring = readToString("app.txt");
		try {
			JSONObject obj = new JSONObject(jstring);
			JSONArray jar = obj.getJSONArray("apps");
			for(int i=0;i<jar.length();i++){
				apps.add(new App(jar.getJSONObject(i)));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private static String readToString(String filePath)
	{
	    StringBuilder contentBuilder = new StringBuilder();
	    try (Stream<String> stream = Files.lines( Paths.get(filePath), StandardCharsets.UTF_8))
	    {
	        stream.forEach(s -> contentBuilder.append(s).append("\n"));
	    }
	    catch (IOException e)
	    {
	        e.printStackTrace();
	    }
	    return contentBuilder.toString();
	}

	public App getApps(int i){
		return apps.get(i);
	}
	
	public int getAppSize(){
		return apps.size();
	}
	
	public App getApp(int ID){
		//kap egy nevet és az alapján visszaad egy komplett appot
		for(int i=0;i<apps.size();i++){
			if(apps.get(i).getAppId()==ID)
				return apps.get(i);
		}
		return null;
	}
}
