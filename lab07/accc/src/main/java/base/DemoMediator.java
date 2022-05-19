package base;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import gui.AppWindow;
import gui.AppWindow.Mediator;
import gui.Form;
import hibernate.entities.BaseEntity;
import hibernate.entities.Event;
import hibernate.entities.Installment;

public class DemoMediator implements Mediator{
	
	public <T extends BaseEntity> void newEntity(Class<T> entityClass) {
		try{
			var entity = entityClass.getDeclaredConstructor().newInstance();
			List<String> form = entity.getProperties();
			form = form.subList(1, form.size());
			new Form(form, map -> {
				try {
					var ent = entityClass.getDeclaredConstructor(Map.class).newInstance(map);
					return "id: " + DatabaseWrapper.add(ent).getId();
				} catch (Exception e) {
					e.printStackTrace();
					return e.getMessage();
				}
			});
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public <T extends BaseEntity> boolean read(Class<T> entityClass) {
		try {
			List<List<String>> records;
			records = readFile(AppWindow.chooseFile());
			
			List<String> props = null;
			props = entityClass.getDeclaredConstructor().newInstance().getProperties();
			
			List<TreeMap<String, String>> initMaps = new ArrayList<TreeMap<String,String>>();
			for(List<String> record : records) {
				var map = new TreeMap<String, String>();
				initMaps.add(map);
				for(int it = 1; it < props.size(); ++it) {
					map.put(props.get(it), record.get(it-1));
				}
			}
			
			for(Map<String,String> initMap : initMaps) {
				var installment = entityClass.getDeclaredConstructor(Map.class)
					.newInstance(initMap);
				DatabaseWrapper.add(installment);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private List<List<String>> readFile(Path filename) throws FileNotFoundException, IOException {
		List<List<String>> records = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(filename.toString()))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		        String[] values = line.split(",");
		        records.add(Arrays.asList(values));
		    }
		}
		return records;
	}
}
