package core.structs;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import core.database.DatabaseController;
import core.database.DatabaseEntity;
import core.enums.RequestType;
import core.exceptions.RequestBegginDateBeforeEndDateException;
import core.exceptions.RequestBegginDateBeforeTodayException;
import core.exceptions.RequestDateIntervalDurationException;

public class Request extends DatabaseEntity {
	private final static String TABLENAME = "request";

	private static final long DURATION_OF_A_DAY_IN_MILLISECONDE = 1000l * 60 * 60 * 24;

	private Employe owner;
	private RequestType type;
	private Timestamp beggindate, enddate;

	private boolean checkCDS = false, checkHR = false;
	private String motif = null;

	public Employe getOwner() {
		return this.owner;
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

	public boolean isCheckCDS() {
		return this.checkCDS;
	}

	public boolean isCheckHR() {
		return this.checkHR;
	}

	public String getMotif() {
		return this.motif;
	}

	public void setOwner(Employe owner) {
		this.owner = owner;
	}
	public void setType(RequestType type) {
		this.type = type;
	}
	public void setBeggindate(Timestamp beggindate) {
		this.beggindate = beggindate;
	}
	public void setEnddate(Timestamp enddate) {
		this.enddate = enddate;
	}
	public void setCheckCDS(boolean checkCDS) {
		this.checkCDS = checkCDS;
	}
	public void setCheckHR(boolean checkHR) {
		this.checkHR = checkHR;
	}
	public void setMotif(String motif) {
		this.motif = motif;
	}
	
	public void checkCDS() {
		this.checkCDS = true;
		this.getOwner().getHumanresource().addARequest(this);

		save();
	}

	public void checkHR() throws Exception {
		int nbday = (int) (Math.abs(this.beggindate.getTime()
				- this.enddate.getTime()) / Request.DURATION_OF_A_DAY_IN_MILLISECONDE);
		this.checkHR = true;

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

	public void refuseHR(final String motif) {
		this.checkHR = false;
		this.motif = motif;

		save();
	}

	public void refuseCDS(final String motif) {
		this.checkCDS = false;
		this.motif = motif;

		save();
	}

	Request() {
		super(TABLENAME);
	}
	
	private Request(int id) {
		super(TABLENAME, id);
		
		deserialize(DatabaseController.getEntityData(this));
	}
	
	public Request(final Employe owner, final RequestType type,
			final Timestamp beggindate, final Timestamp enddate) throws RequestBegginDateBeforeTodayException,
			RequestBegginDateBeforeEndDateException,
			RequestDateIntervalDurationException {
		super(TABLENAME);
		
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
		if (Math.abs(beggindate.getTime() - enddate.getTime()) < Request.DURATION_OF_A_DAY_IN_MILLISECONDE) {
			throw new RequestDateIntervalDurationException(beggindate, enddate);
		}
		
		this.owner = owner;
		this.type = type;

		this.beggindate = beggindate;
		this.enddate = enddate;
		
		this.checkHR = false;
		this.checkCDS = false;
		
		save();
	}
	
	@Override
	public Map<String, String> serialize() {
		Map<String, String> data = new HashMap<String, String>();
		
		data.put("id", String.valueOf(getID()));
		data.put("requester", String.valueOf(getOwner().getID()));
		data.put("type", getType().toString());
		data.put("begindate", getBeggindate().toString());
		data.put("enddate", getEnddate().toString());
		data.put("checkbycds", String.valueOf(isCheckCDS()));
		data.put("checkbyhr", String.valueOf(isCheckHR()));
		data.put("motif", getMotif());
		
		return data;
	}
	
	@Override
	public void deserialize(Map<String, String> data) {
		setID(Integer.parseInt(data.get("id")));
		
		setOwner(new Employe(Integer.parseInt(data.get("requester"))));
		
		type = RequestType.valueOf(data.get("type"));
		beggindate = Timestamp.valueOf(data.get("begindate"));
		enddate = Timestamp.valueOf(data.get("enddate"));
	}
	
	public static DatabaseEntity getInstance() {
		return new Request();
	}

	public static Request[] selectRequestsNotCheckedByCDS() {
		// TODO Auto-generated method stub
		return null;
	}

	public static Request[] selectRequestsNotCheckedByHR() {
		// TODO Auto-generated method stub
		return null;
	}
}
