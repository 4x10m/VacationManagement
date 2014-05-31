package core.structs;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import core.database.DatabaseController;
import core.database.DatabaseEntity;
import core.enums.RequestType;
import core.exceptions.NotEnoughTimeInMeter;

public class Employe extends User {
	private static String TABLE_NAME = "employe";
	private static final long DURATION_OF_A_DAY_IN_MILLISECONDE = 1000l * 60 * 60 * 24;

	private CDS chefdeservice;
	private HR humanresource;

	private ArrayList<Request> requests = null;
	private int paidhollidaymeter = 0, reductioninworkinghoursmeter = 0,
			formationmeter = 0;

	public CDS getChefdeservice() {
		return this.chefdeservice;
	}

	public HR getHumanresource() {
		return this.humanresource;
	}
	
	@Override
	public String getTableName() {
		return TABLE_NAME;
	}

	public ArrayList<Request> getRequests() {
		DatabaseEntity[] entitys = DatabaseEntity.loadAll(new Request());
		
		for(DatabaseEntity entity : entitys) {
			if(((Request)entity).getOwner().getID() == this.getID()) {
				requests.add((Request) entity);
			}
		}
		
		return requests;
	}

	public int getHollidaysMeter() {
		return this.paidhollidaymeter;
	}

	public int getRTTMeter() {
		return this.reductioninworkinghoursmeter;
	}

	public int getFormationMeter() {
		return this.formationmeter;
	}

	public void setHollidaysMeter(final int compteurconges) throws Exception {
		this.paidhollidaymeter = compteurconges;

		if (compteurconges < 0) {
			throw new Exception("Le compteur ne peut pas etre negatif");
		}
	}

	public void setRTTMeter(final int compteurrtt) throws Exception {
		this.reductioninworkinghoursmeter = compteurrtt;

		if (compteurrtt < 0) {
			throw new Exception("Le compteur ne peut pas etre negatif");
		}
	}

	public void setFormationMeter(final int compteurformation)
			throws Exception {
		this.formationmeter = compteurformation;

		if (compteurformation < 0) {
			throw new Exception("Le compteur ne peut pas etre negatif");
		}
	}

	private Employe() {
		super("", "");
	}

	public Employe(int id) {
		super("", "");
		
		this.requests = new ArrayList<>();

		deserialize(DatabaseController.getEntityData(getTableName(), id));
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
	
	@Override
	public void deserialize(Map<String, String> data) {
		super.deserialize(data);
		
		try {
			setFormationMeter(Integer.parseInt(data.get("formationmeter")));
			setRTTMeter(Integer.parseInt(data.get("rttmeter")));
			setHollidaysMeter(Integer.parseInt(data.get("hollidaysmeter")));
		} catch (Exception exception) {
			System.out.println(String.format("impossible to load employe %d", getID()));
			
			exception.printStackTrace();
		}
	}
	
	@Override
	public Map<String, String> serialize() {
		Map<String, String> data = super.serialize();
		
		data.put("formationmeter", String.valueOf(getFormationMeter()));
		data.put("hollidaysmeter", String.valueOf(getHollidaysMeter()));
		data.put("rttmeter", String.valueOf(getRTTMeter()));
		
		return data;
	}

	public static DatabaseEntity getInstance() {
		return new Employe();
	}
}
