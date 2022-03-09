package monitoring;

import java.nio.file.Path;
import java.util.Map;

import monitoring.Scanner.Change;

public class DiffRecord {
	public final Map<String, Change> filesDiff;
	public final Map<String, Change> dirDiff;
	public final Path pwd;
	
	public DiffRecord(Path pwd, Map<String, Change> filesDiff, Map<String, Change> dirDiff) {
		this.filesDiff = filesDiff;
		this.dirDiff = dirDiff;
		this.pwd = pwd;
	}
}
