package views.panels;

import javax.swing.JPanel;

import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;

import core.structs.Employe;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class EmployePanel extends JPanel {
	private JTextField textField_1;
	private JTextField textField;
	private JTextField textField_2;
	
	private Employe employe;

	/**
	 * Create the panel.
	 */
	public EmployePanel(final Employe employe, EmployeRequestsPanel panel2) {
		this.employe = employe;
		
		setLayout(new GridLayout(2, 1, 0, 0));
		
		JPanel panel = new JPanel();
		add(panel);
		panel.setLayout(null);
		
		JLabel lblNom = new JLabel(employe.getUsername());
		lblNom.setBounds(10, 11, 138, 14);
		panel.add(lblNom);
		
		JButton btnNouvelleRequete = new JButton("Nouvelle Requete");
		btnNouvelleRequete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new NewRequestFrame(employe).setVisible(true);
			}
		});
		btnNouvelleRequete.setBounds(312, 11, 128, 31);
		panel.add(btnNouvelleRequete);
		
		JLabel lblCompteurCongs = new JLabel("Compteur cong\u00E9s :");
		lblCompteurCongs.setBounds(10, 36, 101, 14);
		panel.add(lblCompteurCongs);
		
		JLabel lblNewLabel = new JLabel("Compteur rtt :");
		lblNewLabel.setBounds(10, 61, 101, 14);
		panel.add(lblNewLabel);
		
		JLabel lblCompteurFormation = new JLabel("Compteur formation");
		lblCompteurFormation.setBounds(10, 86, 101, 14);
		panel.add(lblCompteurFormation);
		
		textField_1 = new JTextField(String.valueOf(employe.getHollidaysMeter()));
		textField_1.setBounds(116, 33, 86, 20);
		panel.add(textField_1);
		textField_1.setColumns(10);
		
		textField = new JTextField(String.valueOf(employe.getFormationMeter()));
		textField.setBounds(116, 58, 86, 20);
		panel.add(textField);
		textField.setColumns(10);
		
		textField_2 = new JTextField(String.valueOf(employe.getRTTMeter()));
		textField_2.setBounds(116, 86, 86, 20);
		panel.add(textField_2);
		textField_2.setColumns(10);

		add(panel2);
	}
}
