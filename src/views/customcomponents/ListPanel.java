package views.customcomponents;

import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import core.database.DatabaseEntity;
import core.structs.Request;

@SuppressWarnings("serial")
public abstract class ListPanel extends JPanel {
	private JTable datatable;
	private JScrollPane scrollpane;
	
	private Object[][] datatabledata;
	
	JLabel label = new JLabel("Aucune demande");

	public ListPanel() {
		super();
		
		setLayout(new BorderLayout());
		
		datatable = new JTable();
		scrollpane = new JScrollPane(datatable);
	
		add(scrollpane, BorderLayout.CENTER);
		
		this.setFocusTraversalKeysEnabled(true);
		
		this.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent arg0) {
				super.keyTyped(arg0);
				
				if(arg0.getKeyCode() == KeyEvent.VK_F5) refreshTable();
			}
		});
	}
	
	public void refreshTable() {
		removeAll();
		
		DatabaseEntity[] data = getData();
		Map<String, String>[] datamaps = new Map[data.length];
		
		for(int i = 0; i < data.length; i++) {
			try {
				Map<String, String> entitydata = serialize(data[i]);
				
				datamaps[i] = entitydata;
			} catch (IllegalArgumentException e1) {
				e1.printStackTrace();
			}
		}
		
		if(datamaps.length > 0) {
			String[] columnnames = datamaps[0].keySet().toArray(new String[datamaps[0].keySet().size()]);
			
			
			
			datatabledata = new Object[datamaps.length][columnnames.length];
			
			for(int i = 0; i < datamaps.length; i++) {
				for(int j = 0; j < columnnames.length; j++) {
					datatabledata[i][j] = datamaps[i].get(columnnames[j]);
				}
			}
			
			datatable = new JTable(datatabledata, columnnames) {
				@Override
				public boolean isCellEditable(int row, int column) {
					return false;
				}
			};

			datatable.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					super.mouseClicked(e);
					
					if(e.getClickCount() == 2) {
						DatabaseEntity selectentity = getSelectedEntity();
						
						if(selectentity != null) {
							handleDoubleClick(selectentity);
						}
						
					}
				}
			});
		
			datatable.setSize(new Dimension(700, 500));
			
			scrollpane = new JScrollPane(datatable, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                    JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
			scrollpane.setPreferredSize(new Dimension(700,500));
			scrollpane.setSize(new Dimension(700, 500));
			scrollpane.getViewport().setSize(new Dimension(700, 500));
			datatable.setPreferredScrollableViewportSize(new Dimension(700,500));
			datatable.setSize(new Dimension(700, 500));
			datatable.setPreferredSize(new Dimension(700, 500));
			
			scrollpane.revalidate();
			scrollpane.repaint();

			add(scrollpane, BorderLayout.CENTER);
		}
		else {
			add(new JLabel("Aucune donnée"), BorderLayout.CENTER);
		}
		
		revalidate();
		repaint();
	}
	
	private Map<String, String> serialize(DatabaseEntity databaseEntity) throws IllegalArgumentException {
		Map<String, String> data = new HashMap<String, String>();
		
		Request request = (Request) databaseEntity;
		
		data.put("nom", request.getOwner().getUsername());
		data.put("Type", request.getType().toString());
		data.put("Date de d�but", request.getBeggindate().toString());
		data.put("Date de fin", request.getEnddate().toString());
		data.put("Etat", request.getState().toString());
		data.put("Motif", request.getState().toString());
		
		return data;
	}

	public DatabaseEntity getSelectedEntity() {
		DatabaseEntity result = null;
		
		if(datatable.getSelectedRowCount() == 1) {
			DatabaseEntity[] entitys = getData();
			int selectedindex = datatable.getSelectedRow();
			
			if(selectedindex >= 0 && entitys != null) {
				result = entitys[selectedindex];
			}
		}
		
		return result;
	}
	
	public void deleteSelectedItem() {
		getSelectedEntity().delete();
	}
	
	public void handleDoubleClick(DatabaseEntity entity) { };
	
	public abstract DatabaseEntity[] getData();
}
