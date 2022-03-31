package com.base;

import processing.Processor;
import processing.Status;
import processing.StatusListener;

public class Adder implements Processor{
	private final static int UNI_SLEEP = 1 * 1000;
	
	private Thread job;
	private int jobIdCounter = 0;
	private int result;
	
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
				String[] taskSplit = task.trim().split("\\s");
				result = Integer.valueOf(taskSplit[0]) + Integer.valueOf(taskSplit[taskSplit.length-1]);
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
		return "Adds x to y and returns the result. Pass \"x + y\"";
	}

	@Override
	public String getResult() {
		return Integer.toString(result);
	}

}
