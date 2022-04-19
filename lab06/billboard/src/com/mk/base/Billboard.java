package com.mk.base;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import javax.rmi.ssl.SslRMIClientSocketFactory;

import com.mk.rmissl.SSLUnicastRemoteObject;

import bilboards.IBillboard;
import bilboards.IManager;

public class Billboard implements IBillboard{
	private final Buffer buff = new Buffer();
	private Duration interval = Duration.ofSeconds(5);
	private Window window;
	private final IManager manager;
	private final int id;
	private Thread switchThread;
	
	private class Announcement {
		public Duration duration;
		public int orderId;
		public String text;
		
		@Override
		public boolean equals(Object a) {
			if(!(a instanceof Announcement)) {
				return false;
			}
			return ((Announcement) a).orderId == this.orderId;
			
		}
	}

	private class Buffer {
		private final int maxSize = 3;
		private final List<Announcement> buff = new ArrayList<>();
		private int nextIndex = 0;
		
		public boolean safeAdd(Announcement a) {
			synchronized(this) {
				if(buff.size() < maxSize) {
					return buff.add(a);
				} else {
					return false;
				}
			}
		}
		
		public Announcement safeGetNext() {
			synchronized(this) {
				var returnVal = buff.get(nextIndex++);
				if(nextIndex > buff.size()-1) {
					nextIndex = 0;
				}
				return returnVal;
			}
		}
		
		public int safeSize() {
			synchronized(this) {
				return buff.size();
			}
		}
		
		public boolean safeRemove(int orderId) {//TODO SRP
			synchronized(this) {
				var dummyAnn = new Announcement();
				dummyAnn.orderId = orderId;
				int delIndex = -1;
				for(int it = 0; it < buff.size(); ++it) {
					if(buff.get(it).equals(dummyAnn)) {
						delIndex = it;
					}
				}
				if(delIndex != -1) {
					buff.remove(delIndex);
					//adaptacja licznika
					if(delIndex < nextIndex) {
						--nextIndex;
					} else {
						if(delIndex == buff.size()-1) {
							nextIndex = 0;
						}
					}
					return true;
				} else {
					return false;
				}
				
			}
		}
	}
	
	public static void main(String args[]) {
		
		try{
			var window = new Window();
			new Billboard(args[0], Integer.valueOf(args[1]), args[2], window);
		} catch (NumberFormatException e) {
			System.out.println("Wrong argument format: hostname, port, managerName");
			System.exit(1);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	private IManager getManager(String host, int port, String managerName) throws RemoteException, NotBoundException{
		try {
			var registry = LocateRegistry.getRegistry(host, port, new SslRMIClientSocketFactory());
			return (IManager) registry.lookup(managerName);
		} catch (RemoteException | NotBoundException e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	public IBillboard export() throws Exception {
		var ssluro = new SSLUnicastRemoteObject();
		return (IBillboard) ssluro.exportObject(this, 0);
	}
	
	public Billboard(String host, int port, String managerName, Window window) throws Exception {
		System.setProperty("javax.net.ssl.keyStore", "rmisslcert.jks");
		System.setProperty("javax.net.ssl.keyStorePassword", "pass123");
		System.setProperty("javax.net.ssl.trustStore", "rmisslcert.jks");
		System.setProperty("javax.net.ssl.trustStorePassword", "pass123");
		
		try{
			manager = getManager(host, port, managerName);
			this.window = window;
			
			id = manager.bindBillboard(export());
			
			Runtime.getRuntime().addShutdownHook(new Thread(() -> {
				try {
					manager.unbindBillboard(id);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}));
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public boolean addAdvertisement(String advertText, Duration displayPeriod, int orderId) throws RemoteException {
		var a = new Announcement();
		a.duration = displayPeriod;
		a.orderId = orderId;
		a.text = advertText;
		synchronized(buff) {
			return buff.safeAdd(a);
		}
	}

	@Override
	public boolean removeAdvertisement(int orderId) throws RemoteException {
		synchronized(buff){
			return buff.safeRemove(orderId);
		}
	}

	@Override
	public int[] getCapacity() throws RemoteException {
		var returnArr = new int[2];
		returnArr[0] = buff.maxSize;
		returnArr[1] = buff.maxSize - buff.safeSize();
		return returnArr;
	}

	@Override
	public void setDisplayInterval(Duration displayInterval) throws RemoteException {
		this.interval = displayInterval;
	}
	
	@Override
	public boolean start() throws RemoteException {
		if(switchThread != null && switchThread.isAlive()) {
			return false;
		}
		switchThread = new Thread(() -> {
			try {
				while(!Thread.interrupted()) {
					if(buff.safeSize() > 0) {
						var announcement = buff.safeGetNext();
						announcement.duration = announcement.duration.minus(interval);
						window.setBoardContents(announcement.text);
						if(announcement.duration.getSeconds() <= 0) {
							buff.safeRemove(announcement.orderId);
						}
					}
					Thread.sleep(interval.getSeconds() * 1000);
					window.setBoardContents("");
				}
			} catch (InterruptedException e) {
				System.err.println("Billboard sleep interval interrupted");
			}
		});
		
		switchThread.start();
		return false;
	}

	@Override
	public boolean stop() throws RemoteException {
		switchThread.interrupt();
		return true;
	}

}
