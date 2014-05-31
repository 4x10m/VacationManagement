package views;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import core.database.DatabaseConnector;
import core.database.DatabaseController;
import core.structs.Employe;
import core.structs.User;
import views.panels.AuthPanel;
import views.panels.EmployePanel;
import views.panels.EmployeRequestsPanel;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DatabaseConnector connector = new DatabaseConnector();
					connector.connect();
					
					DatabaseController.setConnection(connector);
					
					MainFrame frame = new MainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		setExtendedState(MAXIMIZED_BOTH);
		
		loadIdentificationPanel();
	}
	
	public void loadIdentificationPanel() {
		setContentPane(new AuthPanel(this));
		
		repaint();
	}

	public void loadEmployeRequestPanel(User user) {
		EmployeRequestsPanel panel2 = new EmployeRequestsPanel(this, (Employe) user);
		
		setContentPane(new EmployePanel((Employe) user, panel2));
		
		pack();
		repaint();
	}
}