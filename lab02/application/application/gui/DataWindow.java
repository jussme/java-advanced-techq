package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
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

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.EtchedBorder;

public class DataWindow extends JFrame{
	private static final long serialVersionUID = 1L;

	private static final String TAB = "   ";
	
	private JFrame frame;
	private JList<String> dirList;
	private JPanel recordsContainer;
	private JTextArea lblPwd;
	
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
		var dir = dirList.getSelectedValue().trim();
		return dir;
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
	
	public void renderRecord(RecordContainerPanel recordPanel) {
		recordsContainer.add(recordPanel);
		recordsContainer.repaint();
		recordsContainer.revalidate();
		frame.validate();
	}
	
	public void clearRecords() {
		recordsContainer.removeAll();
		recordsContainer.repaint();
		recordsContainer.revalidate();
		frame.validate();
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
		this.frame.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(BiConsumer<DataWindow, String> cd, Consumer<DataWindow> chooseDir) {
		frame = new JFrame();
		frame.setTitle("Data");
		frame.setBackground(Color.DARK_GRAY);
		frame.getContentPane().setBackground(Color.DARK_GRAY);
		frame.setBounds(100, 100, 1088, 814);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new GridLayout(0, 1, 0, 0));
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 8));
		frame.getContentPane().add(splitPane);
		
		JPanel panel_1 = new JPanel();
		panel_1.setForeground(Color.GREEN);
		panel_1.setBackground(Color.DARK_GRAY);
		splitPane.setLeftComponent(panel_1);
		GridBagLayout gbl_panel_1 = new GridBagLayout();
		gbl_panel_1.columnWidths = new int[]{0, 0};
		gbl_panel_1.rowHeights = new int[]{0, 0, 0, 0, 0};
		gbl_panel_1.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panel_1.rowWeights = new double[]{0.0, 0.0, 1.0, 1.0, Double.MIN_VALUE};
		panel_1.setLayout(gbl_panel_1);
		
		JPanel panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.insets = new Insets(0, 0, 0, 8);
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 0;
		panel_1.add(panel, gbc_panel);
		panel.setBackground(Color.DARK_GRAY);
		panel.setLayout(new BorderLayout(0, 0));
		
		JButton btnChooseDir = new JButton("Choose dir");
		btnChooseDir.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnChooseDir.addActionListener(event -> chooseDir.accept(this));
		panel.add(btnChooseDir);
		JPanel bufPanel = new JPanel();
		GridBagConstraints gbc_bufPanel = new GridBagConstraints();
		gbc_bufPanel.insets = new Insets(0, 0, 0, 8);
		gbc_bufPanel.gridx = 0;
		gbc_bufPanel.gridy = 1;
		panel_1.add(bufPanel, gbc_bufPanel);
		
		bufPanel.setBackground(Color.DARK_GRAY);
		bufPanel.setLayout(new BorderLayout(0, 0));
		
		lblPwd = new JTextArea();
		lblPwd.setColumns(23);
		lblPwd.setLineWrap(true);
		lblPwd.setTabSize(2);
		lblPwd.setText("pwd");
		lblPwd.setFont(new Font("Monospaced", Font.PLAIN, 15));
		lblPwd.setForeground(Color.GREEN);
		lblPwd.setBackground(Color.DARK_GRAY);
		lblPwd.setEditable(false);
		bufPanel.add(lblPwd);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		GridBagConstraints gbc_scrollPane_1 = new GridBagConstraints();
		gbc_scrollPane_1.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_1.gridheight = 2;
		gbc_scrollPane_1.insets = new Insets(0, 0, 0, 8);
		gbc_scrollPane_1.gridx = 0;
		gbc_scrollPane_1.gridy = 2;
		panel_1.add(scrollPane_1, gbc_scrollPane_1);
		scrollPane_1.getVerticalScrollBar().setUnitIncrement(16);
		scrollPane_1.getHorizontalScrollBar().setUnitIncrement(16);
		
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
		scrollPane_1.setViewportView(dirList);
		
		recordsContainer = new JPanel();
		recordsContainer.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
		recordsContainer.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		var recordsScrollPane = new JScrollPane();
		splitPane.setRightComponent(recordsScrollPane);
		recordsScrollPane.getVerticalScrollBar().setUnitIncrement(16);
		recordsScrollPane.getHorizontalScrollBar().setUnitIncrement(16);
		recordsScrollPane.setViewportView(recordsContainer);
		GridBagConstraints gbc_textPane = new GridBagConstraints();
		gbc_textPane.insets = new Insets(0, 0, 5, 0);
		gbc_textPane.fill = GridBagConstraints.BOTH;
		gbc_textPane.gridx = 0;
		gbc_textPane.gridy = 3;
		GridBagConstraints gbc_dirList = new GridBagConstraints();
		gbc_dirList.fill = GridBagConstraints.BOTH;
		gbc_dirList.gridx = 0;
		gbc_dirList.gridy = 3;
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.insets = new Insets(0, 0, 5, 0);
		gbc_panel_1.weightx = 0.75;
		gbc_panel_1.gridheight = 4;
		gbc_panel_1.fill = GridBagConstraints.BOTH;
		gbc_panel_1.gridx = 1;
		gbc_panel_1.gridy = 0;
	}

}
