package views.panels;

import javax.swing.JPanel;

import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
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

@SuppressWarnings("serial")
public class NewRequestFrame extends JFrame {
	private JTextField begindate_textfield;
	private JTextField enddate_textfield;
	private JRadioButton rtt_radiobutton;
	private JRadioButton hollidays_radiobutton;
	private JRadioButton formation_radiobutton;
	
	private EmployePanel invoker;
	private Employe employe;

	public NewRequestFrame(EmployePanel invoker, Employe employe) {
		this.invoker = invoker;
		this.employe = employe;
		
		JPanel radiobutton_panel = new JPanel();
		JLabel type_label = new JLabel("type");
		JLabel begindate_label = new JLabel("Date de d\u00E9but (MM/JJ/AAAA)");
		JLabel enddate_label = new JLabel("Date de fin (MM/JJ/AAAA)");
		
		rtt_radiobutton = new JRadioButton("RTT");
		hollidays_radiobutton = new JRadioButton("Cong\u00E9s");
		formation_radiobutton = new JRadioButton("Formation");
		
		begindate_textfield = new JTextField();
		enddate_textfield = new JTextField();
		
		JButton add_button = new JButton("Ajouter");
		JButton cancel_button = new JButton("Annuler");
		
		setTitle("Nouvelle Requête");
		setSize(400, 275);
		setResizable(false);
		setLayout(new GridLayout(0, 2, 5, 10));
		
		add_button.addMouseListener(new AddButtonMouseAdapter());
		cancel_button.addMouseListener(new CancelButtonEventHandler());
		
		radiobutton_panel.setLayout(new GridLayout(3, 1));
		
		radiobutton_panel.add(rtt_radiobutton);
		radiobutton_panel.add(hollidays_radiobutton);
		radiobutton_panel.add(formation_radiobutton);

		add(type_label);
		add(radiobutton_panel);
		add(begindate_label);
		add(begindate_textfield);
		add(enddate_label);
		add(enddate_textfield);
		add(add_button);
		add(cancel_button);
	}

	class AddButtonMouseAdapter extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent arg0) {
			super.mouseClicked(arg0);
			
			RequestType type = null;
			
			if(formation_radiobutton.isSelected()) type = RequestType.REDUCTION_IN_WORKING_TIME;
			if(rtt_radiobutton.isSelected()) type = RequestType.FORMATION;
			if(hollidays_radiobutton.isSelected()) type = RequestType.PAID_HOLLIDAYS;
			
			try {
				new Request(employe, type, new Timestamp((new SimpleDateFormat("MM/dd/yyyy").parse(begindate_textfield.getText())).getTime()), new Timestamp((new SimpleDateFormat("MM/dd/yyyy").parse(enddate_textfield.getText())).getTime()));

				invoker.refresh();
				
				dispose();
			} catch (RequestBegginDateBeforeTodayException e) {
				JOptionPane.showMessageDialog(NewRequestFrame.this, e.getMessage(), "Warning", JOptionPane.WARNING_MESSAGE);
			} catch (RequestBegginDateBeforeEndDateException e) {
				JOptionPane.showMessageDialog(NewRequestFrame.this, e.getMessage(), "Warning", JOptionPane.WARNING_MESSAGE);
			} catch (RequestDateIntervalDurationException e) {
				JOptionPane.showMessageDialog(NewRequestFrame.this, e.getMessage(), "Warning", JOptionPane.WARNING_MESSAGE);
			} catch (NotEnoughTimeInMeter e) {
				JOptionPane.showMessageDialog(NewRequestFrame.this, e.getMessage(), "Warning", JOptionPane.WARNING_MESSAGE);
			} catch (ParseException e) {
				JOptionPane.showMessageDialog(NewRequestFrame.this, e.getMessage(), "Warning", JOptionPane.WARNING_MESSAGE);
			}

		}
	}
	
	class CancelButtonEventHandler extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			super.mouseClicked(e);
			
			dispose();
		}
	}
}
