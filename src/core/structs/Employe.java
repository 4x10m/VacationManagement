package core.structs;

import java.util.ArrayList;
import java.util.Map;

public class Employe extends User {
	private static String TABLE_NAME = "employe";

	private int paidhollidaymeter = 0;
	private int reductioninworkinghoursmeter = 0;
	private int	formationmeter = 0;
	
	@Override
	public String getTableName() {
		return TABLE_NAME;
	}

	public ArrayList<Request> getRequests() {
		ArrayList<Request> myRequests = new ArrayList<Request>();
		Request[] requests = Request.loadAllRequest();
		
		for(Request request : requests) {
			if(request.getOwner().getID() == this.getID()) {
				myRequests.add(request);
			}
		}
		
		return myRequests;
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

	public void setHollidaysMeter(final int hollidaymeter) throws Exception {
		if (hollidaymeter < 0) throw new Exception("Le compteur ne peut pas etre negatif");
		
		this.paidhollidaymeter = hollidaymeter;
		
		save();
	}

	public void setRTTMeter(final int rttmeter) throws Exception {
		if (rttmeter < 0) throw new Exception("Le compteur ne peut pas etre negatif");
		
		this.reductioninworkinghoursmeter = rttmeter;
		
		save();
	}

	public void setFormationMeter(final int formationmeter)
			throws Exception {
		if (formationmeter < 0) throw new Exception("Le compteur ne peut pas etre negatif");
		
		this.formationmeter = formationmeter;
		
		save();
	}

	public Employe(int id) {
		super(id);

		load();
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
}
