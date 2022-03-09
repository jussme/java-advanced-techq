package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import monitoring.Scanner.Change;

public class DiffWindow extends JFrame{
	private static final long serialVersionUID = 1L;

	private static final String TAB = "  ";
	
	private JFrame frmDirdiff;
	private JTextPane textPane;
	private JList<String> dirList;
	private JLabel lblPwd;
	
	public void setDir(String pwd, Map<String, Change> filesChanged, Map<String, Change> dirs) {
		this.textPane.setText(buildFilesString(filesChanged));
		this.lblPwd.setText(pwd.toString());
		this.dirList.setListData(buildDirsStrings(dirs));
	}
	
	private static String buildFilesString(Map<String, Change> filesChanged) {
		StringBuilder sb = new StringBuilder();
		
		for(Entry<String, Change> entry : filesChanged.entrySet()) {
			sb.append("\n" + TAB);
			switch(entry.getValue()) {
			case DELETED:
				sb.append("- ");
				break;
			case MODIFIED:
				sb.append("* ");
				break;
			case CREATED:
				sb.append("+ ");
				break;
			case NONE:
				sb.append(TAB);
				break;
			}
			sb.append(entry.getKey());
		}
		
		return sb.toString();
	}
	
	private static String[] buildDirsStrings(Map<String, Change> dirs) {
		List<String> builder = new LinkedList<>();
		builder.add(TAB + TAB + "..");
		for(Entry<String, Change> dirEntry : dirs.entrySet()) {
			switch(dirEntry.getValue()) {
			case DELETED:
				builder.add(TAB + "- " + dirEntry.getKey());
				break;
			case MODIFIED://nie ustawiane
				builder.add(TAB + "* " + dirEntry.getKey());
				break;
			case CREATED:
				builder.add(TAB + "+ " + dirEntry.getKey());
				break;
			case NONE:
				builder.add(TAB + TAB + dirEntry.getKey());
				break;
			}
		}
		
		return builder.toArray(new String[0]);
	}
	
	private String getChoosenDir() {
		var tab = dirList.getSelectedValue().split("\\s");
		return tab[tab.length-1];
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

	/**
	 * Create the application.
	 */
	public DiffWindow(BiConsumer<DiffWindow, String> cd, Consumer<DiffWindow> chooseDir) {
		try {
		    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
		        if ("Windows".equals(info.getName())) {
		            UIManager.setLookAndFeel(info.getClassName());
		            break;
		        }
		    }
		} catch (Exception e) {
		    // If Nimbus is not available, you can set the GUI to another look and feel.
		}
		initialize(cd, chooseDir);
		this.frmDirdiff.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(BiConsumer<DiffWindow, String> cd, Consumer<DiffWindow> chooseDir) {
		frmDirdiff = new JFrame();
		frmDirdiff.setTitle("DirDiff");
		frmDirdiff.setBackground(Color.DARK_GRAY);
		frmDirdiff.getContentPane().setBackground(Color.DARK_GRAY);
		frmDirdiff.setBounds(100, 100, 453, 814);
		frmDirdiff.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWeights = new double[]{1.0};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 1.0, 1.0};
		frmDirdiff.getContentPane().setLayout(gridBagLayout);
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.DARK_GRAY);
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.weightx = 0.1;
		gbc_panel.weighty = 0.1;
		gbc_panel.insets = new Insets(0, 0, 5, 0);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 0;
		frmDirdiff.getContentPane().add(panel, gbc_panel);
		panel.setLayout(new BorderLayout(0, 0));
		
		JButton btnChooseDir = new JButton("Choose dir");
		btnChooseDir.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnChooseDir.addActionListener(event -> chooseDir.accept(this));
		panel.add(btnChooseDir);
		
		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.weighty = 0.55;
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 3;
		scrollPane.getVerticalScrollBar().setUnitIncrement(16);
		scrollPane.getHorizontalScrollBar().setUnitIncrement(16);
		
		lblPwd = new JLabel("pwd");
		lblPwd.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblPwd.setForeground(Color.GREEN);
		lblPwd.setBackground(Color.DARK_GRAY);
		GridBagConstraints gbc_lblPwd = new GridBagConstraints();
		gbc_lblPwd.insets = new Insets(0, 0, 5, 0);
		gbc_lblPwd.gridx = 0;
		gbc_lblPwd.gridy = 1;
		frmDirdiff.getContentPane().add(lblPwd, gbc_lblPwd);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		GridBagConstraints gbc_scrollPane_1 = new GridBagConstraints();
		gbc_scrollPane_1.weighty = 0.35;
		gbc_scrollPane_1.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane_1.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_1.gridx = 0;
		gbc_scrollPane_1.gridy = 2;
		frmDirdiff.getContentPane().add(scrollPane_1, gbc_scrollPane_1);
		frmDirdiff.getContentPane().add(scrollPane, gbc_scrollPane);
		
		JPanel nestedPanel = new JPanel();
		nestedPanel.setBackground(Color.DARK_GRAY);
		GridBagConstraints gbc_nestedPanel = new GridBagConstraints();
		gbc_nestedPanel.fill = GridBagConstraints.BOTH;
		gbc_nestedPanel.gridx = 0;
		gbc_nestedPanel.gridy = 4;
		scrollPane.add(nestedPanel, gbc_nestedPanel);
		nestedPanel.setLayout(new GridLayout(1, 0, 0, 0));
		scrollPane.setViewportView(nestedPanel);
		
		textPane = new JTextPane();
		textPane.setFont(new Font("SansSerif", Font.PLAIN, 16));
		textPane.setForeground(Color.GREEN);
		textPane.setBackground(Color.DARK_GRAY);
		textPane.setEditable(false);
		GridBagConstraints gbc_textPane = new GridBagConstraints();
		gbc_textPane.insets = new Insets(0, 0, 5, 0);
		gbc_textPane.fill = GridBagConstraints.BOTH;
		gbc_textPane.gridx = 0;
		gbc_textPane.gridy = 3;
		nestedPanel.add(textPane);
		
		dirList = new JList<>();
		dirList.setFont(new Font("SansSerif", Font.PLAIN, 14));
		dirList.setForeground(Color.GREEN);
		dirList.setBackground(Color.DARK_GRAY);
		dirList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		dirList.addListSelectionListener(event -> {
			if(!event.getValueIsAdjusting() && dirList.getSelectedValue() != null) {
				cd.accept(this, getChoosenDir());
			}
		});
		GridBagConstraints gbc_dirList = new GridBagConstraints();
		gbc_dirList.fill = GridBagConstraints.BOTH;
		gbc_dirList.gridx = 0;
		gbc_dirList.gridy = 3;
		scrollPane_1.setViewportView(dirList);
	}

}
