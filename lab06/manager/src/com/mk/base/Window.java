package com.mk.base;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.AbstractTableModel;

import com.mk.base.Manager.BillboardInfo;

public class Window {
	private final BillBoardTableModel model = new BillBoardTableModel();
	
	private JFrame frmManager;
	private JPanel panel;
	private JTable table;
	private JPanel panel_2;
	private JSpinner spinner;
	private JButton btnSetInterval;
	private JPanel panel_3;
	private JButton btnRefresh;
	private JPanel panel_4;
	private JButton btnStartStop;

	/**
	 * Create the application.
	 */
	public Window(Manager manager) {
		try {
		    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
		        if ("Nimbus".equals(info.getName())) {
		            UIManager.setLookAndFeel(info.getClassName());
		            break;
		        }
		    }
		} catch (Exception e) {
		   
		}
		initialize(manager);
		frmManager.setVisible(true);
	}
	
	private class BillBoardTableModel extends AbstractTableModel{
		private static final long serialVersionUID = 1L;
		
		private List<List<String>> table = new ArrayList<List<String>>();
		private List<String> colNames = Arrays.asList("Id/hash", "BuffTotal", "BuffFree", "Running");
		
		//wystarczajaco wydajne
		public synchronized void updateBillboard(BillboardInfo info) {//tablica czy wytypowane?
			boolean found = false;
			for(int it = 0; it < table.size(); ++ it) {
				if(Integer.valueOf(table.get(it).get(0)) == info.hash) {//dry?
					table.get(it).set(1, String.valueOf(info.buffTotal));//jezeli tablica mozna petle
					table.get(it).set(2, String.valueOf(info.buffFree));//jezeli tablica mozna petle
					table.get(it).set(3, String.valueOf(info.running));//jezeli tablica mozna petle
					found = true;
					break;
				}
			}
			if (!found) {
				table.add(Arrays.asList(String.valueOf(info.hash),
						String.valueOf(info.buffTotal),
						String.valueOf(info.buffFree),
						String.valueOf(info.running)));
			}
			fireTableStructureChanged();
		}
		
		public synchronized void removeBillboard(int id) {
			table.removeIf(billboard -> Integer.valueOf(billboard.get(0)) == id);
			fireTableStructureChanged();
		}
		
		public synchronized void setTable(List<List<String>> table) {
			this.table = table;
			
			fireTableStructureChanged();
		}
		
		public String getColumnName(int col) {
	        return colNames.get(col);
	    }
	    public synchronized int getRowCount() { return table.size(); }
	    public int getColumnCount() { return table.size() > 0? table.get(0).size() : 0; }
	    public synchronized Object getValueAt(int row, int col) {
	        return table.get(row).get(col);
	    }
	}
	
	public void updateBillboard(BillboardInfo info) {
		model.updateBillboard(info);
	}
	
	public void removeBillboard(int id) {
		model.removeBillboard(id);
	}
	
	private void refreshBillboardsInfo(Manager manager) {
		var table = manager.getBillboardsInfo().stream()
				.map(bInfo -> Arrays.asList(String.valueOf(bInfo.hash),
						String.valueOf(bInfo.buffTotal),
						String.valueOf(bInfo.buffFree),
						String.valueOf(bInfo.running))).collect(Collectors.toList());
		model.setTable(table);
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(Manager manager) {
		frmManager = new JFrame();
		frmManager.setTitle("Manager");
		frmManager.setBounds(100, 100, 549, 263);
		frmManager.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmManager.getContentPane().setLayout(new GridLayout(1, 0, 0, 0));
		
		JPanel panel_1 = new JPanel();
		frmManager.getContentPane().add(panel_1);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		panel = new JPanel();
		panel.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
		panel_1.add(panel, BorderLayout.EAST);
		panel.setLayout(new GridLayout(3, 1, 0, 0));
		
		panel_4 = new JPanel();
		panel.add(panel_4);
		
		btnStartStop = new JButton("Start/Stop");
		btnStartStop.setFont(new Font("SansSerif", Font.PLAIN, 16));
		btnStartStop.addActionListener(event -> {
			var row = table.getSelectedRow();
			if(row >= 0) {
				manager.startStop(Integer.valueOf(model.table.get(row).get(0)));
				refreshBillboardsInfo(manager);
			}
		});
		panel_4.add(btnStartStop);
		
		panel_2 = new JPanel();
		panel.add(panel_2);
		
		spinner = new JSpinner();
		spinner.setFont(new Font("SansSerif", Font.PLAIN, 16));
		spinner.setModel(new SpinnerNumberModel(new Integer(5), new Integer(1), null, new Integer(1)));
		spinner.setPreferredSize(new Dimension(60, 40));
		panel_2.add(spinner);
		
		btnSetInterval = new JButton("Set interval");
		btnSetInterval.setFont(new Font("SansSerif", Font.PLAIN, 16));
		btnSetInterval.addActionListener(event -> {
			var row = table.getSelectedRow();
			if(row >= 0)
				manager.setInterval(Integer.valueOf(model.table.get(row).get(0)), (Integer)spinner.getValue());
		});
		panel_2.add(btnSetInterval);
		
		panel_3 = new JPanel();
		panel.add(panel_3, BorderLayout.NORTH);
		
		btnRefresh = new JButton("Refresh");
		btnRefresh.setFont(new Font("SansSerif", Font.PLAIN, 16));
		btnRefresh.addActionListener(event -> {
			refreshBillboardsInfo(manager);
		});
		panel_3.add(btnRefresh);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportBorder(new TitledBorder(new EtchedBorder(EtchedBorder.RAISED, null, null), "Billboards", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.add(scrollPane);
		
		table = new JTable(model) {
			@Override
			public void valueChanged(ListSelectionEvent event) {
				super.valueChanged(event);
				//if(!event.getValueIsAdjusting() && this.getSelectedRow() != -1) {
				//	new Thread(() -> refreshBillboardInfo(manager, Integer.valueOf(model.table.get(this.getSelectedRow()).get(0)))).start();
				//}
			}
		};
		table.setDefaultEditor(Object.class, null);//non-editable
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.getTableHeader().setReorderingAllowed(false);
		scrollPane.setViewportView(table);
	}

}
