package views.panels;

import core.database.DatabaseEntity;
import core.structs.CDS;
import core.structs.HR;
import core.structs.Request;
import core.structs.User;
import views.MainFrame;
import views.customcomponents.ListPanel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

@SuppressWarnings("serial")
public class ChooseRequestPanel extends JPanel {
	public ChooseRequestPanel(final MainFrame container, final User user) {
		setSize(500, 500);
		setBorder(new EmptyBorder(10, 10, 10, 10));
		setLayout(new GridBagLayout());
		
		GridBagConstraints head_constraints = new GridBagConstraints();
		GridBagConstraints footer_constraints = new GridBagConstraints();
		
		JPanel head_panel = new JPanel(new GridLayout(1, 2, 10, 10));
		
		JButton refresh_button = new JButton("Rafra�chir");
		JButton deconnection_button = new JButton("D�connexion");
		
		final ListPanel footer_panel = new ListPanel() {
			@Override
			public void handleDoubleClick(DatabaseEntity entity) {
				new MainFrame().loadActOnRequestPanel(this, (Request) entity, user);
			}

			@Override
			public DatabaseEntity[] getData() {
				Request[] requests = null;
				
				if(user instanceof CDS) {
					requests = Request.selectRequestsNotCheckedByCDS();
				}
				else if(user instanceof HR) {
					requests = Request.selectRequestsNotCheckedByHR();
				}
				
				return requests;
			}
		};
		
		refresh_button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				super.mouseClicked(arg0);
				
				footer_panel.refreshTable();
				
				container.repaint();
			}
		});
		
		deconnection_button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				
				container.loadIdentificationPanel();
			}
		});
		
		footer_panel.refreshTable();
		
		head_constraints.gridx = 0;
		head_constraints.gridy = 0;
		head_constraints.weightx = 1;
		head_constraints.weighty = 0.1;
		head_constraints.fill = GridBagConstraints.HORIZONTAL;
		
		footer_constraints.gridx = 0;
		footer_constraints.gridy = 1;
		footer_constraints.weightx = 1;
		footer_constraints.weighty = 1;
		footer_constraints.fill = GridBagConstraints.BOTH;
		
		head_panel.add(refresh_button);
		head_panel.add(deconnection_button);
		
		add(head_panel, head_constraints);
		add(footer_panel, footer_constraints);
		
		repaint();
	}
}
