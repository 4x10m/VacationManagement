package core.structs;

import java.util.ArrayList;
import java.util.Map;

import core.database.DatabaseController;
import core.database.DatabaseEntity;

public class CDS extends User {
	private int id;
	private ArrayList<Request> requeststocheck = null;

	public ArrayList<Request> getRequestToCheck() {
		return this.requeststocheck;
	}

	public void addARequest(final Request request) {
		this.requeststocheck.add(request);
	}

	public int getID() {
		return this.id;
	}
	
	private CDS() {
		super("", "");
	}

	public CDS(final int id) {
		super("", "");
		
		super.setID(id);
		this.requeststocheck = new ArrayList<Request>();
		
		this.deserialize(DatabaseController.getEntityData(super.getTableName(), super.getID()));
	}

	public void acceptRequest(final Request request) {
		request.checkCDS();
		this.requeststocheck.remove(request);
	}

	public void refuseRequest(final Request request, final String motif) {
		request.refuseCDS(motif);
		this.requeststocheck.remove(request);
	}

	public static DatabaseEntity getInstance() {
		return new CDS();
	}
}
