package procurement;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JComponent;

import gui.RecordContainerPanel;
import gui.RepresentationFactory;
import gui.RepresentationFactory.RecordType;

public class Retriever {
	
	public static List<String> cd(Path dir) {
		List<String> returnList = new LinkedList<>();
		try(var dirs = Files.newDirectoryStream(dir, entry -> Files.isDirectory(entry))){
			for(Path dirInDir : dirs) {
				returnList.add(dirInDir.getFileName().toString());
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return returnList;
	}
	
	public static List<RecordContainerPanel> retrieve(Path dir, Storage storage) {
		var returnList = new LinkedList<RecordContainerPanel>();
		for(RecordType type : RecordType.values()) {
			JComponent rep = storage.get(dir, type);
			if(rep != null) {
				returnList.add(new RecordContainerPanel(rep, true));
			} else {
				rep = RepresentationFactory.getRepresentationComponent(type, Paths.get(dir.toString(), type.toString()));
				if (rep != null) {
					storage.register(dir, type, rep);
					returnList.add(new RecordContainerPanel(rep, false));
				}			
			}
		}
		return returnList;
	}
}
