package views.customcomponents;

import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import core.database.DatabaseEntity;

@SuppressWarnings("serial")
public abstract class ListPanel extends JPanel {
	private JFrame container;
	
	private JTable datatable;
	private JScrollPane scrollpane;
	
	private DatabaseEntity[] data;
	private Object[][] datatabledata;

	public ListPanel(JFrame container) {
		super();
		
		this.container = container;
		
		setLayout(new BorderLayout());
		
		datatable = new JTable();
		scrollpane = new JScrollPane(datatable);
	
		add(scrollpane, BorderLayout.CENTER);
	}
	
	public void refreshTable() {
		refresh();
		
		String[] columnnames = getColumnNames();
		DatabaseEntity[] data = getData();
		
		datatabledata = new Object[data.length][columnnames.length];
		
		for(int i = 0; i < data.length; i++) {
			Map<String, String> entitydata = data[i].serialize();
			Set<String> keys = entitydata.keySet();
			String[] keysarray = keys.toArray(new String[keys.size()]);
			
			for(int j = 0; j < columnnames.length; j++) {
				datatabledata[i][j] = entitydata.get(keysarray[j]);
				columnnames[j] = keysarray[j];
			}
		}
		
		datatable = new JTable(datatabledata, columnnames) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		datatable.setSize(200, 200);
		datatable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				
				if(e.getClickCount() == 2) {
					DatabaseEntity selectentity = getSelectedEntity();
					
					if(selectentity != null) {
						JPanel newpanel = handleDoubleClick(selectentity);
						
						if(newpanel != null) {
							//((ListPanel) container).add(newpanel);
						}
					}
					
				}
			}
		});
		
		remove(scrollpane);
		scrollpane = new JScrollPane(datatable);
		add(scrollpane, BorderLayout.CENTER);
	}
	
	public DatabaseEntity getSelectedEntity() {
		DatabaseEntity result = null;
		
		if(datatable.getSelectedRowCount() == 1) {
			int selectedindex = datatable.getSelectedRow();
			
			data = getData();
			
			if(selectedindex >= 0 && data != null) {
				for(DatabaseEntity entity : data) {
					if(entity.getID() == Integer.parseInt(datatabledata[selectedindex][0].toString())) {
						result = entity;
						
						break;
					}
				}
			}
		}
		
		return result;
	}
	
	public void deleteSelectedItem() {
		getSelectedEntity().delete();
	}
	
	public abstract JPanel handleDoubleClick(DatabaseEntity entity);
	
	public abstract String[] getColumnNames();
	public abstract DatabaseEntity[] getData();
	protected abstract void refresh();
}
