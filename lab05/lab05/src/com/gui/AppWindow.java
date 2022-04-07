package com.gui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;

public class AppWindow {

	private JFrame frame;
	private JTable table;
	private MyTableModel model = new MyTableModel();
	private JTextArea textArea;
	
	public interface Mediator{
		List<List<String>> loadCSV(Path file);
		List<List<String>> reloadCSV();
		void compileResults(Consumer<String> allResultsConsumer);
	}

	
	public AppWindow(Mediator mediator) {
		try {
		    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
		        if ("Windows".equals(info.getName())) {
		            UIManager.setLookAndFeel(info.getClassName());
		            break;
		        }
		    }
		} catch (Exception e) {
		   
		}
		initialize(mediator);
		frame.setVisible(true);
	}
	
	private class MyTableModel extends AbstractTableModel{
		private static final long serialVersionUID = 1L;
		
		private List<List<String>> table = Arrays.asList(new ArrayList<String>());
		private List<String> colNames = new ArrayList<>();
		
		public void setTable(List<List<String>> table) {
			colNames = new ArrayList<String>(table.get(0));
			
			this.table = new ArrayList<List<String>>();
			this.table = table.subList(1, table.size()).stream().collect(Collectors.toList());
			
			fireTableStructureChanged();
		}
		
		public String getColumnName(int col) {
	        return colNames.get(col);
	    }
	    public int getRowCount() { return table.size(); }
	    public int getColumnCount() { return table.get(0).size(); }
	    public Object getValueAt(int row, int col) {
	        return table.get(row).get(col);
	    }/*
	    public boolean isCellEditable(int row, int col)
	        { return false; }
	    public void setValueAt(Object value, int row, int col) {
	    	table.get(row).set(col, String.valueOf(value));
	        fireTableCellUpdated(row, col);
	    }
	    */
	}
	
	private void setTable(List<List<String>> table) {
		this.model.setTable(table);
	}
	
	private static Path chooseFile() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		if(fileChooser.showOpenDialog(fileChooser) == JFileChooser.APPROVE_OPTION) {
			Path dir = fileChooser.getSelectedFile().toPath();
			return dir;
		}
		return null;
	}
	
	private void setCalcResults(String results) {
		this.textArea.setText(results);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(Mediator mediator) {
		frame = new JFrame();
		frame.setBounds(100, 100, 728, 480);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWeights = new double[]{1.0};
		gridBagLayout.rowWeights = new double[]{0.0, 1.0};
		frame.getContentPane().setLayout(gridBagLayout);
		
		JPanel panel_1 = new JPanel();
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.fill = GridBagConstraints.BOTH;
		gbc_panel_1.insets = new Insets(0, 0, 5, 0);
		gbc_panel_1.gridx = 0;
		gbc_panel_1.gridy = 0;
		frame.getContentPane().add(panel_1, gbc_panel_1);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new EmptyBorder(5, 5, 5, 5));
		GridBagConstraints gbc_panel_2 = new GridBagConstraints();
		gbc_panel_2.fill = GridBagConstraints.BOTH;
		gbc_panel_2.gridx = 0;
		gbc_panel_2.gridy = 1;
		frame.getContentPane().add(panel_2, gbc_panel_2);
		
		JButton btnLoadCSV = new JButton("Load file");
		btnLoadCSV.setFont(new Font("Tahoma", Font.PLAIN, 16));
		
		panel_1.add(btnLoadCSV);
		
		JButton btnReload = new JButton("Reload file");
		btnReload.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnReload.addActionListener(event -> {
			new Thread(() -> {
				this.setTable(mediator.reloadCSV());
			}).start();
		});
		panel_1.add(btnReload);
		GridBagLayout gbl_panel_2 = new GridBagLayout();
		gbl_panel_2.columnWidths = new int[]{605, 99, 0};
		gbl_panel_2.rowHeights = new int[]{389, 0};
		gbl_panel_2.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		gbl_panel_2.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		panel_2.setLayout(gbl_panel_2);
		
		table = new JTable(model);
		table.setFont(new Font("Tahoma", Font.PLAIN, 13));
		table.setAutoCreateRowSorter(true);
		table.setDefaultEditor(Object.class, null);//non-editable
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.getTableHeader().setReorderingAllowed(false);
		
		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.insets = new Insets(0, 0, 0, 5);
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 0;
		panel_2.add(scrollPane, gbc_scrollPane);
		scrollPane.setViewportView(table);
		
		JPanel panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.anchor = GridBagConstraints.WEST;
		gbc_panel.fill = GridBagConstraints.VERTICAL;
		gbc_panel.gridx = 1;
		gbc_panel.gridy = 0;
		panel_2.add(panel, gbc_panel);
		
		JButton btnCalculate = new JButton("Calculate");
		btnCalculate.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnCalculate.addActionListener(event -> {
			mediator.compileResults(this::setCalcResults);
		});
		panel.setLayout(new BorderLayout(0, 0));
		panel.add(btnCalculate, BorderLayout.NORTH);
		
		textArea = new JTextArea();
		panel.add(textArea);
		
		btnLoadCSV.addActionListener(event -> {
			new Thread(() -> {
				var path = chooseFile();
				if(path == null){
					return;
				}
				this.setTable(mediator.loadCSV(path));
			}).start();
		});
	}

}
