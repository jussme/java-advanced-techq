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
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;

public class DataWindow extends JFrame{
	private static final long serialVersionUID = 1L;

	private static final String TAB = "   ";
	
	private JFrame frmDirdiff;
	private JTextPane textPane;
	private JList<String> dirList;
	private JLabel lblPwd;
	private JLabel label_loadMethod;
	private JTextPane textPane_person;
	private JLabel label_image;
	
	public void setDir(String pwd, List<String> dirs) {
		this.lblPwd.setText(pwd.toString());
		this.dirList.setListData(buildDirsStrings(dirs));
	}
	
	private static String[] buildDirsStrings(List<String> dirs) {
		List<String> builder = new LinkedList<>();
		builder.add("..");
		builder.addAll(dirs);
		builder = builder.stream()
				.map(dirString -> TAB.concat(dirString))
				.collect(Collectors.toList());
		
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
	public DataWindow(BiConsumer<DataWindow, String> cd, Consumer<DataWindow> chooseDir) {
		try {
		    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
		        if ("Windows".equals(info.getName())) {
		            UIManager.setLookAndFeel(info.getClassName());
		            break;
		        }
		    }
		} catch (Exception e) {
		   
		}
		initialize(cd, chooseDir);
		this.frmDirdiff.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(BiConsumer<DataWindow, String> cd, Consumer<DataWindow> chooseDir) {
		frmDirdiff = new JFrame();
		frmDirdiff.setTitle("DirDiff");
		frmDirdiff.setBackground(Color.DARK_GRAY);
		frmDirdiff.getContentPane().setBackground(Color.DARK_GRAY);
		frmDirdiff.setBounds(100, 100, 922, 814);
		frmDirdiff.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{388, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 1.0, 1.0};
		frmDirdiff.getContentPane().setLayout(gridBagLayout);
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.DARK_GRAY);
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.weightx = 0.1;
		gbc_panel.weighty = 0.1;
		gbc_panel.insets = new Insets(0, 0, 5, 5);
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
		gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
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
		gbc_lblPwd.insets = new Insets(0, 0, 5, 5);
		gbc_lblPwd.gridx = 0;
		gbc_lblPwd.gridy = 1;
		frmDirdiff.getContentPane().add(lblPwd, gbc_lblPwd);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		GridBagConstraints gbc_scrollPane_1 = new GridBagConstraints();
		gbc_scrollPane_1.weighty = 0.35;
		gbc_scrollPane_1.insets = new Insets(0, 0, 5, 5);
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
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.insets = new Insets(0, 0, 5, 0);
		gbc_panel_1.weightx = 0.75;
		gbc_panel_1.gridheight = 4;
		gbc_panel_1.fill = GridBagConstraints.BOTH;
		gbc_panel_1.gridx = 1;
		gbc_panel_1.gridy = 0;
		//frmDirdiff.getContentPane().add(panel_1, gbc_panel_1);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		panel_1.add(panel_2, BorderLayout.NORTH);
		panel_2.setLayout(new BorderLayout(0, 0));
		
		label_loadMethod = new JLabel("Loaded from memory");
		label_loadMethod.setHorizontalAlignment(SwingConstants.CENTER);
		label_loadMethod.setFont(new Font("Tahoma", Font.PLAIN, 14));
		panel_2.add(label_loadMethod, BorderLayout.NORTH);
		
		JScrollPane scrollPane_2 = new JScrollPane();
		panel_2.add(scrollPane_2, BorderLayout.CENTER);
		
		JPanel panel_4 = new JPanel();
		panel_4.setLayout(new GridLayout(0, 1, 0, 0));
		scrollPane_2.setViewportView(panel_4);
		
		textPane_person = new JTextPane();
		textPane_person.setEditable(false);
		panel_4.add(textPane_person);
		
		JPanel panel_3 = new JPanel();
		panel_3.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		panel_1.add(panel_3, BorderLayout.CENTER);
		panel_3.setLayout(new GridLayout(0, 1, 0, 0));
		
		label_image = new JLabel("No image");
		label_image.setFont(new Font("Tahoma", Font.PLAIN, 14));
		label_image.setHorizontalAlignment(SwingConstants.CENTER);
		panel_3.add(label_image);
		
		JScrollPane scrollPane_3 = new JScrollPane();
		GridBagConstraints gbc_scrollPane_3 = new GridBagConstraints();
		gbc_scrollPane_3.gridheight = 4;
		gbc_scrollPane_3.insets = new Insets(0, 0, 0, 5);
		gbc_scrollPane_3.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_3.gridx = 1;
		gbc_scrollPane_3.gridy = 0;
		frmDirdiff.getContentPane().add(scrollPane_3, gbc_scrollPane_3);
		scrollPane_3.setViewportView(panel_1);
	}

}
