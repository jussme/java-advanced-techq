package monitoring;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

import monitoring.Scanner.Change;

public class Status {
	private static final String DIR_START_TAG = "dir";
	private static final String FILE_START_TAG = "file";
	private static final String ROOT_START_TAG = "snapshot";
	private static final String FILE_NAME_START_TAG = "name";
	private static final String HASH_START_TAG = "hash";
	
	public final Map<String, String> filenamesToHashes = new TreeMap<>();
	public final List<String> subdirs = new ArrayList<>(15);
	
	public void writeToStream(OutputStream os) throws TransformerException {
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(this.toXML());
        StreamResult result = new StreamResult(os);

        transformer.transform(source, result);
	}
	
	private static String getMD5(Path file) {
		try (var bInputStream = new BufferedInputStream(Files.newInputStream(file));
				var digestInputStream = new DigestInputStream(bInputStream, null)){
			var messageDigest = MessageDigest.getInstance("MD5");
			digestInputStream.setMessageDigest(messageDigest);
			digestInputStream.readAllBytes();
			return Base64.getEncoder().encodeToString(messageDigest.digest());
		} catch(Exception e) {
			System.err.println("Can't calculate file " + file);
			e.printStackTrace();
		}
		return null;
	}
	
	private Document toXML() {
		try{
			DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = f.newDocumentBuilder();
			var doc = docBuilder.newDocument();
			
			var root = doc.createElement(ROOT_START_TAG);
			doc.appendChild(root);
			
			for(String subdir : subdirs) {
				var dirNode = doc.createElement(DIR_START_TAG);
				dirNode.appendChild(doc.createTextNode(subdir));
				root.appendChild(dirNode);
			}
			for(Entry<String, String> filenameToHash : filenamesToHashes.entrySet()) {
				var fileNode = doc.createElement(FILE_START_TAG);
				
				var filenameChildNode = doc.createElement(FILE_NAME_START_TAG);
				filenameChildNode.appendChild(doc.createTextNode(filenameToHash.getKey()));
				
				var hashChildNode = doc.createElement(HASH_START_TAG);
				hashChildNode.appendChild(doc.createTextNode(filenameToHash.getValue())); 
				
				fileNode.appendChild(filenameChildNode);
				fileNode.appendChild(hashChildNode);
				
				root.appendChild(fileNode);
			}
			
			return doc;
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Status(Path dirPath) {
		var dirFileRep = dirPath.toFile();
		for (File file : dirFileRep.listFiles((FileFilter)null)) {
			if (file.isDirectory()) {
				subdirs.add(file.getName());
			} else {
				if (file.isFile()) {
					String hash;
					filenamesToHashes.put(file.getName(), (hash = Status.getMD5(file.toPath())) != null? hash : "none");//aby byl event przy czytaniu hash'a z xml
					
				}
			}
		}
	}
	
	public Status(File statusFile) throws FileNotFoundException{
		var inputFactory = XMLInputFactory.newInstance();
		try(var fileInputStream = new FileInputStream(statusFile)){
			var eventReader = inputFactory.createXMLEventReader(fileInputStream);
			
			while(eventReader.hasNext()) {
				XMLEvent event = eventReader.nextEvent();
				
				if (event.isStartElement()) {
					switch(event.asStartElement().getName().getLocalPart()) {
					case DIR_START_TAG:
						subdirs.add(eventReader.nextEvent().asCharacters().getData());
						break;
					case FILE_START_TAG:
						eventReader.nextEvent();//tag nazwy
						String filename = eventReader.nextEvent().asCharacters().getData();
						eventReader.nextEvent();//end tag
						eventReader.nextEvent();//start tag/tag nazwy
						String hash = eventReader.nextEvent().asCharacters().getData();
						filenamesToHashes.put(filename, hash);
						break;
					}
				}
			}
		} catch (FileNotFoundException fe) {
			throw fe;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//TODO stream?
	public Map<String, Change> checkDirs(Status presentStatus) {
		Map<String, Change> changeMap = new TreeMap<>();
		for(String subdir : this.subdirs) {
			if (!presentStatus.subdirs.contains(subdir)) {
				changeMap.put(subdir, Change.DELETED);
			} else {
				changeMap.put(subdir, Change.NONE);
			}
		}
		for(String subdir : presentStatus.subdirs) {
			if (!this.subdirs.contains(subdir)) {
				changeMap.put(subdir, Change.CREATED);
			}
		}
		
		return changeMap;
	}
	
	public Map<String, Change> checkFiles(Status presentStatus) {
		Map<String, Change> changeMap = new TreeMap<>();
		var presentFsHs = presentStatus.filenamesToHashes;
		for(Entry<String, String> oldFH : this.filenamesToHashes.entrySet()) {
			if(presentFsHs.containsKey(oldFH.getKey())) {
				if(!presentFsHs.get(oldFH.getKey()).equals(oldFH.getValue())) {
					changeMap.put(oldFH.getKey(), Change.MODIFIED);
				} else {
					changeMap.put(oldFH.getKey(), Change.NONE);
				}
			} else {
				changeMap.put(oldFH.getKey(), Change.DELETED);
			}
		}
		var oldFsHs = this.filenamesToHashes;
		for(Entry<String, String> presentFH : presentFsHs.entrySet()) {
			if(!oldFsHs.containsKey(presentFH.getKey())) {
				changeMap.put(presentFH.getKey(), Change.CREATED);
			}
		}
		
		
		return changeMap;
	}
}
