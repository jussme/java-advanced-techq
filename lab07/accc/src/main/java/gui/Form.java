package gui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.EmptyBorder;

public class Form extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private Map<String, RowPanel> fields = new TreeMap<>();

	private class RowPanel extends JPanel{
		private static final long serialVersionUID = 1L;
		private JTextField inputField;
		
		public RowPanel(String label) {
			add(new JLabel(label));
			add((inputField = new JTextField()));
			inputField.setPreferredSize(new Dimension(200, 30));
			
			setVisible(true);
		}
		
		public String getInput() {
			return inputField.getText().trim();
		}
	}
	
	/**
	 * Create the frame.
	 */
	public Form(List<String> form, Function<Map<String, String>, String> action) {
		try {
		    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
		        if ("Nimbus".equals(info.getName())) {
		            UIManager.setLookAndFeel(info.getClassName());
		            break;
		        }
		    }
		} catch (Exception e) {
		   
		}
		
		init(form, action);
		
		setVisible(true);
	}
	
	private void init(List<String> form, Function<Map<String, String>, String> action) {
		setBounds(100, 100, 683, 411);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(form.size() + 1, 0, 0, 0));
		
		for(String entry : form) {
			var row = new RowPanel(entry);
			fields.put(entry, row);
			contentPane.add(row);
		}
		
		var actionButton = new JButton("Perform");
		actionButton.addActionListener(event -> {
			var argMap = fields.entrySet().stream()
					.collect(Collectors.toMap(
							entry -> entry.getKey(),
							entry -> entry.getValue().getInput()));
			String retMessage = action.apply(argMap);
			if(retMessage == null) {
				JOptionPane.showMessageDialog(this,
						"Success",
						"Success",
						JOptionPane.INFORMATION_MESSAGE
				);
				this.dispose();
			} else {
				JOptionPane.showMessageDialog(this,
						retMessage,
						"Error",
						JOptionPane.ERROR_MESSAGE
				);
			}
		});
		contentPane.add(actionButton);
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}
}
