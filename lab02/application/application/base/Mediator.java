package base;

import java.awt.EventQueue;
import java.nio.file.Path;
import java.nio.file.Paths;

import gui.DataWindow;
import gui.RecordContainerPanel;
import procurement.Retriever;
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
						var dirs = Retriever.cd(tempPwd);
						if(dirs != null) {
							pwd = tempPwd;
							window.clearRecords();
							window.setDir(pwd.toString(), dirs);
							for(RecordContainerPanel panel : Retriever.retrieve(pwd, storage)) {
								window.renderRecord(panel);
							}
						}
					}, window -> {//jfilechooser
						pwd = DataWindow.chooseDir();
						if(pwd == null) {
							return;
						}
						var dirs = Retriever.cd(pwd);
						
						if(dirs != null) {
							window.clearRecords();
							window.setDir(pwd.toString(), dirs);
							for(RecordContainerPanel panel : Retriever.retrieve(pwd, storage)) {
								window.renderRecord(panel);
							}
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
