package core.structs;

import java.util.ArrayList;

public class CDS extends User {
	private ArrayList<Request> requeststocheck = null;

	public ArrayList<Request> getRequestToCheck() {
		return this.requeststocheck;
	}

	public void addARequest(final Request request) {
		this.requeststocheck.add(request);
	}

	public CDS(final int id) {
		super(id);
		
		this.requeststocheck = new ArrayList<Request>();
		
		load();
	}

	public void acceptRequest(final Request request) {
		request.checkCDS();
		this.requeststocheck.remove(request);
	}

	public void refuseRequest(final Request request, final String motif) {
		request.refuse(motif);
		this.requeststocheck.remove(request);
	}
}
