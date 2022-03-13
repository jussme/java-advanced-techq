package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
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

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.EtchedBorder;

public class DataWindow extends JFrame{
	private static final long serialVersionUID = 1L;

	private static final String TAB = "   ";
	
	private JFrame frmDirdiff;
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
	}
	
	public void clearRecords() {
		synchronized(recordsContainer.getTreeLock()) {
			for(Component child : recordsContainer.getComponents()) {
				recordsContainer.remove(child);
			}
		}
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
		gridBagLayout.columnWeights = new double[]{0.0, 1.0};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 1.0, 1.0};
		frmDirdiff.getContentPane().setLayout(gridBagLayout);
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.DARK_GRAY);
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.insets = new Insets(8, 8, 0, 5);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 0;
		frmDirdiff.getContentPane().add(panel, gbc_panel);
		panel.setLayout(new BorderLayout(0, 0));
		
		JButton btnChooseDir = new JButton("Choose dir");
		btnChooseDir.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnChooseDir.addActionListener(event -> chooseDir.accept(this));
		panel.add(btnChooseDir);
		GridBagConstraints gbc_lblPwd = new GridBagConstraints();
		gbc_lblPwd.insets = new Insets(0, 8, 0, 5);
		gbc_lblPwd.gridx = 0;
		gbc_lblPwd.gridy = 1;
		JPanel bufPanel = new JPanel();
		
		bufPanel.setBackground(Color.DARK_GRAY);
		frmDirdiff.getContentPane().add(bufPanel, gbc_lblPwd);
		bufPanel.setLayout(new GridLayout(0, 1, 0, 0));
		
		lblPwd = new JTextArea();
		lblPwd.setText("pwd");
		lblPwd.setFont(new Font("Monospaced", Font.PLAIN, 15));
		lblPwd.setForeground(Color.GREEN);
		lblPwd.setBackground(Color.DARK_GRAY);
		lblPwd.setLineWrap(true);
		lblPwd.setEditable(false);
		bufPanel.add(lblPwd);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.getVerticalScrollBar().setUnitIncrement(16);
		scrollPane_1.getHorizontalScrollBar().setUnitIncrement(16);
		GridBagConstraints gbc_scrollPane_1 = new GridBagConstraints();
		gbc_scrollPane_1.gridheight = 2;
		gbc_scrollPane_1.insets = new Insets(0, 8, 8, 5);
		gbc_scrollPane_1.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_1.gridx = 0;
		gbc_scrollPane_1.gridy = 2;
		frmDirdiff.getContentPane().add(scrollPane_1, gbc_scrollPane_1);
		GridBagConstraints gbc_textPane = new GridBagConstraints();
		gbc_textPane.insets = new Insets(0, 0, 5, 0);
		gbc_textPane.fill = GridBagConstraints.BOTH;
		gbc_textPane.gridx = 0;
		gbc_textPane.gridy = 3;
		
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
		
		recordsContainer = new JPanel();
		recordsContainer.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.insets = new Insets(0, 0, 5, 0);
		gbc_panel_1.weightx = 0.75;
		gbc_panel_1.gridheight = 4;
		gbc_panel_1.fill = GridBagConstraints.BOTH;
		gbc_panel_1.gridx = 1;
		gbc_panel_1.gridy = 0;
		recordsContainer.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		var recordsScrollPane = new JScrollPane();
		recordsScrollPane.getVerticalScrollBar().setUnitIncrement(16);
		recordsScrollPane.getHorizontalScrollBar().setUnitIncrement(16);
		GridBagConstraints gbc_scrollPane_3 = new GridBagConstraints();
		gbc_scrollPane_3.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_3.insets = new Insets(8, 0, 8, 8);
		gbc_scrollPane_3.gridheight = 4;
		gbc_scrollPane_3.gridx = 1;
		gbc_scrollPane_3.gridy = 0;
		frmDirdiff.getContentPane().add(recordsScrollPane, gbc_scrollPane_3);
		recordsScrollPane.setViewportView(recordsContainer);
	}

}
