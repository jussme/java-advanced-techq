package gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

import base.DatabaseWrapper;
import hibernate.entities.BaseEntity;
import hibernate.entities.Event;
import hibernate.entities.Installment;
import hibernate.entities.Payment;
import hibernate.entities.Person;

public class AppWindow {

	private JFrame frame;
	private JTable tableInstallments;
	private JTable tablePayments;
	private EntityTableModel<Installment> installmentsModel;
	private EntityTableModel<Payment> paymentsModel;

	public interface Mediator {
		public <T extends BaseEntity> void newEntity(Class<T> entityClass);
		public <T extends BaseEntity> boolean read(Class<T> entityClass);
	}
	
	private class EntityTableModel<T extends BaseEntity> extends AbstractTableModel{
		private static final long serialVersionUID = 1L;
		
		private List<List<String>> table = new ArrayList<List<String>>();
		private List<String> colNames = Arrays.asList("Id/hash", "BuffTotal", "BuffFree", "Running");
		
		public EntityTableModel(List<T> records) {
			setTable(records);
		}
		
		public boolean setTable(List<T> records) {
			if(records.isEmpty()) {
				return false;
			}
			
			colNames = records.get(0).getProperties();
			table = records.stream().map(BaseEntity::toStrings).collect(Collectors.toList());
			fireTableStructureChanged();
			return true;
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
	
	/**
	 * Create the application.
	 */
	public AppWindow(Mediator mediator) {
		try {
		    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
		        if ("Nimbus".equals(info.getName())) {
		            UIManager.setLookAndFeel(info.getClassName());
		            break;
		        }
		    }
		} catch (Exception e) {
		   
		}
		initialize(mediator);
		frame.setVisible(true);
	}

	public static Path chooseFile() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		if(fileChooser.showOpenDialog(fileChooser) == JFileChooser.APPROVE_OPTION) {
			Path dir = fileChooser.getSelectedFile().toPath();
			return dir;
		}
		return null;
	}
	
	//stack
	private void resizeColumnWidth(JTable table) {
	    final TableColumnModel columnModel = table.getColumnModel();
	    for (int column = 0; column < table.getColumnCount(); column++) {
	        int width = 15; // Min width
	        for (int row = 0; row < table.getRowCount(); row++) {
	            TableCellRenderer renderer = table.getCellRenderer(row, column);
	            Component comp = table.prepareRenderer(renderer, row, column);
	            width = Math.max(comp.getPreferredSize().width +1 , width);
	        }
	        if(width > 300)
	            width=300;
	        columnModel.getColumn(column).setPreferredWidth(width);
	    }
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(Mediator mediator) {
		frame = new JFrame();
		frame.setBounds(100, 100, 1090, 462);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new GridLayout(0, 2, 0, 0));
		
		JPanel panel_2 = new JPanel();
		panel.add(panel_2);
		panel_2.setLayout(new GridLayout(0, 1, 0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		//scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		//scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);     
		panel_2.add(scrollPane);
		
		var installments = DatabaseWrapper.getEntities(Installment.class);
		installmentsModel = new EntityTableModel<Installment>(installments);
		tableInstallments = new JTable(installmentsModel);
		tableInstallments.setFont(new Font("Tahoma", Font.PLAIN, 16));
		tableInstallments.getTableHeader().setReorderingAllowed(false);
		tableInstallments.setDefaultEditor(Object.class, null);//non-editable
		tableInstallments.setAutoCreateRowSorter(true);
		tableInstallments.setBorder(new EmptyBorder(5, 5, 5, 5));
		//panel.add(table);
		resizeColumnWidth(tableInstallments);
		scrollPane.setViewportView(tableInstallments);
		
		JPanel panel_3 = new JPanel();
		panel.add(panel_3);
		panel_3.setLayout(new GridLayout(0, 1, 0, 0));
		
		JScrollPane scrollPane_1 = new JScrollPane();
		panel_3.add(scrollPane_1);
		
		var payments = DatabaseWrapper.getEntities(Payment.class);
		paymentsModel = new EntityTableModel<Payment>(payments);
		tablePayments = new JTable(paymentsModel);
		tablePayments.setFont(new Font("Tahoma", Font.PLAIN, 16));
		tablePayments.setAutoCreateRowSorter(true);
		tablePayments.getTableHeader().setReorderingAllowed(false);
		tablePayments.setDefaultEditor(Object.class, null);//non-editable
		tablePayments.setBorder(new EmptyBorder(5, 5, 5, 5));
		//panel.add(table_1);
		resizeColumnWidth(tablePayments);
		scrollPane_1.setViewportView(tablePayments);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
		frame.getContentPane().add(panel_1, BorderLayout.EAST);
		GridBagLayout gbl_panel_1 = new GridBagLayout();
		gbl_panel_1.columnWeights = new double[]{0.0};
		gbl_panel_1.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
		panel_1.setLayout(gbl_panel_1);
		
		JButton btnEvent = new JButton("New Event");
		btnEvent.addActionListener(event -> {
			mediator.newEntity(Event.class);
		});
		GridBagConstraints gbc_btnEvent = new GridBagConstraints();
		gbc_btnEvent.insets = new Insets(5, 5, 5, 0);
		gbc_btnEvent.gridx = 0;
		gbc_btnEvent.gridy = 0;
		panel_1.add(btnEvent, gbc_btnEvent);
		
		JButton btnInstallment = new JButton("New Installment");
		btnInstallment.addActionListener(event -> {
			mediator.newEntity(Installment.class);
		});
		GridBagConstraints gbc_btnInstallment = new GridBagConstraints();
		gbc_btnInstallment.insets = new Insets(0, 5, 5, 0);
		gbc_btnInstallment.gridx = 0;
		gbc_btnInstallment.gridy = 1;
		panel_1.add(btnInstallment, gbc_btnInstallment);
		
		JButton btnPayment = new JButton("New Payment");
		btnPayment.addActionListener(event -> {
			mediator.newEntity(Payment.class);
		});
		GridBagConstraints gbc_btnPayment = new GridBagConstraints();
		gbc_btnPayment.insets = new Insets(0, 5, 5, 0);
		gbc_btnPayment.gridx = 0;
		gbc_btnPayment.gridy = 2;
		panel_1.add(btnPayment, gbc_btnPayment);
		
		JButton btnPerson = new JButton("New Person");
		btnPerson.addActionListener(event -> {
			mediator.newEntity(Person.class);
		});
		GridBagConstraints gbc_btnPerson = new GridBagConstraints();
		gbc_btnPerson.insets = new Insets(0, 5, 5, 0);
		gbc_btnPerson.gridx = 0;
		gbc_btnPerson.gridy = 3;
		panel_1.add(btnPerson, gbc_btnPerson);
		
		JButton btnRefreshTables = new JButton("Refresh tables");
		GridBagConstraints gbc_btnRefreshTables = new GridBagConstraints();
		gbc_btnRefreshTables.insets = new Insets(0, 0, 5, 0);
		gbc_btnRefreshTables.gridx = 0;
		gbc_btnRefreshTables.gridy = 4;
		btnRefreshTables.addActionListener(event -> {
			installmentsModel.setTable(DatabaseWrapper.getEntities(Installment.class));
			paymentsModel.setTable(DatabaseWrapper.getEntities(Payment.class));
			resizeColumnWidth(tableInstallments);
			resizeColumnWidth(tablePayments);
		});
		panel_1.add(btnRefreshTables, gbc_btnRefreshTables);
		
		JButton btnReadInstallments = new JButton("Read installments");
		btnReadInstallments.addActionListener(event -> {
			if(!mediator.read(Installment.class)) {
				JOptionPane.showMessageDialog(null,
						"Import error",
						"Error",
						JOptionPane.ERROR_MESSAGE);
			}
		});
		GridBagConstraints gbc_btnReadInstallments = new GridBagConstraints();
		gbc_btnReadInstallments.insets = new Insets(0, 5, 5, 5);
		gbc_btnReadInstallments.gridx = 0;
		gbc_btnReadInstallments.gridy = 5;
		panel_1.add(btnReadInstallments, gbc_btnReadInstallments);
		
		JButton btnReadPayments = new JButton("Read payments");
		btnReadPayments.addActionListener(event -> {
			if(!mediator.read(Payment.class)) {
				JOptionPane.showMessageDialog(null,
						"Import error",
						"Error",
						JOptionPane.ERROR_MESSAGE);
			}
		});
		GridBagConstraints gbc_btnReadPayments = new GridBagConstraints();
		gbc_btnReadPayments.insets = new Insets(0, 5, 5, 5);
		gbc_btnReadPayments.gridx = 0;
		gbc_btnReadPayments.gridy = 6;
		panel_1.add(btnReadPayments, gbc_btnReadPayments);
	}

}
