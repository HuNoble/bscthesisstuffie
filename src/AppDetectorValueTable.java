import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class AppDetectorValueTable {
	String a_address; //user id
	String b_address;
	int app_id; //application id
	int uc_id; //use-case id
	Timestamp time_created;
	Timestamp time_last_seen;
	int data_a_to_b;
	int data_b_to_a;
	double kpi_speed_ref1;
	double kpi_speed_ref2;
	int tcp_number;
	int tcp;
	
	public AppDetectorValueTable(){
		
	}
	
	public AppDetectorValueTable(String a, String b, int a_id, Timestamp tc, Timestamp tls, int da, int db, int tnumb){
		time_created=tc;
		time_last_seen=tls;
		a_address=a;
		b_address=b;
		app_id=a_id;
		data_a_to_b=da;
		data_b_to_a=db;
		tcp_number=tnumb;
	}
	
}
