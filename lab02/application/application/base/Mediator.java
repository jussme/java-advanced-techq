package base;

import java.awt.EventQueue;
import java.awt.Font;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import javax.swing.JLabel;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;

import gui.DataWindow;
import gui.RepresentationFactory;
import procurement.Storage;

public class Mediator {
	private Path pwd;
	private final Storage storage = new Storage();
	
	
	
	public void showWindow() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new DataWindow((window, input) -> {//cd
						Path tempPwd = input.equals("..")? pwd.getParent() : Paths.get(pwd.toString(), input);
						if(!tempPwd.toFile().exists()) {
							return;
						}
						pwd = tempPwd;
						var containerPanelText = new RecordContainerPanel(RepresentationFactory.getRepresentationComponent(null, tempPwd));
						var containerPanelText = new RecordContainerPanel(RepresentationFactory.getRepresentationComponent(null, tempPwd));
						if(diffRecord != null) {
							window.setDir(diffRecord.pwd.toString(), diffRecord.filesDiff, diffRecord.dirDiff);
						}
					}, window -> {//wybor folderu w dialogu
						
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
