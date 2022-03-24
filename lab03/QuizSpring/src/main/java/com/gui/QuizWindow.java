package com.gui;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

public class QuizWindow{

	private JFrame frame;
	private JTextField textField;
	private JLabel lblQuestion;
	private JLabel lblAnswer;
	private JButton btnCheckAnswer;
	private JLabel lblConfirmation;

	public interface Mediator {
		public List<String> getCountries();
		public String[]	getLanguages();
		public void setLanguage(String lng);
		public String getPrefLanguage();
		public String getValue(String key);
		public boolean checkAnswer(int answer, int countryIndex) throws IllegalArgumentException;
	}
	
	/**
	 * Create the application.
	 */
	public QuizWindow(Mediator mediator) {
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

	private void reloadText(Mediator mediator) {
		lblQuestion.setText(mediator.getValue("question"));
		lblAnswer.setText(mediator.getValue("answer"));
		btnCheckAnswer.setText(mediator.getValue("checkAnswer"));
		lblConfirmation.setText("--");
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(Mediator mediator) {
		frame = new JFrame();
		frame.setBounds(100, 100, 838, 492);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new GridLayout(0, 1, 0, 0));
		
		JPanel panel = new JPanel();
		
		JComboBox<String> comboBoxLanguage = new JComboBox<>();
		var langs = mediator.getLanguages();
		var prefLang = mediator.getPrefLanguage();
		for(String lang : langs) {
			comboBoxLanguage.addItem(lang);
		}
		comboBoxLanguage.setSelectedItem(prefLang);
		comboBoxLanguage.addActionListener(new ActionListener( ) {

			@Override
			public void actionPerformed(ActionEvent e) {
				mediator.setLanguage((String) comboBoxLanguage.getSelectedItem());
				reloadText(mediator);
			}
			
		});
		comboBoxLanguage.setFont(new Font("Tahoma", Font.PLAIN, 16));
		
		JPanel panelLang = new JPanel();
		panelLang.add(comboBoxLanguage);
		frame.getContentPane().add(panelLang);
		
		frame.getContentPane().add(panel);
		
		lblQuestion = new JLabel();
		lblQuestion.setFont(new Font("Tahoma", Font.PLAIN, 16));
		panel.add(lblQuestion);
		
		JComboBox<String> comboBoxCountries = new JComboBox<>();
		comboBoxCountries.setFont(new Font("Tahoma", Font.PLAIN, 16));
		for(String country : mediator.getCountries()) {
			comboBoxCountries.addItem(country);
		}
		panel.add(comboBoxCountries);
		
		JPanel panel_2 = new JPanel();
		frame.getContentPane().add(panel_2);
		
		lblAnswer = new JLabel();
		lblAnswer.setFont(new Font("Tahoma", Font.PLAIN, 16));
		panel_2.add(lblAnswer);
		
		textField = new JTextField();
		textField.setHorizontalAlignment(SwingConstants.CENTER);
		textField.setFont(new Font("Tahoma", Font.PLAIN, 16));
		panel_2.add(textField);
		textField.setColumns(10);
		
		btnCheckAnswer = new JButton();
		btnCheckAnswer.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnCheckAnswer.addActionListener(event -> {
			int answer = -1;
			try{
				answer = Integer.valueOf(textField.getText());
			} catch (Exception e) {
				return;
			}
			if (mediator.checkAnswer(answer, comboBoxCountries.getSelectedIndex())) {//poprawna odpowiedz
				lblConfirmation.setText(String.format(
						mediator.getValue("right"), (String)comboBoxCountries.getSelectedItem(), answer));
			} else {//niepoprawna odpowiedz
				lblConfirmation.setText(mediator.getValue("wrong"));
			}
		});
		panel_2.add(btnCheckAnswer);
		
		JPanel panel_1 = new JPanel();
		frame.getContentPane().add(panel_1);
		
		lblConfirmation = new JLabel();
		lblConfirmation.setFont(new Font("Tahoma", Font.PLAIN, 16));
		panel_1.add(lblConfirmation);
		
		reloadText(mediator);
	}

}
