package core.structs;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import core.database.DatabaseEntity;
import core.enums.RequestState;
import core.enums.RequestType;
import core.exceptions.NotEnoughTimeInMeter;
import core.exceptions.RequestBegginDateBeforeEndDateException;
import core.exceptions.RequestBegginDateBeforeTodayException;
import core.exceptions.RequestDateIntervalDurationException;

public class Request extends DatabaseEntity {
	///ATTRIBUTES
	private static final long DURATION_OF_A_DAY_IN_MILLISECONDE = 1000l * 60 * 60 * 24;

	public int owner;
	public RequestType type;
	public Timestamp beggindate, enddate;

	public RequestState state;
	public String motif = null;

	
	///GETTERS AND SETTERS
	public Employe getOwner() {
		return new Employe(owner);
	}
	public RequestType getType() {
		return this.type;
	}
	public Timestamp getBeggindate() {
		return this.beggindate;
	}
	public Timestamp getEnddate() {
		return this.enddate;
	}
	public RequestState getState() {
		return this.state;
	}
	public String getMotif() {
		return this.motif;
	}

	private void setOwner(int owner) {
		this.owner = owner;
	}
	private void setType(RequestType type) {
		this.type = type;
	}
	private void setBeggindate(Timestamp beggindate) {
		this.beggindate = beggindate;
	}
	private void setEnddate(Timestamp enddate) {
		this.enddate = enddate;
	}
	private void setState(RequestState state) {
		this.state = state;
	}
	private void setMotif(String motif) {
		this.motif = motif;
	}
	
	
	///CONSTRUCTORS
	private Request() { }
	
	public Request(int id) {
		super(id);
		
		load();
	}
	
	public Request(final Employe owner, final RequestType type,
			final Timestamp beggindate, final Timestamp enddate) throws RequestBegginDateBeforeTodayException,
			RequestBegginDateBeforeEndDateException,
			RequestDateIntervalDurationException, NotEnoughTimeInMeter {
		super();
		
		Date today = Calendar.getInstance().getTime();

		beggindate.setHours(0);
		beggindate.setMinutes(1);

		enddate.setHours(0);
		enddate.setMinutes(1);

		// check if begin date is after today
		if (beggindate.before(today)) {
			throw new RequestBegginDateBeforeTodayException(beggindate);
		}

		// check if begin date is before end date
		if (beggindate.after(enddate)) {
			throw new RequestBegginDateBeforeEndDateException(enddate);
		}

		// check if interval between begin date and end date is bigger than one
		// day
		if (Math.abs(beggindate.getTime() - enddate.getTime()) < Request.DURATION_OF_A_DAY_IN_MILLISECONDE - 10) {
			throw new RequestDateIntervalDurationException(beggindate, enddate);
		}
		
		int requestdurationindays = (int) (Math.abs(beggindate.getTime()
				- enddate.getTime()) / Request.DURATION_OF_A_DAY_IN_MILLISECONDE);

		switch (type) {
		case PAID_HOLLIDAYS:
			if (requestdurationindays > owner.getHollidaysMeter()) {
				throw new NotEnoughTimeInMeter(type,
						requestdurationindays, owner.getHollidaysMeter());
			}
		case FORMATION:
			if (requestdurationindays > owner.getFormationMeter()) {
				throw new NotEnoughTimeInMeter(type,
						requestdurationindays, owner.getFormationMeter());
			}
		case REDUCTION_IN_WORKING_TIME:
			if (requestdurationindays > owner.getRTTMeter()) {
				throw new NotEnoughTimeInMeter(type,
						requestdurationindays, owner.getRTTMeter());
			}
		}
		
		this.setOwner(owner.getID());
		this.setType(type);

		this.setBeggindate(beggindate);
		this.setEnddate(enddate);
		
		this.setState(RequestState.WAITFORCDSVALIDATION);
		
		save();
	}
	
	
	///METHODS
	public void checkCDS() {
		this.state = RequestState.WAITFORHRVALIDATION;
				
		save();
	}

	public void checkHR() throws Exception {
		//calculate request duration in day
		int nbday = (int) (Math.abs(this.beggindate.getTime()
				- this.enddate.getTime()) / Request.DURATION_OF_A_DAY_IN_MILLISECONDE);
		
		setState(RequestState.VALIDATED);

		//sub request duration to appropriate owner meter
		switch (this.type) {
		case PAID_HOLLIDAYS:
			this.getOwner().setHollidaysMeter(this.getOwner().getHollidaysMeter() - nbday);
			break;
		case FORMATION:
			this.getOwner().setFormationMeter(this.getOwner().getFormationMeter() - nbday);
			break;
		case REDUCTION_IN_WORKING_TIME:
			this.getOwner().setRTTMeter(this.getOwner().getRTTMeter() - nbday);
			break;
		}
		
		save();
	}

	public void refuse(final String motif) {
		setState(RequestState.REFUSED);
		setMotif(motif);

		save();
	}
	
	
	///INHERITED METHODS
	@Override
	protected String getTableName() {
		return "request";
	}
	
	@Override
	public Map<String, String> serialize() {
		Map<String, String> data = new HashMap<String, String>();
		
		data.put("requester", String.valueOf(getOwner().getID()));
		data.put("type", getType().toString());
		data.put("begindate", getBeggindate().toString());
		data.put("enddate", getEnddate().toString());
		data.put("state", String.valueOf(this.state));
		data.put("motif", getMotif());
		
		return data;
	}
	
	@Override
	public void deserialize(Map<String, String> data) {
		this.setOwner(Integer.parseInt(data.get("requester")));
		this.setType(RequestType.valueOf(data.get("type")));
		this.setBeggindate(Timestamp.valueOf(data.get("begindate")));
		this.setEnddate(Timestamp.valueOf(data.get("enddate")));
		this.setState(RequestState.valueOf(data.get("state")));
		this.setMotif(data.get("motif"));
	}

	
	///STATIC METHODS
	public static Request[] loadAllRequest() {
		DatabaseEntity[] entitys = DatabaseEntity.loadAll(new Request());
		Request[] requests = new Request[entitys.length];
		
		for(int i = 0; i < entitys.length; i++) requests[i] = (Request) entitys[i];
		
		return requests;
	}
	
	public static Request[] selectRequestsNotCheckedByCDS() {
		ArrayList<Request> requests = new ArrayList<Request>();
		DatabaseEntity[] entitys = loadAll(new Request());
		
		for(DatabaseEntity entity : entitys) {
			Request request = (Request) entity;
			
			if(request.getState() == RequestState.WAITFORCDSVALIDATION) {
				requests.add(request);
			}
		}
		
		return requests.toArray(new Request[0]);
	}

	public static Request[] selectRequestsNotCheckedByHR() {
		ArrayList<Request> requests = new ArrayList<Request>();
		DatabaseEntity[] entitys = loadAll(new Request());
		
		for(DatabaseEntity entity : entitys) {
			Request request = (Request) entity;
			
			if(request.getState() == RequestState.WAITFORHRVALIDATION) {
				requests.add(new Request(entity.getID()));
			}
		}
		
		return requests.toArray(new Request[0]);
	}
}
