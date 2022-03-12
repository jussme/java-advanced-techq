package procurement;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JComponent;

import gui.RecordContainerPanel;
import gui.RepresentationFactory;
import gui.RepresentationFactory.RecordType;

public class Retriever {
	private final static String FILENAME_TEXT = "record.txt";
	private final static String FILENAME_IMAGE = "image.png";
	
	public static List<String> cd(Path dir) {
		var dirsAsFiles = Arrays.asList(dir.toFile().listFiles(file -> file.isDirectory()));
		return dirsAsFiles.stream()
				.map((dirAsFile) -> dirAsFile.toString())
				.collect(Collectors.toList());
	}
	
	public static List<RecordContainerPanel> retrieve(Path dir, Storage storage) {
		var returnList = new LinkedList<RecordContainerPanel>();
		for(RecordType type : RecordType.values()) {
			JComponent rep = storage.get(dir, type);
			if(rep != null) {
				returnList.add(new RecordContainerPanel(rep, true));
			} else {
				returnList.add(new RecordContainerPanel(
						RepresentationFactory.getRepresentationComponent(type, Paths.get(dir.toString(), type.toString())), false));				
			}
		}
	}
}
