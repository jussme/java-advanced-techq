package com.base;

import java.awt.Font;
import java.awt.GridLayout;
import java.io.File;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.SwingConstants;

public class AppWindow {

	private JFrame frame;
	private final Mediator mediator;
	private JTextArea processorInfo;
	private JList<String> processors;
	private JTextField textFieldTask;
	private JTextField textFieldResult;
	private JLabel lblProgress;
	
	private List<String> classes = new LinkedList<>();
	
	public interface Mediator{
		String loadClass();
		void setDir();
		void unload(String cl);
		String getInfo(String cl);
		void executeTask(String cl, String task, AppWindow th);
	}

	public void setTaskResult(String result) {
		textFieldResult.setText(result);
	}
	
	public void setTaskProgress(int progress) {
		lblProgress.setText(Integer.toString(progress) + "%");
	}

	/**
	 * Create the application.
	 */
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
		this.mediator = mediator;
		initialize();
		this.frame.setVisible(true);
	}
	
	public void setProcessorInfo(String info) {
		this.processorInfo.setText(info);
	}
	
	public static Path chooseDir() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fileChooser.setAcceptAllFileFilterUsed(false);
		if(fileChooser.showOpenDialog(fileChooser) == JFileChooser.APPROVE_OPTION) {
			Path dir = fileChooser.getSelectedFile().toPath();
			return dir;
		}
		return null;
	}
	
	public static Path chooseFile(File baseDir) {
		JFileChooser fileChooser = new JFileChooser(baseDir);
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileChooser.setAcceptAllFileFilterUsed(false);
		if(fileChooser.showOpenDialog(fileChooser) == JFileChooser.APPROVE_OPTION) {
			Path file = fileChooser.getSelectedFile().toPath();
			return file;
		}
		return null;
	}
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 728, 481);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new GridLayout(0, 1, 0, 0));
		
		JPanel panel_2 = new JPanel();
		frame.getContentPane().add(panel_2);
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel);
		
		JButton btnDir = new JButton("Dir");
		btnDir.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnDir.addActionListener(event -> {
			mediator.setDir();
		});
		panel.add(btnDir);
		
		JButton btnFile = new JButton("File");
		btnFile.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnFile.addActionListener(event -> {
			String cl = mediator.loadClass();
			if(cl == null) {
				return;
			}
			classes.add(cl);
			processors.setListData(classes.toArray(new String[0]));
		});
		panel.add(btnFile);
		
		JPanel panel_1 = new JPanel();
		frame.getContentPane().add(panel_1);
		
		processors = new JList<>();
		processors.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		processors.setFont(new Font("Tahoma", Font.PLAIN, 16));
		processors.addListSelectionListener(event -> {
			if(!event.getValueIsAdjusting() && processors.getSelectedValue() != null) {
				processorInfo.setText(mediator.getInfo(processors.getSelectedValue()));
			}
		});
		panel_1.add(processors);
		
		processorInfo = new JTextArea();
		processorInfo.setEditable(false);
		processorInfo.setFont(new Font("Monospaced", Font.PLAIN, 16));
		panel_1.add(processorInfo);
		
		JButton btnUnload = new JButton("Unload");
		btnUnload.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnUnload.addActionListener(event -> {
			String cl;
			if((cl = processors.getSelectedValue()) != null) {
				classes.remove(cl);
				processors.setListData(classes.toArray(new String[0]));
				mediator.unload(cl);
			}
		});
		panel_1.add(btnUnload);
		
		JPanel panel_3 = new JPanel();
		frame.getContentPane().add(panel_3);
		
		textFieldTask = new JTextField();
		textFieldTask.setFont(new Font("Tahoma", Font.PLAIN, 16));
		panel_3.add(textFieldTask);
		textFieldTask.setColumns(10);
		
		JButton btnSubmit = new JButton("Submit");
		btnSubmit.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnSubmit.addActionListener(event -> {
			this.setTaskResult("");
			mediator.executeTask(processors.getSelectedValue(), textFieldTask.getText().trim(), this);
		});
		panel_3.add(btnSubmit);
		
		lblProgress = new JLabel("progress");
		lblProgress.setFont(new Font("Tahoma", Font.PLAIN, 16));
		panel_3.add(lblProgress);
		
		textFieldResult = new JTextField();
		textFieldResult.setHorizontalAlignment(SwingConstants.CENTER);
		textFieldResult.setEditable(false);
		textFieldResult.setFont(new Font("Tahoma", Font.PLAIN, 16));
		panel_3.add(textFieldResult);
		textFieldResult.setColumns(10);
		
		JPanel panel_4 = new JPanel();
		frame.getContentPane().add(panel_4);
	}

}
