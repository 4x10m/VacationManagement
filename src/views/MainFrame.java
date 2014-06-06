package views;

import java.awt.EventQueue;

import javax.swing.JFrame;

import core.structs.Employe;
import core.structs.Request;
import core.structs.User;
import views.customcomponents.ListPanel;
import views.panels.ActOnRequestPanel;
import views.panels.AuthPanel;
import views.panels.ChooseRequestPanel;
import views.panels.EmployePanel;
import views.panels.MotifPanel;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public MainFrame() {
		setVisible(true);
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		
		loadIdentificationPanel();
	}
	
	public void loadIdentificationPanel() {
		setTitle("Identification");
		setContentPane(new AuthPanel(this));
		
		setSize(385, 260);
		repaint();
	}

	public void loadEmployePanel(User user) {
		setTitle("Panneau de l'employ�");
		setContentPane(new EmployePanel(this, (Employe) user));
		
		setSize(449, 395);
		repaint();
	}
	
	public void loadSuperiorPanel(User user) {
		setTitle("Gestion des demandes");
		
		setContentPane(new ChooseRequestPanel(this, user));
		
		setSize(470, 500);
		repaint();
	}
	
	public void loadActOnRequestPanel(ListPanel panel, Request request, User user) {
		setTitle("Demande");
		
		setContentPane(new ActOnRequestPanel(this, panel, request, user));
		
		setSize(470, 500);
		repaint();
	}
	
	public void loadMotifPanel(ListPanel paneltorefresh, Request request, User user) {
		setTitle("Motif");
		
		setContentPane(new MotifPanel(this, paneltorefresh, request, user));
		
		setSize(470, 500);
		repaint();
	}
}