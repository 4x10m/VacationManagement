package core;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import core.enums.RequestType;
import core.exceptions.RequestBegginDateBeforeEndDateException;
import core.exceptions.RequestBegginDateBeforeTodayException;
import core.exceptions.RequestDateIntervalDurationException;
import databaseaccess.DatabaseController;

public class Request {
	private final int id;
	public static DatabaseController databasecontroller;

	private static final long DURATION_OF_A_DAY_IN_MILLISECONDE = 1000l * 60 * 60 * 24;

	private final Employe owner;
	private final RequestType type;
	private final Timestamp beggindate, enddate;

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

	public void checkCDS() {
		this.checkCDS = true;
		this.getOwner().getHumanresource().addARequest(this);

		Request.databasecontroller.checkRequestByCDS(this);
	}

	public void checkHR() throws Exception {
		int nbday = (int) (Math.abs(this.beggindate.getTime()
				- this.enddate.getTime()) / Request.DURATION_OF_A_DAY_IN_MILLISECONDE);
		this.checkHR = true;

		switch (this.type) {
		case PAID_HOLLIDAYS:
			this.getOwner().setCompteurconges(
					this.getOwner().getCompteurconges() - nbday);
			break;
		case FORMATION:
			this.getOwner().setCompteurformation(
					this.getOwner().getCompteurformation() - nbday);
			break;
		case REDUCTION_IN_WORKING_TIME:
			this.getOwner().setCompteurrtt(
					this.getOwner().getCompteurrtt() - nbday);
			break;
		}
		Request.databasecontroller.checkRequestByHR(this);
	}

	public void refuseHR(final String motif) {
		this.checkHR = false;
		this.motif = motif;

		Request.databasecontroller.checkRequestByHR(this);
	}

	public void refuseCDS(final String motif) {
		this.checkCDS = false;
		this.motif = motif;

		Request.databasecontroller.checkRequestByCDS(this);
	}

	public Request(final int id, final Employe owner, final RequestType type,
			final Timestamp beggindate, final Timestamp enddate,
			final Boolean checkbycds, final Boolean checkbyHR)
			throws RequestBegginDateBeforeTodayException,
			RequestBegginDateBeforeEndDateException,
			RequestDateIntervalDurationException {
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

		this.id = id;

		Request.databasecontroller.insertARequest(this);

	}

	public int getID() {
		return this.id;
	}
}
