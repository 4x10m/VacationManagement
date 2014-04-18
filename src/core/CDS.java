package core;

import java.util.ArrayList;

import databaseaccess.DatabaseController;

public class CDS {
	public static DatabaseController databasecontroller;
	private final int id;
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

	public CDS(final int id) {
		this.id = id;
		CDS.databasecontroller = CDS.databasecontroller;
		this.requeststocheck = new ArrayList<Request>();
	}

	public void acceptRequest(final Request request) {
		request.checkCDS();
		this.requeststocheck.remove(request);
	}

	public void refuseRequest(final Request request, final String motif) {
		request.refuseCDS(motif);
		this.requeststocheck.remove(request);
	}
}
