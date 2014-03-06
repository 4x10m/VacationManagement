package core;

import java.util.ArrayList;

public class HR {
	private ArrayList<Request> requeststocheck;
	
	public ArrayList<Request> getRequestToCheck() {
		return requeststocheck;
	}
	public void addARequest(Request request) {
		requeststocheck.add(request);
	}
	
	public HR() {
		requeststocheck = new ArrayList<Request>();
	}
	
	public void acceptRequest(Request request) {
		try {
			request.checkHR();
			requeststocheck.remove(request);
		} catch (Exception e) {
			refuseRequest(request, e.getMessage());
		}
	}
	public void refuseRequest(Request request, String motif) {
		request.refuseHR(motif);
		requeststocheck.remove(request);
	}
}
