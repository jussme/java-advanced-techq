package com.mk.java.gui;

import java.nio.file.Path;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import java.awt.GridLayout;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.Font;

public class AppWindow {

	private JFrame frame;
	
	static public interface Mediator {
		void chooseInputFile();
		void chooseOutputFile();
		void chooseKey(boolean privateKey);
		void encrypt();
		void decrypt();
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

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(Mediator mediator) {
		frame = new JFrame();
		frame.setBounds(100, 100, 940, 489);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new GridLayout(5, 0, 0, 0));
		
		JPanel panel_1 = new JPanel();
		frame.getContentPane().add(panel_1);
		
		JButton btnInputFile = new JButton("Choose input file");
		btnInputFile.addActionListener(event -> {
			mediator.chooseInputFile();
		});
		panel_1.add(btnInputFile);
		
		JLabel lblInputFile = new JLabel("New label");
		lblInputFile.setFont(new Font("SansSerif", Font.PLAIN, 16));
		panel_1.add(lblInputFile);
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel);
		
		JButton btnOutputFile = new JButton("Choose output file");
		panel.add(btnOutputFile);
		
		JLabel lblOutputFile = new JLabel("New label");
		panel.add(lblOutputFile);
		
		JPanel panel_3 = new JPanel();
		frame.getContentPane().add(panel_3);
		
		JButton btnPublicKey = new JButton("Choose public key");
		btnPublicKey.addActionListener(event -> {
			mediator.chooseKey(false);
		});
		panel_3.add(btnPublicKey);
		
		JLabel lblPublicKey = new JLabel("New label");
		panel_3.add(lblPublicKey);
		
		JPanel panel_4 = new JPanel();
		frame.getContentPane().add(panel_4);
		
		JButton btnPrivateKey = new JButton("Choose private key");
		panel_4.add(btnPrivateKey);
		
		JLabel lblPrivateKey = new JLabel("New label");
		panel_4.add(lblPrivateKey);
		
		JPanel panel_2 = new JPanel();
		frame.getContentPane().add(panel_2);
		
		JButton btnEncrypt = new JButton("Encrypt");
		btnEncrypt.addActionListener(event -> {
			mediator.encrypt();
		});
		panel_2.add(btnEncrypt);
		
		JButton btnDecrypt = new JButton("Decrypt");
		panel_2.add(btnDecrypt);
	}

}
