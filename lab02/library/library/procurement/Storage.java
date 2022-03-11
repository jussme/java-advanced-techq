package procurement;

import java.nio.file.Path;
import java.util.Map;
import java.util.WeakHashMap;

import javax.swing.JComponent;

public class Storage {
	private final Map<Path, Record> map = new WeakHashMap<>();
	
}
