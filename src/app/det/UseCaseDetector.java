package app.det;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Iterator;


public class UseCaseDetector {
	private Applications apps = new Applications();
	private HashSet<AppDetectorValueTable> appvalue = new HashSet<AppDetectorValueTable>();
	private boolean tcp;
	private int tcp_number;
	private int tcp_id;
	private boolean spot=false;
	private int spot_tcp=0;
	
	public HashSet<AppDetectorValueTable> UCDetection(HashSet<AppDetectorValueTable> tempappdetvalue) throws UnknownHostException{
		for(Iterator<AppDetectorValueTable> i=tempappdetvalue.iterator();i.hasNext();){
			AppDetectorValueTable temp= i.next();
			if(temp.app_id==0){
				temp.uc_id=0;
				temp.kpi_speed_ref1=temp.data_a_to_b+temp.data_b_to_a;
				temp.kpi_speed_ref2=temp.data_a_to_b+temp.data_b_to_a;
				temp.tcp=1000;
				appvalue.add(temp);
			}
			if(temp.app_id==1){
				temp.uc_id=0;
				temp.kpi_speed_ref1=temp.data_a_to_b+temp.data_b_to_a;
				temp.kpi_speed_ref2=temp.data_a_to_b+temp.data_b_to_a;
				temp.tcp=1000;
				Wordpress(temp);
			}/*
			if(temp.app_id==2){
				if(!spot){
					temp.uc_id=1;
					temp.kpi_speed_ref1=apps.getApp(2).getUcs(1).getKpiSpeedRef1();
					temp.kpi_speed_ref2=apps.getApp(2).getUcs(1).getKpiSpeedRef2();
					temp.tcp=apps.getApp(2).getUcs(1).getTcp();
					spot_tcp=temp.tcp;
					spot=true;
					}
				else{
					if(spot_tcp!=0){
						temp.uc_id=1;
						temp.kpi_speed_ref1=apps.getApp(2).getUcs(1).getKpiSpeedRef1();
						temp.kpi_speed_ref2=apps.getApp(2).getUcs(1).getKpiSpeedRef2();
						temp.tcp=apps.getApp(2).getUcs(1).getTcp();
						temp.tcp=spot_tcp;
						spot_tcp--;
					}else{
						temp.uc_id=0;
						temp.tcp=1000;
						temp.kpi_speed_ref1=temp.data_a_to_b+temp.data_b_to_a;
						temp.kpi_speed_ref2=temp.data_a_to_b+temp.data_b_to_a;
					}
				}
			}*/
		}
		return appvalue;
	}
	
	public void Wordpress(AppDetectorValueTable temp) throws UnknownHostException{
		if(tcp&&tcp_number>0){
			WordpressPastTCPDetector(temp);
		}
		if(temp.data_b_to_a>20000){
			if(temp.data_b_to_a<temp.data_a_to_b){
				WordpressUseCase2(temp);
			}
			else {
				boolean Amazon=false;
				boolean Softlayer=false;
				String a_iprange1="184.72.0.0"; //amazon: helpshift
				String a_iprange2="184.73.255.255"; // 184.169.128.0; 184.169.255.254, 50.18.0.0, 50.18.255.255
				String a2_iprange1="184.169.128.0";
				String a2_iprange2="184.169.255.254";
				String a3_iprange1="50.18.0.0";
				String a3_iprange2="50.18.255.255";
				String s_iprange1="159.122.19.0"; //mixplayer: softlayer
				String s_iprange2="159.122.19.255";

				for(Iterator<AppDetectorValueTable> i=appvalue.iterator();i.hasNext();){
					AppDetectorValueTable dtmp=i.next();
					if(dtmp.time_last_seen.getTime()>=temp.time_created.getTime()-3000)
					{
						if(ipRange(dtmp.b_address, a_iprange1, a_iprange2)||ipRange(dtmp.b_address, a2_iprange1, a2_iprange2)||ipRange(dtmp.b_address, a3_iprange1, a3_iprange2))
						{
							Amazon=true;
						}
						if(ipRange(dtmp.b_address, s_iprange1, s_iprange2)){
							Softlayer=true;
						}

					}
				}
				if(Softlayer||Amazon){
					WordpressUseCase1(temp);
				}
			}
		}
		appvalue.add(temp);
	}
	
	public long ipSize(String ipstring) throws UnknownHostException{
		
		InetAddress i=InetAddress.getByName(ipstring);
		byte[] octets=i.getAddress();
		long result =0;
		for(byte octet:octets){
			result<<=8;
			result |= octet & 0xff;
		}
		return result;

	}
	
	public boolean ipRange(String ipstring, String iprange1, String iprange2) throws UnknownHostException{
		 if(ipSize(ipstring)>=ipSize(iprange1)&&ipSize(ipstring)<=ipSize(iprange2))
			 return true;
		 else return false;
	}
	
	private void WordpressPastTCPDetector(AppDetectorValueTable temp){
		temp.uc_id=tcp_id;
		temp.kpi_speed_ref1=apps.getApp(temp.app_id).getUcs(tcp_id).getKpiSpeedRef1();
		temp.kpi_speed_ref2=apps.getApp(temp.app_id).getUcs(tcp_id).getKpiSpeedRef2();
		temp.tcp=tcp_number;
		tcp_number--;
		if(tcp_number==0)
			tcp=false;
	}
	
	private void WordpressUseCase2(AppDetectorValueTable temp){
		temp.uc_id=2;
		temp.kpi_speed_ref1=apps.getApp(temp.app_id).getUcs(2).getKpiSpeedRef1();
		temp.kpi_speed_ref2=apps.getApp(temp.app_id).getUcs(2).getKpiSpeedRef2();
		temp.tcp=apps.getApp(temp.app_id).getUcs(2).getTcp();
		tcp_number=temp.tcp;
		tcp_id=2;
		tcp=true;
	}
	
	private void WordpressUseCase1(AppDetectorValueTable temp){
		temp.uc_id=1;
		temp.kpi_speed_ref1=apps.getApp(temp.app_id).getUcs(1).getKpiSpeedRef1();
		temp.kpi_speed_ref2=apps.getApp(temp.app_id).getUcs(1).getKpiSpeedRef2();
		temp.tcp=apps.getApp(temp.app_id).getUcs(1).getTcp();
		tcp_number=temp.tcp;
		tcp_id=1;
		tcp=true;
	}
	
	public boolean getTCP(){
		return tcp;
	}
	
	public void setTCP(boolean b){
		tcp=b;
	}
	
	public int getTCPnumb(){
		return tcp_number;
	}
	
	public void setTCPnumb(int b){
		tcp_number=b;
	}
	
	public int getHashSetSize(){
		return appvalue.size();
	}
	
	public HashSet<AppDetectorValueTable> getHashSet(){
		return appvalue;
	}
	
	public int getTCPId(){
		return tcp_id;
	}
	
	public void setTCPid(int i){
		tcp_id=i;
	}
}
