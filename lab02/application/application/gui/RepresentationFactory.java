package gui;

import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

public class RepresentationFactory {
	public enum RecordType {
		TEXT_PERSONAL("record.txt"),
		IMAGE_FACE("image.png");
		
		private String c;
		private RecordType(String c) {
			this.c = c;
		}
		@Override
		public String toString() {
			return c;
		}
	}
	
	public static JComponent getRepresentationComponent(RecordType type, Path file) {
		try {
			JComponent component = null;
			switch(type) {
			case TEXT_PERSONAL:
				component = getTextPersonalRepresentation(file);
			case IMAGE_FACE:
				component = getImageFaceRepresentation(file);
			}
			component.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
			return component;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	private static JComponent getTextPersonalRepresentation(Path file) throws IOException {
		var recordTextRep = new JTextPane();
		recordTextRep.setEditable(false);
		var textContents = Files.readString(file);
		recordTextRep.setText(textContents);
		return recordTextRep;
	}
	
	private static JComponent getImageFaceRepresentation(Path file) throws IOException {
		JLabel recordImageRep = new JLabel();
		recordImageRep.setFont(new Font("Tahoma", Font.PLAIN, 14));
		recordImageRep.setHorizontalAlignment(SwingConstants.CENTER);
		var bufferedImage = ImageIO.read(file.toFile());
		recordImageRep.setIcon(new ImageIcon(bufferedImage));
		return recordImageRep;
	}
}
