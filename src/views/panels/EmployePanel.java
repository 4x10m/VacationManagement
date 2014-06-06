package views.panels;

import javax.swing.JPanel;

import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import views.MainFrame;
import views.customcomponents.ListPanel;
import core.database.DatabaseEntity;
import core.structs.Employe;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

@SuppressWarnings("serial")
public class EmployePanel extends JPanel {
	private final Employe employe;
	
	private final ListPanel panel2;
	
	private final JTextField hollidaysmeter_textfield;
	private final JTextField formationmeter_textfield;
	private final JTextField rttmeter_textfield;
	
	public EmployePanel(final MainFrame container, final Employe employe) {
		this.employe = employe;

		setLayout(new GridLayout(2, 1, 10, 10));
		setBorder(new EmptyBorder(10, 10, 10, 10));
		
		final JPanel panel = new JPanel();
		panel2 = new ListPanel() {
			@Override
			public DatabaseEntity[] getData() {
				Object[] temp = employe.getRequests().toArray();
				 DatabaseEntity[] requests = new DatabaseEntity[temp.length];
				
				for(int i = 0; i < temp.length; i++) requests[i] = (DatabaseEntity) temp[i];
				
				return requests;
			}
		};

		JLabel username_label = new JLabel(employe.getUsername());
		JLabel holliday_label = new JLabel("Compteur Cong\u00E9s :");
		JLabel rtt_label = new JLabel("Compteur RTT :");
		JLabel formation_label = new JLabel("Compteur Formation :");

		hollidaysmeter_textfield = new JTextField(String.valueOf(employe.getHollidaysMeter()));
		formationmeter_textfield = new JTextField(String.valueOf(employe.getFormationMeter()));
		rttmeter_textfield = new JTextField(String.valueOf(employe.getRTTMeter()));
		
		JButton newrequest_button = new JButton("Nouvelle Requête");
		JButton refreshButton = new JButton("Rafraîchir");
		JButton deconnection_button = new JButton("Déconnexion");
		
		hollidaysmeter_textfield.setEnabled(false);
		formationmeter_textfield.setEnabled(false);
		rttmeter_textfield.setEnabled(false);
		
		newrequest_button.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent arg0) {
				super.mouseClicked(arg0);
				
				new NewRequestFrame(EmployePanel.this, employe).setVisible(true);
			}
		});
		
		refreshButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				super.mouseClicked(arg0);
				
				refresh();
			}
		});
		
		deconnection_button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				
				container.loadIdentificationPanel();
			}
		});
		
		panel.setLayout(new GridLayout(5, 2, 10, 10));
		
		add(panel);
		add(panel2);
		
		panel.add(newrequest_button);
		panel.add(refreshButton);
		panel.add(username_label);
		panel.add(deconnection_button);
		panel.add(holliday_label);
		panel.add(hollidaysmeter_textfield);
		panel.add(rtt_label);
		panel.add(rttmeter_textfield);
		panel.add(formation_label);
		panel.add(formationmeter_textfield);
		
		refresh();
	}
	
	public void refresh() {
		panel2.refreshTable();
		
		hollidaysmeter_textfield.setText(String.valueOf(employe.getHollidaysMeter()));
		formationmeter_textfield.setText(String.valueOf(employe.getFormationMeter()));
		rttmeter_textfield.setText(String.valueOf(employe.getRTTMeter()));
	
		panel2.repaint();
		revalidate();
		repaint();
	}
}
