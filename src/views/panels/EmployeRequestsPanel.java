package views.panels;

import javax.swing.JPanel;

import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import core.database.DatabaseEntity;
import core.structs.Employe;
import core.structs.Request;
import views.customcomponents.ListPanel;

public class EmployeRequestsPanel extends ListPanel {
	private static final long serialVersionUID = 130455863028890224L;
	
	private DatabaseEntity[] requests;
	
	private Employe employe;
	private JFrame table;

	public EmployeRequestsPanel(JFrame container, Employe employe) {
		super(container); 
		
		this.employe = employe;
		
		refreshTable();
	}

	@Override
	public JPanel handleDoubleClick(DatabaseEntity entity) {
		return null;
	}

	@Override
	public String[] getColumnNames() {
		return new String[] { "id", "type", "begin date", "end date", "check by cds", "check by hr", "motif" };
	}

	@Override
	public DatabaseEntity[] getData() {
		return requests;
	}

	@Override
	public void refresh() {
		Object[] temp = employe.getRequests().toArray();
		requests = new DatabaseEntity[temp.length];
		
		for(int i = 0; i < temp.length; i++) requests[i] = (DatabaseEntity) temp[i];
	}
}