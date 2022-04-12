package com.mk.base;

import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;
import java.util.TreeMap;

import bilboards.IBillboard;
import bilboards.IManager;
import bilboards.Order;

public class Manager implements IManager{
	private final Map<Integer, IBillboard> hashBillboards = new TreeMap<>();
	
	
	public static void main(String[] args) {
		
	}
	
	public Manager(int port, String managerName) {
		try {
			share(port, managerName);
		} catch (RemoteException | AlreadyBoundException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	private void share(int port, String managerName) throws AccessException, RemoteException, AlreadyBoundException {
		var registry = LocateRegistry.createRegistry(port);
		var managerStub = UnicastRemoteObject.exportObject(this, 0);
		registry.bind(managerName, managerStub);
	}

	@Override
	public int bindBillboard(IBillboard billboard) throws RemoteException {
		int id = billboard.hashCode();
		if(hashBillboards.put(id, billboard) != null) {
			System.err.println("billboard replaced!: " + id);
		}
		return id;
	}

	@Override
	public boolean unbindBillboard(int billboardId) throws RemoteException {
		return hashBillboards.remove(billboardId) == null? false : true;
	}

	@Override
	public boolean placeOrder(Order order) throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean withdrawOrder(int orderId) throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}
}
