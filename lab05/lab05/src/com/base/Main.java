package com.base;

import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ServiceLoader;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import com.gui.AppWindow;
import com.gui.AppWindow.Mediator;

import ex.api.ClusterAnalysisService;
import ex.api.ClusteringException;
import ex.api.DataSet;

public class Main {

	private static class DemoMediator implements Mediator{
		private Path csv;
		private List<List<String>> table;
		private boolean processing;
		
		@Override
		public List<List<String>> loadCSV(Path file) {
			List<List<String>> records = new ArrayList<>();
			try (BufferedReader br = new BufferedReader(new FileReader(file.toFile()))) {
			    String line;
			    while ((line = br.readLine()) != null) {
			        String[] values = line.split(",");
			        records.add(Arrays.asList(values));
			    }
			} catch (IOException e) {
				e.printStackTrace();
			}
			csv = file;
			table = records;
			return records;
		}
		
		@Override
		public List<List<String>> reloadCSV() {
			return loadCSV(csv);
		}

		private DataSet buildDataSet() {
			DataSet dataSet = new DataSet();
			dataSet.setHeader(table.get(0).toArray(new String[0]));
			dataSet.setData(table.subList(1, table.size())
					.stream()
					.map(list -> list.toArray(new String[0]))
					.toArray(String[][]::new)
			);
			return dataSet;
		}
		
		@Override
		public void compileResults(Consumer<String> allResultsConsumer) {
			ServiceLoader<ClusterAnalysisService> loader = ServiceLoader.load(ClusterAnalysisService.class);
			if(loader.stream().count() == 0) {
				System.exit(1);
			}
			synchronized(this) {
				if(processing) {
					return;
				}
				processing = true;
			}
			new Thread(() -> {
				StringBuffer builder = new StringBuffer();
				loader.reload();
				var dataSet = buildDataSet();
				List<ClusterAnalysisService> services = loader.stream()
						.map(provider -> provider.get())
						.collect(Collectors.toList());
				for(ClusterAnalysisService service : services) {
					try {
						service.submit(dataSet);
					} catch (ClusteringException e) {
						e.printStackTrace();
					}
				}
				while(services.size() > 0) {
					var listIterator = services.listIterator();
					while(listIterator.hasNext()) {
						var service = listIterator.next();
						try {
							var returnData = service.retrieve(true);
							if(returnData != null) {
								builder.append(service.getName() + ": " + Arrays.stream(returnData.getData())
									.map(row -> List.of(row))
									.collect(Collectors.toList())  + "\n");
								allResultsConsumer.accept(builder.toString());
								listIterator.remove();
							}
						} catch(ClusteringException ce) {
							ce.printStackTrace();
						}
					}
					
					try{
						Thread.sleep(1000);
					} catch (InterruptedException ie) {
						ie.printStackTrace();
					}
				}
				
				synchronized(DemoMediator.this) {
					processing = false;
				}
			}).start();
			return;
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
