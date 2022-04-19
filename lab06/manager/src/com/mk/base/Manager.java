package com.mk.base;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.security.Policy;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.mk.rmissl.CustomClientSocketFactory;
import com.mk.rmissl.CustomServerSocketFactory;
import com.mk.rmissl.SSLUnicastRemoteObject;

import bilboards.IBillboard;
import bilboards.IManager;
import bilboards.Order;

public class Manager implements IManager{
	private final Map<Integer, IBillboard> hashBillboard = new TreeMap<>();
	private final Map<Integer, Boolean> hashRunning = new TreeMap<>();
	private final Map<Integer, Order> hashOrder = new TreeMap<>();
	private Window window;
	
	public static class BillboardInfo{
		public final int hash;
		public final int buffTotal;
		public final int buffFree;
		public final boolean running;
		
		public BillboardInfo(int hash, int buffTotal, int buffFree, boolean running) {
			this.hash = hash;
			this.buffTotal = buffTotal;
			this.buffFree = buffFree;
			this.running = running;
		}
		
		@Override
		public boolean equals(Object o) {
			if(! (o instanceof BillboardInfo)) {
				return false;
			}
			if(this.hash == ((BillboardInfo)o).hash) {
				return true;
			}
			return false;
		}
	}
	
	public static void main(String[] args) {
		//args[0] port
		//args[1] managerName
		try{
			var manager = new Manager(Integer.valueOf(args[0]), args[1]);
			var window = new Window(manager);
			manager.window = window;
		} catch (NumberFormatException e) {
			System.out.println("Wrong argument format: port, managerName");
			System.exit(1);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public BillboardInfo getBillboardInfo(int id) {
		int[] cap;
		try {
			cap = hashBillboard.get(id).getCapacity();
		} catch (RemoteException e) {
			e.printStackTrace();
			return null;
		}
		return new BillboardInfo(id, cap[0], cap[1], hashRunning.get(id));
	}
	
	public List<BillboardInfo> getBillboardsInfo() {
		var info = new ArrayList<BillboardInfo>();
		for(Entry<Integer, IBillboard> hashBillboard : hashBillboard.entrySet()) {
			try {
				var cap = hashBillboard.getValue().getCapacity();
				var id = hashBillboard.getKey();
				info.add(new BillboardInfo(id, cap[0], cap[1], hashRunning.get(id)));
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		return info;
	}
	
	public void setInterval(int id, int interval) {
		try {
			this.hashBillboard.get(id).setDisplayInterval(Duration.ofSeconds(interval));
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	public void startStop(int id) {
		var running = hashRunning.get(id);
		var billboard = hashBillboard.get(id);
		try {
			if(running) {
				billboard.stop();
				hashRunning.put(id, false);
			} else {
				billboard.start();
				hashRunning.put(id, true);
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	public Manager(int port, String managerName) throws Exception {
		Policy.setPolicy(new CustomPolicy());
		
		if(System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}
		
		try {
			share(port, managerName);
		} catch (RemoteException | AlreadyBoundException e) {
			throw e;
		}
	}
	
	private void share(int port, String managerName) throws Exception {
		var registry = LocateRegistry.createRegistry(port, new CustomClientSocketFactory(), new CustomServerSocketFactory());
		var unicastRemoteObject = new SSLUnicastRemoteObject();
		var managerStub = unicastRemoteObject.exportObject(this, 0);
		registry.bind(managerName, managerStub);
	}
	
	/**
	 * report that a billboard with the given id is running or not
	 * @param id
	 * @param running
	 */
	private void billboardRunning(int id, boolean running) {
		hashRunning.put(id, running);
		var billboard = hashBillboard.get(id);
		int[] capacityInfo;
		try {
			capacityInfo = billboard.getCapacity();
		} catch (RemoteException e) {
			e.printStackTrace();
			return;
		}
		window.updateBillboard(new BillboardInfo(id, capacityInfo[0], capacityInfo[1], running));
	}
	
	@Override
	public int bindBillboard(IBillboard billboard) throws RemoteException {
		int id = billboard.hashCode();
		if(hashBillboard.put(id, billboard) != null) {
			System.err.println("billboard replaced!: " + id);
		}
		billboard.start();
		billboardRunning(id, true);
		return id;
	}

	@Override
	public boolean unbindBillboard(int id) throws RemoteException {
		billboardRunning(id, false);
		window.removeBillboard(id);
		return hashBillboard.remove(id) == null? false : true;
	}

	@Override
	public boolean placeOrder(Order order) throws RemoteException {
		boolean foundOne = false;
		var orderId = order.hashCode();
		for(Entry<Integer, IBillboard> hashBillboard : hashBillboard.entrySet()) {
			var freeSpace = hashBillboard.getValue().getCapacity()[1];
			if(freeSpace != 0) {
				foundOne = true;
				hashBillboard.getValue().addAdvertisement(order.advertText, order.displayPeriod, orderId);
			}
		}
		if(foundOne) {
			hashOrder.put(orderId, order);
			order.client.setOrderId(orderId);
			return true;
		}
		return false;
	}

	@Override
	public boolean withdrawOrder(int orderId) throws RemoteException {
		for(Entry<Integer, IBillboard> hashBillboard : hashBillboard.entrySet()) {
			hashBillboard.getValue().removeAdvertisement(orderId);
		}
		hashOrder.remove(orderId);
		return true;
	}
}
