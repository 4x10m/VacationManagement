package views.panels;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;

import core.database.DatabaseController;
import core.structs.CDS;
import core.structs.Employe;
import core.structs.HR;
import core.structs.User;

import java.awt.Color;

import views.MainFrame;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

@SuppressWarnings("serial")
public class AuthPanel extends JPanel {
	private JTextField id_textfield;
	private JTextField password_textfield;
	
	private MainFrame container;

	public AuthPanel(MainFrame container) {
		this.container = container;
		
		setBackground(new Color(0, 0, 0));
		setLayout(null);
		
		JLabel id_label = new JLabel("Identifiant");
		id_label.setForeground(new Color(255, 255, 255));
		id_label.setBounds(71, 57, 112, 33);
		add(id_label);
		
		id_textfield = new JTextField();
		id_textfield.setBounds(217, 57, 161, 33);
		add(id_textfield);
		id_textfield.setColumns(10);
		
		JLabel password_label = new JLabel("Mot de passe");
		password_label.setForeground(new Color(255, 255, 255));
		password_label.setBounds(71, 126, 112, 23);
		add(password_label);
		
		password_textfield = new JTextField();
		password_textfield.setBounds(217, 121, 161, 33);
		add(password_textfield);
		password_textfield.setColumns(10);
		
		JButton connection_button = new JButton("Connexion");
		connection_button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				connection_button_click(id_textfield.getText(), password_textfield.getText());
			}
		});
		connection_button.setBackground(new Color(192, 192, 192));
		connection_button.setBounds(169, 199, 112, 23);
		add(connection_button);
	}
	
	private void connection_button_click(String username, String password) {
		int id = DatabaseController.checkCredential(username, password);
		
		if(id > 0) {
			User user = User.static_load(id);
			
			if(user instanceof Employe) {
				container.loadEmployeRequestPanel((Employe) user);
			}
			
			if(user instanceof CDS) {
				container.loadEmployeRequestPanel(user);
			}
			
			if(user instanceof HR) {
				container.loadEmployeRequestPanel(user);
			}
		}
	}
}
