package views;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;

public class IDPanel extends JPanel {
	private JTextField textField;
	private JTextField textField_1;

	/**
	 * Create the panel.
	 */
	public IDPanel() {
		setLayout(null);
		
		JLabel lblIdentifiant = new JLabel("Identifiant");
		lblIdentifiant.setBounds(10, 11, 60, 14);
		add(lblIdentifiant);
		
		textField = new JTextField();
		textField.setBounds(80, 8, 86, 20);
		add(textField);
		textField.setColumns(10);
		
		JLabel lblMotDePasse = new JLabel("Mot de passe");
		lblMotDePasse.setBounds(10, 42, 70, 14);
		add(lblMotDePasse);
		
		textField_1 = new JTextField();
		textField_1.setBounds(80, 39, 86, 20);
		add(textField_1);
		textField_1.setColumns(10);
		
		JButton btnConnexion = new JButton("Connexion");
		btnConnexion.setBounds(35, 67, 89, 23);
		add(btnConnexion);

	}
}
