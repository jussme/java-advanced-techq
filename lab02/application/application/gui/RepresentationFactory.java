package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

/**
 * Factory supplying basic JComponent representations of text and image record files 
 * 
 * @author mk
 *
 */
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
				break;
			case IMAGE_FACE:
				component = getImageFaceRepresentation(file);
				break;
			}
			component.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
			return component;
		} catch (NoSuchFileException fe) {//no file corresponding to the type
			System.err.println("NoSuchFileException");
		} catch (IIOException ie) {//no image file corresponding to the type
			System.err.println("IIOException");
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
		recordTextRep.setFont(recordTextRep.getFont().deriveFont(16f));
		return recordTextRep;
	}
	
	private static JComponent getImageFaceRepresentation(Path file) throws IOException {
		final int maxWidth = 600, maxHeight = 600;
		
		JLabel recordImageRep = new JLabel();
		recordImageRep.setFont(new Font("Tahoma", Font.PLAIN, 14));
		recordImageRep.setHorizontalAlignment(SwingConstants.CENTER);
		var bufferedImage = ImageIO.read(file.toFile());
		if(bufferedImage != null) {
			var dim = getScaledDimensions(bufferedImage.getWidth(), bufferedImage.getHeight(), maxWidth, maxHeight);
			bufferedImage = scaleImage(bufferedImage, dim.width, dim.height);
			recordImageRep.setIcon(new ImageIcon(bufferedImage));	
			return recordImageRep;
		}
		throw new IOException();
	}
	
	private static BufferedImage scaleImage(BufferedImage originalImage, int width, int height) {
		Image resultingImage = originalImage.getScaledInstance(width, height, Image.SCALE_DEFAULT);
	    BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	    outputImage.getGraphics().drawImage(resultingImage, 0, 0, null);
	    return outputImage;
	}
	
	private static Dimension getScaledDimensions(int width, int height, int maxWidth, int maxHeight) {
		int targetWidth, targetHeight;
		if(height > maxHeight) {//resize?
			targetWidth = (int)(width * maxHeight/(float)height);
			targetHeight = maxHeight;
		} else {
			targetHeight = height;
		}
		if(width > maxWidth) {
			targetHeight = (int)(height * maxWidth/(float)width);
			targetWidth = maxWidth;
		} else {
			targetWidth = width;
		}
		return new Dimension(targetWidth, targetHeight);
	}
}
