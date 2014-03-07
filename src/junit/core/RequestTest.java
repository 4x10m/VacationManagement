package junit.core;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import core.CDS;
import core.Employe;
import core.HR;
import core.Request;
import core.enums.RequestType;
import core.exceptions.RequestBegginDateBeforeEndDateException;
import core.exceptions.RequestBegginDateBeforeTodayException;
import core.exceptions.RequestDateIntervalDurationException;

public class RequestTest {
	static HR hr;
	static CDS cds;
	static Employe employe;
	
	static Date oneweekago, yesterday, today, tomorrow, inoneweek;
	
	Request request;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Calendar calendar = Calendar.getInstance(); //today
		
		hr = new HR();
		cds = new CDS();
		
		employe = new Employe(cds, hr);
		
		today = calendar.getTime();
		
		calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) - 1);
		yesterday = calendar.getTime();
		
		calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) - 6);
		oneweekago = calendar.getTime();
		
		calendar.setTime(today);
		
		calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1); 
		tomorrow = calendar.getTime();
		
		calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 6);
		inoneweek = calendar.getTime();
	}
	
	@Before
	public void setUp() throws Exception {
		
	}
	
	@Test
	public void testInvalidRequestBeginDateBeforeToday() throws RequestBegginDateBeforeEndDateException, RequestDateIntervalDurationException {
		try {
			request = new Request(employe, RequestType.PAID_HOLLIDAYS, yesterday, inoneweek);
			
			fail("Request begin time cannot be before today. Method would have throw an exception.");
		} catch (RequestBegginDateBeforeTodayException e) { }
	}
	
	@Test
	public void testValidRequest() throws RequestBegginDateBeforeTodayException, RequestBegginDateBeforeEndDateException, RequestDateIntervalDurationException {
		request = new Request(employe, RequestType.PAID_HOLLIDAYS, tomorrow, inoneweek);
	}

	@Test
	public void testGetOwner() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetType() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetBeggindate() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetEnddate() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsCheckCDS() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsCheckHR() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetMotif() {
		fail("Not yet implemented");
	}

	@Test
	public void testCheckCDS() {
		fail("Not yet implemented");
	}

	@Test
	public void testCheckHR() {
		fail("Not yet implemented");
	}

	@Test
	public void testRefuseHR() {
		fail("Not yet implemented");
	}

	@Test
	public void testRefuseCDS() {
		fail("Not yet implemented");
	}


}
