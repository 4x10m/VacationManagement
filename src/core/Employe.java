package core;

import java.util.ArrayList;
import java.util.Date;

public class Employe {
	private final CDS chefdeservice;
	private final HR humanresource;
	
	private ArrayList<Request> requests = null;
	private int compteurconges = 0, compteurrtt = 0, compteurformation = 0;
	
	public CDS getChefdeservice() {
		return chefdeservice;
	}
	public HR getHumanresource() {
		return humanresource;
	}
	public ArrayList<Request> getRequests() {
		return requests;
	}
	public int getCompteurconges() {
		return compteurconges;
	}
	public int getCompteurrtt() {
		return compteurrtt;
	}
	public int getCompteurformation() {
		return compteurformation;
	}

	public void setCompteurconges(int compteurconges) throws Exception {
		this.compteurconges = compteurconges;
		
		if(compteurconges < 0) throw new Exception("Le compteur ne peut pas etre negatif");
	}
	public void setCompteurrtt(int compteurrtt) throws Exception {
		this.compteurrtt = compteurrtt;
		
		if(compteurrtt < 0) throw new Exception("Le compteur ne peut pas etre negatif");
	}
	public void setCompteurformation(int compteurformation) throws Exception {
		this.compteurformation = compteurformation;
		
		if(compteurformation < 0) throw new Exception("Le compteur ne peut pas etre negatif");
	}

	public Employe(CDS chefdeservice, HR humanresource) {
		this.chefdeservice = chefdeservice;
		this.humanresource = humanresource;
		
		requests = new ArrayList<>();
	}
	
	public void doARequest(Request request) {
		requests.add(request);
		chefdeservice.addARequest(request);
	}
	
	public void aRequestJustBeAcceptedByCDS() {
		
	}
	public void aRequestJustBeAcceptedByHR() {
		
	}
}
