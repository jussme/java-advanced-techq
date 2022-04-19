package com.mk.base;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

public class ClientWindow {

	private JFrame frame;
	private JTextField textField_anounc;
	private JTextField textField_time;
	private DefaultListModel<String> listModel;

	/**
	 * Create the application.
	 */
	public ClientWindow(Client client) {
		try {
		    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
		        if ("Nimbus".equals(info.getName())) {
		            UIManager.setLookAndFeel(info.getClassName());
		            break;
		        }
		    }
		} catch (Exception e) {
		   
		}
		initialize(client);
		frame.setVisible(true);
	}

	public void reportId(int id) {
		listModel.addElement(String.valueOf(id));
	}
	
	public void delId(int id) {
		listModel.removeElement(String.valueOf(id));
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(Client client) {
		frame = new JFrame();
		frame.setBounds(100, 100, 618, 358);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new GridLayout(2, 0, 0, 0));
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel);
		
		listModel = new DefaultListModel<String>();
		panel.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_2 = new JPanel();
		panel.add(panel_2, BorderLayout.CENTER);
		
		JScrollPane scrollPane = new JScrollPane();
		panel_2.add(scrollPane);
		
		JList<String> list_anounc = new JList<>(listModel);
		list_anounc.setFont(new Font("Tahoma", Font.PLAIN, 16));
		scrollPane.setViewportView(list_anounc);
		list_anounc.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		JPanel panel_3 = new JPanel();
		panel.add(panel_3, BorderLayout.EAST);
		
		JButton btnCancel = new JButton("Cancel order");
		btnCancel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnCancel.addActionListener(event -> {
			client.withdrawOrder(list_anounc.getSelectedValue());
		});
		panel_3.add(btnCancel);
		
		JPanel panel_1 = new JPanel();
		frame.getContentPane().add(panel_1);
		
		textField_anounc = new JTextField();
		textField_anounc.setHorizontalAlignment(SwingConstants.CENTER);
		textField_anounc.setFont(new Font("Tahoma", Font.PLAIN, 16));
		panel_1.add(textField_anounc);
		textField_anounc.setColumns(10);
		
		textField_time = new JTextField();
		textField_time.setHorizontalAlignment(SwingConstants.CENTER);
		textField_time.setFont(new Font("Tahoma", Font.PLAIN, 16));
		panel_1.add(textField_time);
		textField_time.setColumns(10);
		
		JButton btn_order = new JButton("Order");
		btn_order.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btn_order.addActionListener(event -> {
			if(!client.placeOrder(textField_anounc.getText(), textField_time.getText())) {
				JOptionPane.showMessageDialog(this.frame, "Could not place order", "Failure", JOptionPane.ERROR_MESSAGE);
			}
		});
		panel_1.add(btn_order);
	}

}
