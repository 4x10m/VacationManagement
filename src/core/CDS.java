package core;

import java.util.ArrayList;

public class CDS {
	private ArrayList<Request> requeststocheck = null;
	
	public ArrayList<Request> getRequestToCheck() {
		return requeststocheck;
	}
	public void addARequest(Request request) {
		requeststocheck.add(request);
	}
	
	public CDS() {
		requeststocheck = new ArrayList<Request>();
	}
	
	public void acceptRequest(Request request) {
		request.checkCDS();
		requeststocheck.remove(request);
	}
	public void refuseRequest(Request request, String motif) {
		request.refuseCDS(motif);
		requeststocheck.remove(request);
	}
}
