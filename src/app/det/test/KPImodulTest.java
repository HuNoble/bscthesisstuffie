package app.det.test;

import static org.junit.Assert.*;

import java.sql.Timestamp;
import java.util.HashSet;

import org.junit.Test;

import app.det.AppDetectorValueTable;
import app.det.KPImodul;
import junit.framework.Assert;

public class KPImodulTest {

	@Test
	public void test_KPImodulGenerator_invalidID_removeElement() {
		//arrange
		KPImodul kpi=new KPImodul();
		HashSet<AppDetectorValueTable> appdet = new HashSet<AppDetectorValueTable>();
		Timestamp datetime=new Timestamp(2017,11,27,9,43,37,0);
		Timestamp datetime2=new Timestamp(2017,11,27,9,43,37,0);
		AppDetectorValueTable appdetval=new AppDetectorValueTable("184.72.0.0", "159.122.19.0", 0, datetime, datetime2, 200, 200, 13);
		appdet.add(appdetval);
		int size=appdet.size();
		//act

		kpi.KPImodulGenerator(appdet);
		int size2=appdet.size();
		boolean result=(size2<size);
		boolean expected=true;
		//assert
		Assert.assertEquals(expected, result);
		
	}
	
	@Test
	public void test_KPImodulGenerator_invalidTime_removeElement() {
		//arrange
		KPImodul kpi=new KPImodul();
		HashSet<AppDetectorValueTable> appdet = new HashSet<AppDetectorValueTable>();
		Timestamp datetime=new Timestamp(2017,11,27,9,43,37,0);
		Timestamp datetime2=new Timestamp(2017,11,27,9,43,38,0);
		AppDetectorValueTable appdetval=new AppDetectorValueTable("184.72.0.0", "159.122.19.0", 0, datetime, datetime2, 200, 200, 13);
		appdet.add(appdetval);
		int size=appdet.size();
		//act

		kpi.KPImodulGenerator(appdet);
		int size2=appdet.size();
		boolean result=(size2<size);
		boolean expected=true;
		//assert
		Assert.assertEquals(expected, result);
		
	}


	@Test
	public void test_KPImodulGenerator_validAppDetVal_addElement(){
		//arrange
		KPImodul kpi=new KPImodul();
		HashSet<AppDetectorValueTable> appdet = new HashSet<AppDetectorValueTable>();
		Timestamp datetime=new Timestamp(2017,11,27,9,43,37,0);
		Timestamp datetime2=new Timestamp(2017,11,27,9,43,37,0);
		AppDetectorValueTable appdetval=new AppDetectorValueTable("184.72.0.0", "159.122.19.0", 1, datetime, datetime2, 200, 200, 13);
		appdet.add(appdetval);
		int size=appdet.size();
		//act

		kpi.KPImodulGenerator(appdet);
		int size2=appdet.size();
		boolean result=(size2==size);
		boolean expected=true;
		//assert
		Assert.assertEquals(expected, result);
		
	}
}
