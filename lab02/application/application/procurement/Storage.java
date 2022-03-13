package procurement;

import java.nio.file.Path;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map;
import java.util.Map.Entry;
import java.util.WeakHashMap;

import javax.swing.JComponent;

import gui.RepresentationFactory.RecordType;

public class Storage {
	private final Map<Entry<Path, RecordType>, JComponent> fileToRepresentation = new WeakHashMap<>();
	
	public JComponent get(Path dir, RecordType type) {
		return fileToRepresentation.get(new SimpleEntry<>(dir, type));
	}
	
	public JComponent register(Path dir, RecordType type, JComponent component) {
		return fileToRepresentation.put(new SimpleEntry<>(dir, type), component);
	}
}
