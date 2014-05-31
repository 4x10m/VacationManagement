package core.structs;

import java.util.ArrayList;

import core.database.DatabaseEntity;

public class HR extends User {
	private ArrayList<Request> requeststocheck;

	public ArrayList<Request> getRequestToCheck() {
		return this.requeststocheck;
	}

	public void addARequest(final Request request) {
		this.requeststocheck.add(request);
	}
	
	private HR() {
		super("", "");
	}

	public HR(int id) {
		super(id);
		
		this.requeststocheck = new ArrayList<Request>();
	}

	public void acceptRequest(final Request request) {
		try {
			request.checkHR();
			this.requeststocheck.remove(request);
		} catch (Exception e) {
			this.refuseRequest(request, e.getMessage());
		}
	}

	public void refuseRequest(final Request request, final String motif) {
		request.refuseHR(motif);
		this.requeststocheck.remove(request);
	}

	public static DatabaseEntity getInstance() {
		return new HR();
	}
}
