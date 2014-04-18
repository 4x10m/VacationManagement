package core;

import java.util.ArrayList;

import databaseaccess.DatabaseController;

public class HR {
	public static DatabaseController databasecontroller;
	private final int id;
	private final ArrayList<Request> requeststocheck;

	public ArrayList<Request> getRequestToCheck() {
		return this.requeststocheck;
	}

	public void addARequest(final Request request) {
		this.requeststocheck.add(request);
	}

	public HR(final int id) {
		this.id = id;
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

	public int getID() {
		return this.id;
	}
}
