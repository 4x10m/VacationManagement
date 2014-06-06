package core.structs;

import java.util.ArrayList;

public class HR extends User {
	private ArrayList<Request> requeststocheck;

	public ArrayList<Request> getRequestToCheck() {
		return this.requeststocheck;
	}

	public void addARequest(final Request request) {
		this.requeststocheck.add(request);
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
		request.refuse(motif);
		this.requeststocheck.remove(request);
	}
}
