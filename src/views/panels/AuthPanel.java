package views.panels;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.border.EmptyBorder;

import core.exceptions.InvalidCredentialException;
import core.structs.CDS;
import core.structs.Employe;
import core.structs.HR;
import core.structs.User;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;

import views.MainFrame;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

@SuppressWarnings("serial")
public class AuthPanel extends JPanel {
	public AuthPanel(final MainFrame container) {
		container.setTitle("Identification");
		
		setBackground(new Color(0, 0, 0));
		setLayout(new BorderLayout());
		
		JPanel content = new JPanel(new GridLayout(2, 1, 10, 10));
		JPanel id_panel = new JPanel(new GridLayout(2, 2, 10, 10));
		JPanel button_panel = new JPanel(new GridLayout(1, 1, 30, 30));
		
		JLabel id_label = new JLabel("Identifiant");
		JLabel password_label = new JLabel("Mot de passe");
		
		final JTextField id_textfield = new JTextField();
		final JTextField password_textfield = new JTextField();
		
		content.setBackground(Color.black);
		content.setBorder(new EmptyBorder(10, 10, 10, 10));
		id_panel.setBackground(Color.black);
		button_panel.setBackground(Color.black);
		button_panel.setBorder(new EmptyBorder(10, 30, 10, 30));
		
		id_label.setForeground(new Color(255, 255, 255));
		password_label.setForeground(new Color(255, 255, 255));
		
		final JButton connection_button = new JButton("Connexion");
		connection_button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				setEnabled(false);
				
				connection_button.setText("Connexion en cours...");
				repaint();
				
				try {
					User user = User.identify(id_textfield.getText(), password_textfield.getText());
					
					if(user instanceof Employe) {
						container.loadEmployePanel((Employe) user);
					}
					
					if(user instanceof CDS || user instanceof HR) {
						container.loadSuperiorPanel(user);
					}
				} catch (InvalidCredentialException e) {
					JOptionPane.showMessageDialog(AuthPanel.this, "Identifiants incorrects, réessayez...", "Attention", JOptionPane.WARNING_MESSAGE);
					
					setEnabled(true);
					connection_button.setText("Connexion");
				}
			}
		});
		connection_button.setBackground(new Color(192, 192, 192));
		
		id_panel.add(id_label);
		id_panel.add(id_textfield);
		id_panel.add(password_label);
		id_panel.add(password_textfield);
		
		button_panel.add(connection_button);
		
		content.add(id_panel);
		content.add(button_panel);
		
		add(content, BorderLayout.CENTER);
	}
}
