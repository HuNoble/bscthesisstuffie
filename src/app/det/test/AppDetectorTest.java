package app.det.test;

import static org.junit.Assert.*;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import app.det.AppDetector;
import app.det.Applications;
import app.det.DNSDetector;
import app.det.SSLDetector;
import app.det.TCPDetector;
import app.det.TempTCPTable;
import junit.framework.Assert;


public class AppDetectorTest {
	AppDetector apdet;
	ReadWriteLock ssllock;
	ReadWriteLock dnslock;
	ReadWriteLock tcplock;
	SSLDetector ssldetector;
	DNSDetector dnsdetector;
	TCPDetector tcpdetector;
	@Before
	public void setUp() throws Exception{
		 ssllock = new ReentrantReadWriteLock();
	     dnslock = new ReentrantReadWriteLock();
	     tcplock = new ReentrantReadWriteLock();
	     ssldetector = new SSLDetector(ssllock);
	     dnsdetector = new DNSDetector(dnslock);
	     tcpdetector = new TCPDetector(tcplock);
	     apdet = new AppDetector(ssldetector, dnsdetector, tcpdetector);
	     
	}

	@Test
	public void test_ucdet_DomainEquals_True() {
		//Arrange
		
		TempTCPTable t= new TempTCPTable("192.0.78.22", 0);
		HashSet<TempTCPTable> thash=new HashSet<TempTCPTable>();
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("a_address", "192.0.78.22");
		map.put("b_address", "192.0.78.22");
		map.put("time_created", new Timestamp(System.currentTimeMillis()));
		map.put("time_last_seen", new Timestamp(System.currentTimeMillis()));
		map.put("data_a_to_b", 40);
		map.put("data_b_to_a", 200);
		thash.add(t);
		int size=apdet.getTempSize();
		
		//Act

		apdet.DomainEquals(map, 1, t);
		int size2 =apdet.getTempSize();
		boolean result=false;
		if(size2>size)
			result=true;
		boolean expected =true;
		//Assert
		Assert.assertEquals(expected, result);
	}
	
	@Test
	public void test_ucdet_DomainEquals_False() {
		//Arrange
		
			TempTCPTable t= new TempTCPTable("193.0.78.22", 0);
			HashSet<TempTCPTable> thash=new HashSet<TempTCPTable>();
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("a_address", "192.0.78.22");
			map.put("b_address", "192.0.78.22");
			map.put("time_created", new Timestamp(System.currentTimeMillis()+1));
			map.put("time_last_seen", new Timestamp(System.currentTimeMillis()+10));
			map.put("data_a_to_b", 40);
			map.put("data_b_to_a", 200);
			thash.add(t);
			int size=apdet.getTempSize();
				
				//Act

				apdet.DomainEquals(map, 1, t);
				int size2 =apdet.getTempSize();
				boolean result=false;
		if(size2==size)
				result=true;
		boolean expected =true;
				//Assert
		Assert.assertEquals(expected, result);

	}

	@Test
	public void test_dnsDet_nameEquals_True(){
		Map<String,String> map=new HashMap<String,String>();
		map.put("domain", "wordpress");
		map.put("address", "192.0.78.22");
		Applications app=new Applications();
		//Act

		apdet.nameEquals(map);
		int result=apdet.getTmpValueID();
		//int result = apdet.getTmpValueDomain();
		int expected = 1;
		//Assert
		Assert.assertEquals(expected, result);
	}
	
	@Test
	public void test_dnsDet_nameEquals_False(){
		Map<String,String> map=new HashMap<String,String>();
		map.put("domain", "spotify");
		map.put("address", "192.0.78.22");
		Applications app=new Applications();
		//Act

		apdet.nameEquals(map);
		int result=apdet.getTmpValueID();
		//int result = apdet.getTmpValueDomain();
		int expected = 0;
		//Assert
		Assert.assertEquals(expected, result);
	}
}
