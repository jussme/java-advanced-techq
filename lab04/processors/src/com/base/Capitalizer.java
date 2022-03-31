package com.base;

import processing.Processor;
import processing.Status;
import processing.StatusListener;

public class Capitalizer implements Processor{
	private final static int UNI_SLEEP = 1 * 1000;
	
	private Thread job;
	private int jobIdCounter = 0;
	private String result;
	
	@Override
	public boolean submitTask(String task, StatusListener sl) {
		job = new Thread(() -> {
			try{
				sl.statusChanged(new Status(++jobIdCounter, 0));
				Thread.sleep(UNI_SLEEP);
				sl.statusChanged(new Status(jobIdCounter, 25));
				Thread.sleep(UNI_SLEEP);
				sl.statusChanged(new Status(jobIdCounter, 34));
				Thread.sleep(UNI_SLEEP);
				sl.statusChanged(new Status(jobIdCounter, 50));
				Thread.sleep(UNI_SLEEP);
				sl.statusChanged(new Status(jobIdCounter, 65));
				Thread.sleep(UNI_SLEEP);
				sl.statusChanged(new Status(jobIdCounter, 84));
				Thread.sleep(UNI_SLEEP);
				sl.statusChanged(new Status(jobIdCounter, 99));
				result = task.toUpperCase();
				Thread.sleep(UNI_SLEEP);
				sl.statusChanged(new Status(jobIdCounter, 100));
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (NumberFormatException ne) {
				ne.printStackTrace();
			}
		});
		job.start();
		return false;
	}

	@Override
	public String getInfo() {
		return "Capitalizes any passed string.";
	}

	@Override
	public String getResult() {
		return result;
	}

}
