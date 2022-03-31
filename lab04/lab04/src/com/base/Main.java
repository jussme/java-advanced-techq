package com.base;

import java.awt.EventQueue;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Map;
import java.util.TreeMap;

import processing.Processor;
import processing.Status;
import processing.StatusListener;

public class Main {

	private static class DemoMediator implements AppWindow.Mediator{
		private Loader loader;
		private Path choosenDir;
		private Map<String, Class<?>> canNameClass = new TreeMap<>();
		
		@Override
		public String loadClass() {
			if(choosenDir == null) {
				return null;
			}
			Path file = AppWindow.chooseFile(choosenDir.toFile());
			if(file == null) {
				return null;
			}
			String binCanonicalFileName = file.toString().substring(choosenDir.toString().length()+1);
			binCanonicalFileName = binCanonicalFileName.replace(FileSystems.getDefault().getSeparator(), ".");
			binCanonicalFileName = binCanonicalFileName.replace(".class", "");
			try {
				canNameClass.put(binCanonicalFileName, loader.loadClass(binCanonicalFileName));
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				return null;
			}
			return binCanonicalFileName;
		}


		@Override
		public void setDir() {
			choosenDir = AppWindow.chooseDir();
			if (choosenDir != null) {
				loader = new Loader(choosenDir);
			}
		}


		@Override
		public void unload(String cl) {
			loader = null;
			canNameClass.remove(cl);
			System.gc();
			loader = new Loader(choosenDir);
		}


		@Override
		public String getInfo(String cl) {
			try {
				Class<?> procClass = canNameClass.get(cl);
				Object procObj = procClass.getConstructor().newInstance(new Object[] {});
				Method getInfoMethod = procClass.getDeclaredMethod("getInfo");
				return (String)getInfoMethod.invoke(procObj);
			} catch (NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}


		@Override
		public void executeTask(String cl, String task, AppWindow th) {
			try {
				Class<?> procClass = canNameClass.get(cl);
				Object procObj = procClass.getConstructor().newInstance(new Object[] {});
				Method submitTask = procClass.getDeclaredMethod("submitTask", String.class, StatusListener.class);
				Method getResult = procClass.getDeclaredMethod("getResult");
				StatusListener sl = new StatusListener() {

					@Override
					public void statusChanged(Status s) {
						int progress = s.getProgress();
						th.setTaskProgress(progress);
						if(progress == 100) {
							try {
								th.setTaskResult((String)getResult.invoke(procObj));
							} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
								e.printStackTrace();
							}
						}
					}
					
				};
				submitTask.invoke(procObj, task, sl);
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}
			
		}
		
	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new AppWindow(new DemoMediator());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
