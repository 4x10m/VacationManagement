package views.panels;

import javax.swing.JPanel;

import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Date;
import java.sql.Timestamp;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.JButton;

import core.enums.RequestType;
import core.exceptions.NotEnoughTimeInMeter;
import core.exceptions.RequestBegginDateBeforeEndDateException;
import core.exceptions.RequestBegginDateBeforeTodayException;
import core.exceptions.RequestDateIntervalDurationException;
import core.structs.Employe;
import core.structs.Request;

public class NewRequestFrame extends JFrame {
	private static final long serialVersionUID = 6441125042263197670L;
	
	private JTextField begindate_textfield;
	private JTextField enddate_textfield;
	private JRadioButton rdbtnNewRadioButton_1;
	private JRadioButton rdbtnNewRadioButton_2;
	private JRadioButton rdbtnNewRadioButton;
	
	private Employe employe;
	
	public Employe getEmploye() {
		return employe;
	}

	private NewRequestFrame() {
		setLayout(new GridLayout(0, 2, 0, 0));
		
		JLabel lblType = new JLabel("type");
		add(lblType);
		
		JPanel panel = new JPanel();
		add(panel);
		
		rdbtnNewRadioButton_1 = new JRadioButton("rtt");
		panel.add(rdbtnNewRadioButton_1);
		
		rdbtnNewRadioButton_2 = new JRadioButton("cong\u00E9s pay\u00E9s");
		panel.add(rdbtnNewRadioButton_2);
		
		rdbtnNewRadioButton = new JRadioButton("formation");
		panel.add(rdbtnNewRadioButton);
		
		JLabel lblNewLabel = new JLabel("date de d\u00E9but");
		add(lblNewLabel);
		
		begindate_textfield = new JTextField();
		add(begindate_textfield);
		begindate_textfield.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("date de fin");
		add(lblNewLabel_1);
		
		enddate_textfield = new JTextField();
		add(enddate_textfield);
		enddate_textfield.setColumns(10);
		
		JButton add_button = new JButton("Ajouter");
		add(add_button);
		
		JButton cancel_button = new JButton("Annuler");
		add(cancel_button);
	}
	
	public NewRequestFrame(Employe employe) {
		this();
		
		this.employe = employe;
	}

	class AddButtonMouseAdapter extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent arg0) {
			super.mouseClicked(arg0);
			
			RequestType type = null;
			
			if(rdbtnNewRadioButton.isSelected()) type = RequestType.REDUCTION_IN_WORKING_TIME;
			if(rdbtnNewRadioButton_1.isSelected()) type = RequestType.FORMATION;
			if(rdbtnNewRadioButton_2.isSelected()) type = RequestType.PAID_HOLLIDAYS;
			
			Request request;
			
			try {
				request = new Request(getEmploye(), type, new Timestamp(Date.parse(begindate_textfield.getText())), new Timestamp(Date.parse(enddate_textfield.getText())));
				
				getEmploye().doARequest(request);
			} catch (RequestBegginDateBeforeTodayException
					| RequestBegginDateBeforeEndDateException
					| RequestDateIntervalDurationException e) {
				e.printStackTrace();
			} catch (NotEnoughTimeInMeter e) {
				e.printStackTrace();
			}
			
			
		}
	}
	
	class CancelButtonEventHandler extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			super.mouseClicked(e);
		}
	}
}
