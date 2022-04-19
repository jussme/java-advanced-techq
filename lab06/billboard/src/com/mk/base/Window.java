package com.mk.base;

import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

public class Window {

	private JFrame frmBillboard;
	private JTextField board;

	/**
	 * Create the application.
	 */
	public Window() {
		try {
		    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
		        if ("Nimbus".equals(info.getName())) {
		            UIManager.setLookAndFeel(info.getClassName());
		            break;
		        }
		    }
		} catch (Exception e) {
		   
		}
		initialize();
		frmBillboard.setVisible(true);
	}

	public void setBoardContents(String contents) {
		board.setText(contents);
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmBillboard = new JFrame();
		frmBillboard.setTitle("Billboard");
		frmBillboard.setBounds(100, 100, 552, 308);
		frmBillboard.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmBillboard.getContentPane().setLayout(new GridLayout(0, 1, 0, 0));
		
		board = new JTextField();
		board.setFont(new Font("Tahoma", Font.PLAIN, 20));
		board.setEditable(false);
		board.setHorizontalAlignment(SwingConstants.CENTER);
		frmBillboard.getContentPane().add(board);
		board.setColumns(10);
	}

}
