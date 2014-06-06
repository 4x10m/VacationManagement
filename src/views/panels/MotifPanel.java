package views.panels;

import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.border.EmptyBorder;

import views.MainFrame;
import views.customcomponents.ListPanel;
import core.structs.Request;
import core.structs.User;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

@SuppressWarnings("serial")
public class MotifPanel extends JPanel {
	public MotifPanel(final MainFrame container, final ListPanel paneltorefresh, final Request request, final User user) {
		setLayout(new GridBagLayout());
		setBorder(new EmptyBorder(10, 10, 10, 10));
		
		GridBagConstraints inforthemotivconstraints= new GridBagConstraints();
		GridBagConstraints motifconstraints = new GridBagConstraints();
		GridBagConstraints saveconstraints = new GridBagConstraints();
		
		JLabel informthemotiv_label = new JLabel("Renseigner le motif du refus :");
		final JTextPane motif_textpane = new JTextPane();
		JButton save_button = new JButton("Enregister");
		
		inforthemotivconstraints.gridx = 0;
		inforthemotivconstraints.gridy = 0;
		inforthemotivconstraints.weighty = 0.3;
		inforthemotivconstraints.weightx = 1;
		inforthemotivconstraints.fill = GridBagConstraints.HORIZONTAL;
		
		motifconstraints.gridx = 0;
		motifconstraints.gridy = 1;
		motifconstraints.ipady = 200;
		motifconstraints.weightx = 1;
		motifconstraints.fill = GridBagConstraints.HORIZONTAL;
		
		saveconstraints.gridx = 0;
		saveconstraints.gridy = 2;
		saveconstraints.weighty = 0.3;
		saveconstraints.weightx = 1;
		
		save_button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				
				request.refuse(motif_textpane.getText());
				
				paneltorefresh.refreshTable();
				
				container.dispose();
			}
		});
		
		add(informthemotiv_label, inforthemotivconstraints);
		add(motif_textpane, motifconstraints);
		add(save_button, saveconstraints);
	}
}
