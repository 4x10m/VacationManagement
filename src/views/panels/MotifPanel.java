package views.panels;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.JLabel;
import javax.swing.JButton;

import core.structs.CDS;
import core.structs.HR;
import core.structs.Request;
import core.structs.User;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MotifPanel extends JPanel {
	private static final long serialVersionUID = 2386718575941658084L;

	public MotifPanel(final JFrame container, final Request request, final User user) {
		setLayout(null);
		
		JLabel lblRenseignerLeMotif = new JLabel("Renseigner le motif du refus :");
		lblRenseignerLeMotif.setBounds(10, 11, 312, 14);
		
		final JTextPane motif_textpane = new JTextPane();
		motif_textpane.setBounds(10, 66, 430, 166);
		
		JButton save_button = new JButton("Enregister");
		save_button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				
				if(user instanceof CDS) {
					request.refuseCDS(motif_textpane.getText());
				}
				
				if(user instanceof HR) {
					request.refuseHR(motif_textpane.getText());
				}
				
				container.dispose();
			}
		});
		save_button.setBounds(172, 255, 89, 23);
		
		add(lblRenseignerLeMotif);
		add(motif_textpane);
		add(save_button);
	}
}
