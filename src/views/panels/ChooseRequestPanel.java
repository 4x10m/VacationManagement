package views.panels;

import javax.swing.JFrame;
import javax.swing.JPanel;

import core.database.DatabaseEntity;
import core.structs.CDS;
import core.structs.HR;
import core.structs.Request;
import core.structs.User;
import views.customcomponents.ListPanel;

import java.awt.CardLayout;

public class ChooseRequestPanel extends ListPanel {
	private static final long serialVersionUID = -2380357581347486382L;
	
	private User user;
	private Request[] requests;
	
	public ChooseRequestPanel(JFrame container, User user) {
		super(container);
		
		this.user = user;
		
		setLayout(new CardLayout(0, 0));
	}

	@Override
	public JPanel handleDoubleClick(DatabaseEntity entity) {
		return new ActOnRequestPanel((Request) entity, user);
	}

	@Override
	public String[] getColumnNames() {
		return new String[] { "Employe", "Date de début", "Date de fin", "Valider par le chef de service", "Valider par les ressources humaines" };
	}

	@Override
	public DatabaseEntity[] getData() {
		return requests;
	}

	@Override
	protected void refresh() {
		if(user instanceof CDS) {
			requests = Request.selectRequestsNotCheckedByCDS();
		}
		else if(user instanceof HR) {
			requests = Request.selectRequestsNotCheckedByHR();
		}
	}

}
