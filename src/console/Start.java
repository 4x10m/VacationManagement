package console;

import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import core.CDS;
import core.Employe;
import core.HR;
import core.Request;
import core.RequestType;

public class Start {
	private static final String requestformatmodel = "%s\t%s\t%s\t%s\t%s\n";
	
	private static CDS chefdeservice = new CDS();
	private static HR humanresource = new HR();
	private static Employe employe = new Employe(chefdeservice, humanresource);
	
	public static void main(String[] args) {
		start();
	}
	
	private static void start() {
		System.out.println(""
				+ "_____________________________________________________________________\n"
				+ "------------------Gestion de congès des employés---------------------\n"
				+ ""
				+ "1. Connexion\n"
				+ "2. A Propos\n"
				+ "3. Quitter\n"
				+ ""
				+ "---------------------------------------------------------------------\n"
				+ "_____________________________________________________________________");

		int choice = InputScanner.getIntBetween(1, 3);
		
		switch (choice) {
		case 1:
			auth();
			break;
		case 2:
			break;
		case 3:
			System.exit(0);
		}
	}
	
	private static void about() {
		//TODO about
		System.out.println(""
				+ "_____________________________________________________________________\n"
				+ "---------------------------------About-------------------------------\n"
				+ ""
				+ "Work on progress"
				+ ""
				+ "---------------------------------------------------------------------\n"
				+ "_____________________________________________________________________");
	}
	
	private static void auth() {
		System.out.println(""
				+ "_____________________________________________________________________\n"
				+ "----------------------------Type d'accés-----------------------------\n"
				+ ""
				+ "1. Employe\n"
				+ "2. Chef de service\n"
				+ "3. Ressource Humaines\n"
				+ "4. Retourné a l'écran d'acceuil\n"
				+ ""
				+ "---------------------------------------------------------------------\n"
				+ "_____________________________________________________________________");
		
		int choice = InputScanner.getIntBetween(1, 4);
		
		switch (choice) {
		case 1:
			menuemploye();
			break;
		case 2:
			menucds();
			break;
		case 3:
			menuhr();
			break;
		case 4:
			start();
			break;
		}
	}
	
	private static void menuemploye() {
		System.out.println(""
				+ "_____________________________________________________________________\n"
				+ "---------------------------Accès employé-----------------------------\n"
				+ ""
				+ "1. Faire une requète de congès\n"
				+ "2. Voir mes requètes de congès\n"
				+ "3. Deconnexion\n"
				+ ""
				+ "---------------------------------------------------------------------\n"
				+ "_____________________________________________________________________");
		
		int choice = InputScanner.getIntBetween(1, 4);
		
		switch (choice) {
		case 1:
			nouvellerequete();
			break;
		case 2:
			voirMesRequetes();
			break;
		case 3:
			auth();
			break;
		}
	}
	
	private static void menucds() {
		System.out.println(""
				+ "_____________________________________________________________________\n"
				+ "------------------------Accès chef de service------------------------\n"
				+ ""
				+ "1. Voir les requètes a accepter\n"
				+ "2. Consulter un employe\n"
				+ "3. Deconnexion\n"
				+ ""
				+ "---------------------------------------------------------------------\n"
				+ "_____________________________________________________________________");
		
		int choice = InputScanner.getIntBetween(1, 3);
		
		switch (choice) {
		case 1:
			voirlesrequetesaacceptercds();
			break;
		case 2:
			consulterunemploye();
			break;
		case 3:
			auth();
			break;
		}
	}
	private static void consulterunemploye() {
		// TODO Auto-generated method stub
		
	}

	private static void voirlesrequetesaacceptercds() {
		ArrayList<Request> requests = null;
		
		requests = chefdeservice.getRequestToCheck();
		
		for(Request request : requests) {
			
		}
	}
	
	private static void afficheunerequetecds(Request request) {
		String convetedrequest = null;
		
		convertedrequest = String.format("%s\t%s\t%s\t, request.getType(), request.)
		
		convertedrequest = String.format(request, args)
	}

	private static void menuhr() {
		System.out.println(""
				+ "_____________________________________________________________________\n"
				+ "---------------------Accès ressources humaines-----------------------\n"
				+ ""
				+ "1. Voir les requètes a accepter\n"
				+ "2. Consulter un employé\n"
				+ "3. Deconnexion\n"
				+ ""
				+ "---------------------------------------------------------------------\n"
				+ "_____________________________________________________________________");
		
		int choice = InputScanner.getIntBetween(1, 3);
		
		switch (choice) {
		case 1:
			voirlesrequetesaaccepterhr();
			break;
		case 2:
			consulterunemploye();
			break;
		case 3:
			auth();
			break;
		}
	}
	
	private static void voirlesrequetesaaccepterhr() {
		// TODO Auto-generated method stub
		
	}

	private static void nouvellerequete() {
		RequestType type;
		Date beggindate, enddate;
		
		type = nouvellerequete1();
		beggindate = nouvellerequete2();
		enddate = nouvellerequete3();
		
		employe.doARequest(new Request(employe, type, beggindate, enddate));
		
		System.out.println(""
				+ "_____________________________________________________________________\n"
				+ "----------------------Nouvelle Requète Enregistrée-------------------\n"
				+ ""
				+ "Votre réquète a bien été enregistrée elle sera soumise a validation\n"
				+ "auprès du chef de service et des ressources humaines.\n\n"
				+ ""
				+ "Appuyez sur entré pour revenir a l'écran a votre ecran de gestion...\n"
				+ ""
				+ "---------------------------------------------------------------------\n"
				+ "_____________________________________________________________________");
		
		InputScanner.getNext();
		
		menuemploye();
	}
	
	private static RequestType nouvellerequete1() {
		System.out.println(""
				+ "_____________________________________________________________________\n"
				+ "------------------------Nouvelle Requète 1/3-------------------------\n"
				+ ""
				+ "Selectionnez le type\n"
				+ ""
				+ "1. Congès\n"
				+ "2. RTT\n"
				+ "3. Formation\n"
				+ ""
				+ "---------------------------------------------------------------------\n"
				+ "_____________________________________________________________________");
		
		int choice = InputScanner.getIntBetween(1, 3);
		
		switch (choice) {
		case 1:
			return RequestType.Conges;
		case 2:
			return RequestType.RTT;
		case 3:
			return RequestType.Formation;
		}
		
		return null;
	}
	
	private static Date nouvellerequete2() {
		System.out.println(""
				+ "_____________________________________________________________________\n"
				+ "------------------------Nouvelle Requète 2/3-------------------------\n"
				+ ""
				+ "Entrez la date de départ (Format: dd/mm/yyyy)\n"
				+ ""
				+ "---------------------------------------------------------------------\n"
				+ "_____________________________________________________________________");
		
		return InputScanner.getDate();
	}
	
	private static Date nouvellerequete3() {
		System.out.println(""
				+ "_____________________________________________________________________\n"
				+ "------------------------Nouvelle Requète 3/3-------------------------\n"
				+ ""
				+ "Entrez la date de fin (Format: dd/mm/yyyy)\n"
				+ ""
				+ "---------------------------------------------------------------------\n"
				+ "_____________________________________________________________________");
		
		return InputScanner.getDate();
	}
	
	private static void voirMesRequetes() {
		int i = 0;
		String model = "%s\t%s\t%s\t%s\t%s\t%s\n";
		String requesttable = " ";
		ArrayList<Request> requests = employe.getRequests();
		
		for(Request request : requests) {
			String checkCDS = "", checkHR = "", motif = "";
			
			if(request.isCheckCDS()) checkCDS = "X";
			else {
				checkCDS = " ";
				motif = request.getMotif();
			}
			
			if(request.isCheckHR()) checkHR = "X";
			else {
				checkHR = " ";
				motif = request.getMotif();
			}
			
			requesttable += String.format(model, String.valueOf(i), request.getType(), request.getBeggindate().toString(), request.getEnddate().toString(), checkCDS, checkHR, motif);
			
			i++;
		}
		
		System.out.println(String.format(""
				+ "_____________________________________________________________________\n"
				+ "-----------------------------Vos requètes----------------------------\n"
				+ ""
				+ "ID\tType\tDate de début\tDate de fin\tValidation CDS\tValidation HR\tMotif\n"
				+ "%s"
				+ ""
				+ "---------------------------------------------------------------------\n"
				+ "_____________________________________________________________________", requesttable));
		
		InputScanner.getNext();
		
		menuemploye();
	}
}
