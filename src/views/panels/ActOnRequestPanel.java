package views.panels;

import javax.swing.JPanel;

import core.structs.CDS;
import core.structs.HR;
import core.structs.Request;
import core.structs.User;

import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;

public class ActOnRequestPanel extends JPanel {
	private static final long serialVersionUID = -771144492023332037L;
	
	private JTextField employe_textfield;
	private JTextField type_textfield;
	private JTextField begindate_textfield;
	private JTextField enddate_textfield;
	
	private JButton accept_button;
	private JButton refuse_button;

	private ActOnRequestPanel()
	{
		setLayout(new GridLayout(0, 2, 0, 0));
		
		JLabel lblNewLabel = new JLabel("Employe");
		JLabel lblNewLabel_1 = new JLabel("Type");
		JLabel lblNewLabel_2 = new JLabel("Date de d\u00E9but");
		JLabel lblNewLabel_3 = new JLabel("Date de fin");

		employe_textfield = new JTextField();
		type_textfield = new JTextField();
		begindate_textfield = new JTextField();
		enddate_textfield = new JTextField();

		accept_button = new JButton("Accepter");
		refuse_button = new JButton("Refuser");
		
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

	public ActOnRequestPanel(final Request request, final User user) {
		this();
		
		final JFrame container = new JFrame();
		
		container.setContentPane(this);
		
		employe_textfield.setText(request.getOwner().getUsername());
		type_textfield.setText(String.valueOf(request.getType()));
		begindate_textfield.setText(String.valueOf(request.getBeggindate()));
		enddate_textfield.setText(String.valueOf(request.getEnddate()));
		
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
			}
		});
		
		refuse_button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				
				container.setContentPane(new MotifPanel(container, request, user));
			}
		});
	}
}
