package views.panels;

import core.structs.CDS;
import core.structs.HR;
import core.structs.Request;
import core.structs.User;

import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.border.EmptyBorder;

import views.MainFrame;
import views.customcomponents.ListPanel;

@SuppressWarnings("serial")
public class ActOnRequestPanel extends JPanel {
	public ActOnRequestPanel(final MainFrame container, final ListPanel paneltorefresh, final Request request, final User user)
	{
		setLayout(new GridLayout(5, 2, 10, 10));
		setBorder(new EmptyBorder(10, 10, 10, 10));
		
		JLabel lblNewLabel = new JLabel("Employe");
		JLabel lblNewLabel_1 = new JLabel("Type");
		JLabel lblNewLabel_2 = new JLabel("Date de d\u00E9but");
		JLabel lblNewLabel_3 = new JLabel("Date de fin");

		JTextField employe_textfield = new JTextField();
		JTextField type_textfield = new JTextField();
		JTextField begindate_textfield = new JTextField();
		JTextField enddate_textfield = new JTextField();

		JButton accept_button = new JButton("Accepter");
		JButton refuse_button = new JButton("Refuser");
		
		employe_textfield.setText(request.getOwner().getUsername());
		type_textfield.setText(String.valueOf(request.getType()));
		begindate_textfield.setText(String.valueOf(request.getBeggindate()));
		enddate_textfield.setText(String.valueOf(request.getEnddate()));
		
		employe_textfield.setEnabled(false);
		type_textfield.setEnabled(false);
		begindate_textfield.setEnabled(false);
		enddate_textfield.setEnabled(false);
		
		accept_button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				super.mouseClicked(arg0);
				
				if(user instanceof CDS) {
					request.checkCDS();
				}
				
				if(user instanceof HR) {
					try {
						request.checkHR();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
				paneltorefresh.refreshTable();
				container.dispose();
			}
		});
		
		refuse_button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				
				container.loadMotifPanel(paneltorefresh, request, user);
			}
		});
		
		add(lblNewLabel);
		add(employe_textfield);
		add(lblNewLabel_1);
		add(type_textfield);
		add(lblNewLabel_2);
		add(begindate_textfield);
		add(lblNewLabel_3);
		add(enddate_textfield);
		add(accept_button);
		add(refuse_button);
	}
}
