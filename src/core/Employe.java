package core;

import java.util.ArrayList;
import java.util.Date;

import core.enums.RequestType;
import core.exceptions.NotEnoughTimeInMeter;
import databaseaccess.DatabaseController;

public class Employe {
	public static DatabaseController databasecontroller;

	private static final long DURATION_OF_A_DAY_IN_MILLISECONDE = 1000l * 60 * 60 * 24;

	private int id;

	private final CDS chefdeservice;
	private final HR humanresource;

	private ArrayList<Request> requests = null;
	private int paidhollidaymeter = 0, reductioninworkinghoursmeter = 0,
			formationmeter = 0;

	public int getID() {
		return this.id;
	}

	public CDS getChefdeservice() {
		return this.chefdeservice;
	}

	public HR getHumanresource() {
		return this.humanresource;
	}

	public ArrayList<Request> getRequests() {
		return this.requests;
	}

	public int getCompteurconges() {
		return this.paidhollidaymeter;
	}

	public int getCompteurrtt() {
		return this.reductioninworkinghoursmeter;
	}

	public int getCompteurformation() {
		return this.formationmeter;
	}

	public void setCompteurconges(final int compteurconges) throws Exception {
		this.paidhollidaymeter = compteurconges;

		if (compteurconges < 0) {
			throw new Exception("Le compteur ne peut pas etre negatif");
		}
	}

	public void setCompteurrtt(final int compteurrtt) throws Exception {
		this.reductioninworkinghoursmeter = compteurrtt;

		if (compteurrtt < 0) {
			throw new Exception("Le compteur ne peut pas etre negatif");
		}
	}

	public void setCompteurformation(final int compteurformation)
			throws Exception {
		this.formationmeter = compteurformation;

		if (compteurformation < 0) {
			throw new Exception("Le compteur ne peut pas etre negatif");
		}
	}

	public Employe(final CDS chefdeservice, final HR humanresource) {
		this.chefdeservice = chefdeservice;
		this.humanresource = humanresource;

		this.requests = new ArrayList<>();
	}

	public Employe(final int id, final CDS chefdeservice, final HR humanresource) {
		this.id = id;
		this.chefdeservice = chefdeservice;
		this.humanresource = humanresource;

		this.requests = new ArrayList<>();

		Employe.databasecontroller.insertANEmploye(this);
	}

	public void doARequest(final Request request) throws NotEnoughTimeInMeter {
		RequestType requesttype = request.getType();

		Date begindate = request.getBeggindate();
		Date enddate = request.getEnddate();
		int requestdurationindays = (int) (Math.abs(begindate.getTime()
				- enddate.getTime()) / Employe.DURATION_OF_A_DAY_IN_MILLISECONDE);

		switch (requesttype) {
		case PAID_HOLLIDAYS:
			if (requestdurationindays > this.paidhollidaymeter) {
				throw new NotEnoughTimeInMeter(requesttype,
						requestdurationindays, this.paidhollidaymeter);
			}
		case FORMATION:
			if (requestdurationindays > this.formationmeter) {
				throw new NotEnoughTimeInMeter(requesttype,
						requestdurationindays, this.paidhollidaymeter);
			}
		case REDUCTION_IN_WORKING_TIME:
			if (requestdurationindays > this.reductioninworkinghoursmeter) {
				throw new NotEnoughTimeInMeter(requesttype,
						requestdurationindays, this.paidhollidaymeter);
			}
		}

		this.requests.add(request);
		this.chefdeservice.addARequest(request);
	}

	public void aRequestJustBeAcceptedByCDSCallback(final Request request) {

	}

	public void aRequestJustBeAcceptedByHRCallBack(final Request request) {

	}
}
