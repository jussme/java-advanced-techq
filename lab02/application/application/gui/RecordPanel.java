package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

public class RecordPanel extends JPanel{
	
	public RecordPanel(JComponent record) {
		this.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		this.setLayout(new BorderLayout(0, 0));
		
		var label_loadMethod = new JLabel("blank");
		label_loadMethod = new JLabel("Loaded from memory");
		label_loadMethod.setHorizontalAlignment(SwingConstants.CENTER);
		label_loadMethod.setFont(new Font("Tahoma", Font.PLAIN, 14));
		this.add(label_loadMethod, BorderLayout.NORTH);
		
		this.add(record, BorderLayout.CENTER);
	}
}
