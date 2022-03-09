package base;

import java.awt.EventQueue;
import java.nio.file.Path;
import java.nio.file.Paths;

import gui.DiffWindow;
import monitoring.Scanner;

public class Mediator {
	private Path pwd;
	
	public void showWindow() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new DiffWindow((window, input) -> {
						//System.out.println(input);
						Path tempPwd = input.equals("..")? pwd.getParent() : Paths.get(pwd.toString(), input);
						if(!tempPwd.toFile().exists()) {
							return;
						}
						pwd = tempPwd;
						var diffRecord = Scanner.cd(pwd);
						if(diffRecord != null) {
							window.setDir(diffRecord.pwd.toString(), diffRecord.filesDiff, diffRecord.dirDiff);
						}
					}, window -> {
						
						pwd = DiffWindow.chooseDir();
						if(pwd == null) {
							return;
						}
						var diffRecord = Scanner.cd(pwd);
						if(diffRecord != null) {
							window.setDir(diffRecord.pwd.toString(), diffRecord.filesDiff, diffRecord.dirDiff);
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
