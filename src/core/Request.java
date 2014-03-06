package core;

import java.util.Date;

public class Request {
	private static final long CONST_DURATION_OF_DAY = 1000l * 60 * 60 * 24;
	
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
		int nbday = (int)(Math.abs(beggindate.getTime() - enddate.getTime()) / CONST_DURATION_OF_DAY);
		checkHR = true;
		
		switch(type) {
		case Conges:
			getOwner().setCompteurconges(getOwner().getCompteurconges() - nbday);
			break;
		case Formation:
			getOwner().setCompteurformation(getOwner().getCompteurformation() - nbday);
			break;
		case RTT:
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
	
	public Request(Employe owner, RequestType type, Date beggindate, Date enddate) {
		this.owner = owner;
		this.type = type;
		
		this.beggindate = beggindate;
		this.enddate = enddate;
	}
}
