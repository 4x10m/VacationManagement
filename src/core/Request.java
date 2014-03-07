package core;

import java.util.Calendar;
import java.util.Date;

import core.enums.RequestType;
import core.exceptions.RequestBegginDateBeforeEndDateException;
import core.exceptions.RequestBegginDateBeforeTodayException;
import core.exceptions.RequestDateIntervalDurationException;

public class Request {
	private static final long DURATION_OF_A_DAY_IN_MILLISECONDE = 1000l * 60 * 60 * 24;
	
	private final Employe owner;
	private final RequestType type;
	private final Date beggindate, enddate;
	
	private boolean checkCDS = false, checkHR = false;
	private String motif = null;
	
	public Employe getOwner() {
		return owner;
	}
	public RequestType getType() {
		return type;
	}
	public Date getBeggindate() {
		return beggindate;
	}
	public Date getEnddate() {
		return enddate;
	}

	public boolean isCheckCDS() {
		return checkCDS;
	}
	public boolean isCheckHR() {
		return checkHR;
	}
	public String getMotif() {
		return motif;
	}

	public void checkCDS() {
		checkCDS = true;
		getOwner().getHumanresource().addARequest(this);
	}
	public void checkHR() throws Exception {
		int nbday = (int)(Math.abs(beggindate.getTime() - enddate.getTime()) / DURATION_OF_A_DAY_IN_MILLISECONDE);
		checkHR = true;
		
		switch(type) {
		case PAID_HOLLIDAYS:
			getOwner().setCompteurconges(getOwner().getCompteurconges() - nbday);
			break;
		case FORMATION:
			getOwner().setCompteurformation(getOwner().getCompteurformation() - nbday);
			break;
		case REDUCTION_IN_WORKING_TIME:
			getOwner().setCompteurrtt(getOwner().getCompteurrtt() - nbday);
			break;
		}
	}
	
	public void refuseHR(String motif) {
		checkHR = false;
		this.motif = motif;
	}
	public void refuseCDS(String motif) {
		checkCDS = false;
		this.motif = motif;
	}
	
	public Request(Employe owner, RequestType type, Date beggindate, Date enddate) throws RequestBegginDateBeforeTodayException, RequestBegginDateBeforeEndDateException, RequestDateIntervalDurationException {
		Date today = Calendar.getInstance().getTime();
		
		beggindate.setHours(0);
		beggindate.setMinutes(1);
		
		enddate.setHours(0);
		enddate.setMinutes(1);
		
		//check if begin date is after today
		if(beggindate.before(today) || (beggindate.getDate() == today.getDate() && beggindate.getMonth() >= today.getMonth() && beggindate.getYear() >= today.getYear())) {
			throw new RequestBegginDateBeforeTodayException(beggindate);
		}
		
		//check if begin date is before end date
		if(beggindate.after(enddate)) {
			throw new RequestBegginDateBeforeEndDateException(enddate);
		}
		
		//check if interval between begin date and end date is bigger than one day
		if(Math.abs(beggindate.getTime() - enddate.getTime()) < DURATION_OF_A_DAY_IN_MILLISECONDE) {
			throw new RequestDateIntervalDurationException(beggindate, enddate);
		}
		
		this.owner = owner;
		this.type = type;
		
		this.beggindate = beggindate;
		this.enddate = enddate;
	}
}
