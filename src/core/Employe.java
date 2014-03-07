package core;

import java.util.ArrayList;
import java.util.Date;

import core.enums.RequestType;
import core.exceptions.NotEnoughTimeInMeter;

public class Employe {
	private static final long DURATION_OF_A_DAY_IN_MILLISECONDE = 1000l * 60 * 60 * 24;
	
	private final CDS chefdeservice;
	private final HR humanresource;
	
	private ArrayList<Request> requests = null;
	private int paidhollidaymeter = 0, reductioninworkinghoursmeter = 0, formationmeter = 0;
	
	public CDS getChefdeservice() {
		return chefdeservice;
	}
	public HR getHumanresource() {
		return humanresource;
	}
	public ArrayList<Request> getRequests() {
		return requests;
	}
	public int getCompteurconges() {
		return paidhollidaymeter;
	}
	public int getCompteurrtt() {
		return reductioninworkinghoursmeter;
	}
	public int getCompteurformation() {
		return formationmeter;
	}

	public void setCompteurconges(int compteurconges) throws Exception {
		this.paidhollidaymeter = compteurconges;
		
		if(compteurconges < 0) throw new Exception("Le compteur ne peut pas etre negatif");
	}
	public void setCompteurrtt(int compteurrtt) throws Exception {
		this.reductioninworkinghoursmeter = compteurrtt;
		
		if(compteurrtt < 0) throw new Exception("Le compteur ne peut pas etre negatif");
	}
	public void setCompteurformation(int compteurformation) throws Exception {
		this.formationmeter = compteurformation;
		
		if(compteurformation < 0) throw new Exception("Le compteur ne peut pas etre negatif");
	}

	public Employe(CDS chefdeservice, HR humanresource) {
		this.chefdeservice = chefdeservice;
		this.humanresource = humanresource;
		
		requests = new ArrayList<>();
	}
	
	public void doARequest(Request request) throws NotEnoughTimeInMeter {
		RequestType requesttype = request.getType();
		
		Date begindate = request.getBeggindate();
		Date enddate = request.getEnddate();
		int requestdurationindays = (int)(Math.abs(begindate.getTime() - enddate.getTime()) / DURATION_OF_A_DAY_IN_MILLISECONDE);
		
		switch(requesttype) {
		case PAID_HOLLIDAYS:
			if(requestdurationindays > paidhollidaymeter) {
				throw new NotEnoughTimeInMeter(requesttype, requestdurationindays, paidhollidaymeter);
			}
		case FORMATION:
			if(requestdurationindays > this.formationmeter) {
				throw new NotEnoughTimeInMeter(requesttype, requestdurationindays, paidhollidaymeter);
			}
		case REDUCTION_IN_WORKING_TIME:
			if(requestdurationindays > this.reductioninworkinghoursmeter) {
				throw new NotEnoughTimeInMeter(requesttype, requestdurationindays, paidhollidaymeter);
			}
		}
		
		requests.add(request);
		chefdeservice.addARequest(request);
	}
	
	public void aRequestJustBeAcceptedByCDSCallback(Request request) {
		
	}
	
	public void aRequestJustBeAcceptedByHRCallBack(Request request) {
		
	}
}
