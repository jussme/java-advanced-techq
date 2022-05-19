package com.mk.java.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.nio.file.Path;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.EtchedBorder;

import com.mk.java.lib.base.Encoder.Algorithm;
import com.mk.java.lib.base.Helper.KeyType;

public class AppWindow {

	private JFrame frame;
	
	static public interface Mediator {
		String chooseInputFile();
		String chooseOutputFile();
		String chooseKey(KeyType keyType);
		void encrypt(Algorithm alg);
		void decrypt(Algorithm alg);
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
		frame.setBounds(100, 100, 573, 489);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new GridLayout(6, 0, 0, 0));
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
		frame.getContentPane().add(panel_1);
		
		JLabel lblInputFile = new JLabel("");
		JButton btnInputFile = new JButton("Choose input file");
		btnInputFile.addActionListener(event -> {
			var file = mediator.chooseInputFile();
			lblInputFile.setText(file);
		});
		panel_1.setLayout(new BorderLayout(0, 0));
		panel_1.add(btnInputFile, BorderLayout.NORTH);
		
		lblInputFile.setHorizontalAlignment(SwingConstants.CENTER);
		lblInputFile.setFont(new Font("SansSerif", Font.PLAIN, 12));
		panel_1.add(lblInputFile, BorderLayout.CENTER);
		
		JPanel panel = new JPanel();
		panel.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
		frame.getContentPane().add(panel);
		
		JLabel lblOutputFile = new JLabel("");
		JButton btnOutputFile = new JButton("Choose output file");
		btnOutputFile.addActionListener(event -> {
			var file = mediator.chooseOutputFile();
			lblOutputFile.setText(file);
		});
		panel.setLayout(new BorderLayout(0, 0));
		panel.add(btnOutputFile, BorderLayout.NORTH);
		
		lblOutputFile.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(lblOutputFile);
		
		JPanel panel_3 = new JPanel();
		panel_3.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
		frame.getContentPane().add(panel_3);
		
		JLabel lblPublicKey = new JLabel("");
		JButton btnPublicKey = new JButton("Choose encryption key");
		btnPublicKey.addActionListener(event -> {
			var key = mediator.chooseKey(KeyType.PUBLIC);
			lblPublicKey.setText(key);
		});
		panel_3.setLayout(new BorderLayout(0, 0));
		panel_3.add(btnPublicKey, BorderLayout.NORTH);
		
		lblPublicKey.setHorizontalAlignment(SwingConstants.CENTER);
		panel_3.add(lblPublicKey);
		
		JPanel panel_4 = new JPanel();
		panel_4.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
		frame.getContentPane().add(panel_4);
		
		JLabel lblPrivateKey = new JLabel("");
		JButton btnPrivateKey = new JButton("Choose decryption key");
		btnPrivateKey.addActionListener(event -> {
			var key = mediator.chooseKey(KeyType.PRIVATE);
			lblPrivateKey.setText(key);
		});
		panel_4.setLayout(new BorderLayout(0, 0));
		panel_4.add(btnPrivateKey, BorderLayout.NORTH);
		
		lblPrivateKey.setHorizontalAlignment(SwingConstants.CENTER);
		panel_4.add(lblPrivateKey);
		
		JPanel panel_5 = new JPanel();
		panel_5.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
		frame.getContentPane().add(panel_5);
		panel_5.setLayout(new BorderLayout(0, 0));

		JLabel lblSecretKey = new JLabel("");
		JButton btnSecretKey = new JButton("Choose secret key");
		btnSecretKey.addActionListener(event -> {
			var key = mediator.chooseKey(KeyType.SECRET);
			lblSecretKey.setText(key);
		});
		panel_5.add(btnSecretKey, BorderLayout.NORTH);
		
		lblSecretKey.setHorizontalAlignment(SwingConstants.CENTER);
		panel_5.add(lblSecretKey, BorderLayout.CENTER);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
		frame.getContentPane().add(panel_2);
		
		JButton btnEncryptRSA = new JButton("Encrypt RSA");
		btnEncryptRSA.addActionListener(event -> {
			mediator.encrypt(Algorithm.RSA);
		});
		panel_2.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panel_2.add(btnEncryptRSA);
		
		JButton btnDecryptRSA = new JButton("Decrypt RSA");
		btnDecryptRSA.addActionListener(event -> {
			mediator.decrypt(Algorithm.RSA);
		});
		
		JButton btnEncryptAES = new JButton("Encrypt AES");
		btnEncryptAES.addActionListener(event -> {
			mediator.encrypt(Algorithm.AES);
		});
		panel_2.add(btnEncryptAES);
		panel_2.add(btnDecryptRSA);
		
		JButton btnDecryptAES = new JButton("Decrypt AES");
		btnDecryptAES.addActionListener(event -> {
			mediator.decrypt(Algorithm.AES);
		});
		panel_2.add(btnDecryptAES);
	}

}
