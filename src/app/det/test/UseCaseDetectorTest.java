package app.det.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import app.det.AppDetectorValueTable;
import app.det.Applications;
import app.det.UseCaseDetector;
import junit.framework.Assert;
import java.net.UnknownHostException;
import java.sql.Timestamp;


public class UseCaseDetectorTest {

	@Before
	public void set(){
		Applications app = new Applications();
	}
	
	@Test
	public void test_ipSize_validIp_validSize() throws UnknownHostException {
		
		//Arrange
		UseCaseDetector ucd = new UseCaseDetector();
		String ip="100.100.1.1";
		long expected=1684275457;
		//Act
		long result=ucd.ipSize(ip);
		
		//Assert
		Assert.assertEquals(expected, result);
	}
	
	@Test (expected=UnknownHostException.class)
	public void test_ipSize_invalidIp_exception() throws Exception {
		
		//Arrange
		UseCaseDetector ucd = new UseCaseDetector();
		String ip="100.10000.10.10";
		//long expected=1684275457;
		//Act
		ucd.ipSize(ip);
		
		//Assert
		
	}
	
	@Test
	public void test_ipRange_validIps_true() throws UnknownHostException {
		
		//Arrange
		UseCaseDetector ucd = new UseCaseDetector();
		String ip="100.100.15.168";
		String ip_range1="100.100.1.1";
		String ip_range2="100.100.255.255";
		boolean expected=true;
		//Act
		boolean result=ucd.ipRange(ip,ip_range1,ip_range2);
		
		//Assert
		Assert.assertEquals(expected, result);
		//fail("Not yet implemented");
	}
	
	@Test
	public void test_ipRange_validIps_false() throws UnknownHostException {
		
		//Arrange
		UseCaseDetector ucd = new UseCaseDetector();
		String ip="186.100.15.168";
		String ip_range1="100.100.1.1";
		String ip_range2="100.100.255.255";
		boolean expected=false;
		//Act
		boolean result=ucd.ipRange(ip,ip_range1,ip_range2);
		
		//Assert
		Assert.assertEquals(expected, result);
	}

	@Test
	public void test_Wordpress_pastUseCase_WordpressPastTCPDetector() throws UnknownHostException{
		//arrange
		UseCaseDetector ucd = new UseCaseDetector();
		Timestamp datetime=new Timestamp(2017,11,27,9,43,37,0);
		Timestamp datetime2=new Timestamp(2017,11,27,9,43,38,0);
		ucd.setTCPnumb(12);
		ucd.setTCP(true);
		ucd.setTCPid(1);
		AppDetectorValueTable appdetval=new AppDetectorValueTable("184.72.0.0", "159.122.19.0", 1, datetime, datetime2, 200, 200, 13);
		//act
		
		ucd.Wordpress(appdetval);
		boolean result=ucd.getHashSet().contains(appdetval);
		boolean expected=true;
		//assert
		Assert.assertEquals(expected, result);
		
	}
	
	@Test
	public void test_Wordpress_UseCase_WordpressUseCase1() throws UnknownHostException{
		//arrange
		UseCaseDetector ucd = new UseCaseDetector();
		Timestamp datetime=new Timestamp(2017,11,27,9,43,37,0);
		Timestamp datetime2=new Timestamp(2017,11,27,9,43,38,0);

		AppDetectorValueTable appdetval=new AppDetectorValueTable("184.72.0.0", "184.72.2.0", 1, datetime, datetime2, 30, 200, 13);
		AppDetectorValueTable appdetval2=new AppDetectorValueTable("184.72.0.0", "159.122.19.0", 1, datetime, datetime2, 30000, 200, 13);
		ucd.getHashSet().add(appdetval);
		//act
		
		ucd.Wordpress(appdetval2);
		boolean result=ucd.getHashSet().contains(appdetval2);
		boolean expected=true;
		//assert
		Assert.assertEquals(expected, result);
		
	}
	
	@Test
	public void test_Wordpress_UseCase_WordpressUseCase2() throws UnknownHostException{
		//arrange
		UseCaseDetector ucd = new UseCaseDetector();
		Timestamp datetime=new Timestamp(2017,11,27,9,43,37,0);
		Timestamp datetime2=new Timestamp(2017,11,27,9,43,38,0);
		AppDetectorValueTable appdetval=new AppDetectorValueTable("192.12.0.72", "192.12.0.72", 1, datetime, datetime2, 30, 200, 13);
		AppDetectorValueTable appdetval2=new AppDetectorValueTable("192.12.0.72", "192.12.0.72", 1, datetime, datetime2, 30000, 200000, 13);
		ucd.getHashSet().add(appdetval);
		//act
		
		ucd.Wordpress(appdetval2);
		boolean result=ucd.getHashSet().contains(appdetval2);
		boolean expected=true;
		//assert
		Assert.assertEquals(expected, result);
		
	}
	
	@Test
	public void test_Wordpress_DefaultUseCase_Default() throws UnknownHostException {
		//arrange
		UseCaseDetector ucd = new UseCaseDetector();
		Timestamp datetime=new Timestamp(2017,11,27,9,43,37,0);
		Timestamp datetime2=new Timestamp(2017,11,27,9,43,38,0);
		ucd.setTCPnumb(12);
		ucd.setTCP(true);
		ucd.setTCPid(1);
		AppDetectorValueTable appdetval=new AppDetectorValueTable("192.12.0.72", "192.12.0.72", 1, datetime, datetime2, 30, 200, 13);

		//act
		
		ucd.Wordpress(appdetval);
		boolean result=ucd.getHashSet().contains(appdetval);
		boolean expected=true;
		//assert
		Assert.assertEquals(expected, result);
	}
}
