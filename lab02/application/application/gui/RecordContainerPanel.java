package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;


/**
 * JPanel container used for wrapping custom/prebuild JComponent representations of records,
 * for it to be itself embedded in a DataWindow.
 * 
 * @author mk
 *
 */
public class RecordContainerPanel extends JPanel{
	private static final long serialVersionUID = 1L;

	private static final String MEMORY_LOAD = "Loaded from memory";
	private static final String DISK_RELOAD = "Reloaded from disk";
	
	private final JLabel label_loadMethod;
	
	public RecordContainerPanel(JComponent record, boolean loadedFromStorage) {
		this.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		this.setLayout(new BorderLayout(0, 0));
		
		label_loadMethod = new JLabel();
		label_loadMethod.setText(loadedFromStorage? MEMORY_LOAD : DISK_RELOAD);
		label_loadMethod.setHorizontalAlignment(SwingConstants.CENTER);
		label_loadMethod.setFont(new Font("Tahoma", Font.PLAIN, 14));
		this.add(label_loadMethod, BorderLayout.NORTH);
		
		this.add(record, BorderLayout.CENTER);
		
		this.setVisible(true);
	}
}
