package com.base;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Loader extends ClassLoader{
	private Path searchPath;
	
	public Loader(Path path) {
		if (!Files.isDirectory(path)) {
			throw new IllegalArgumentException("Not a directory");
		}
		searchPath = path;
	}
	
	public Class<?> findClass(String binFileName) throws ClassNotFoundException {
		Path classFile = Paths.get(searchPath + FileSystems.getDefault().getSeparator() +
				binFileName.replace(".", FileSystems.getDefault().getSeparator()) + ".class");
		byte[] buf;
		try {
			buf = Files.readAllBytes(classFile);
		} catch (IOException e) {
			throw new ClassNotFoundException("Couldnt find class in " + classFile, e);
		}

		return defineClass(binFileName, buf, 0, buf.length);
	}
}
