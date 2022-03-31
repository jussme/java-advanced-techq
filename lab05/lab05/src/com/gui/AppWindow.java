package com.gui;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import java.awt.GridLayout;
import javax.swing.JPanel;
import javax.swing.JList;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import java.awt.Font;

public class AppWindow {

	private JFrame frame;

	
	public AppWindow() {
		try {
		    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
		        if ("Windows".equals(info.getName())) {
		            UIManager.setLookAndFeel(info.getClassName());
		            break;
		        }
		    }
		} catch (Exception e) {
		   
		}
		initialize();
		frame.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 728, 480);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new GridLayout(0, 1, 0, 0));
		
		JPanel panel_1 = new JPanel();
		frame.getContentPane().add(panel_1);
		
		JButton btnLoadCSV = new JButton("Load .csv");
		btnLoadCSV.setFont(new Font("Tahoma", Font.PLAIN, 16));
		panel_1.add(btnLoadCSV);
		
		JButton btnReload = new JButton("Reload .csv");
		btnReload.setFont(new Font("Tahoma", Font.PLAIN, 16));
		panel_1.add(btnReload);
		
		JPanel panel_2 = new JPanel();
		frame.getContentPane().add(panel_2);
		
		JList listServices = new JList();
		listServices.setFont(new Font("Tahoma", Font.PLAIN, 16));
		panel_2.add(listServices);
		
		JPanel panel_3 = new JPanel();
		frame.getContentPane().add(panel_3);
		
		JScrollPane scrollPane = new JScrollPane();
		panel_3.add(scrollPane);
		
		JTextPane textPane = new JTextPane();
		textPane.setFont(new Font("Tahoma", Font.PLAIN, 16));
		scrollPane.setViewportView(textPane);
	}

}
