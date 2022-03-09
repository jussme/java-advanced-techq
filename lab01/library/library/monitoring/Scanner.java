package monitoring;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

import javax.swing.JOptionPane;
import javax.xml.transform.TransformerException;

public class Scanner {
	private final static String LOG_FILE_NAME = ".diffDirStatus";
	
	private static Path checkPathIsDir(Path path) {
		if(!path.toFile().exists()) {//jezeli nie istnieje
			return null;
		}
		if(!path.toFile().isDirectory()) {//jezeli nie jest folderem
			if(!path.toFile().getParentFile().isDirectory()) {//jezeli rodzic sciezki nie jest folderem
				return null;
			} else {//jezeli rodzic sciezki jest folderem
				path = path.toFile().getParentFile().toPath();				
			}
		}
		return path;
	}
	
	public static DiffRecord cd(Path path) {
		path = checkPathIsDir(path);
		if(path == null) {
			return null;
		}
		
		Status oldStatus = null;
		Status newStatus = new Status(path);
		Path filePath = Paths.get(path.toString(), FileSystems.getDefault().getSeparator() + LOG_FILE_NAME);
		try{
			oldStatus = new Status(filePath.toFile());//rzuci wjatek jezeli nie ma pliku
			logDirStatus(path, newStatus);
			return new DiffRecord(path, oldStatus.checkFiles(newStatus), oldStatus.checkDirs(newStatus));
		} catch (FileNotFoundException fe) {
			//dir bez historii, diff bez zmian
			logDirStatus(path, newStatus);
			return new DiffRecord(path,
					newStatus.filenamesToHashes.keySet().stream().collect(Collectors.toMap(filename -> filename, hash -> Change.NONE)),
					newStatus.subdirs.stream().collect(Collectors.toMap(dirName -> dirName, none -> Change.NONE))
			);
		}
	}
	
	private static void logDirStatus(Path dir, Status status) {
		dir = checkPathIsDir(dir);
		if(dir == null) {
			return;
		}
		Path filePath = Paths.get(dir.toString(), FileSystems.getDefault().getSeparator() + LOG_FILE_NAME);
		try{
			createHiddenFile(filePath);
			writeStatusToFile(filePath, status);
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Could not save directory status.", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private static void writeStatusToFile(Path file, Status status) throws IOException, TransformerException {
		if(!file.toFile().exists() || !file.toFile().isFile()) {//jezeli nie istnieje
			throw new FileNotFoundException();
		}
		try(var fileOutputStream = new BufferedOutputStream(Files.newOutputStream(file))){
			status.writeToStream(fileOutputStream);
		} catch(Exception e) {
			throw e;
		}
	}
	
	private static void createHiddenFile(Path path) throws IOException {
		if (path.toFile().exists() && path.toFile().isFile()) {
			return;
		}
		Files.createFile(path);
		Files.setAttribute(path, "dos:hidden", true);
	}
	
	public enum Change {
		NONE,
		DELETED,
		CREATED,
		MODIFIED;
	}
}
