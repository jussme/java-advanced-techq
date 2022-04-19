package com.mk.base;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.time.Duration;

import javax.rmi.ssl.SslRMIClientSocketFactory;

import com.mk.rmissl.SSLUnicastRemoteObject;

import bilboards.IClient;
import bilboards.IManager;
import bilboards.Order;

public class Client implements IClient{
	private final IManager manager;
	private final OrderIdReporter reporter;
	private final IClient stub;
	
	private static class OrderIdReporter{
		private ClientWindow window = null;
		
		private void reportId(int id) {
			window.reportId(id);
		}
		private void delId(int id) {
			window.delId(id);
		}
	}

	@Override
	public void setOrderId(int orderId) throws RemoteException {
		reporter.reportId(orderId);
	}

	public void withdrawOrder(String orderId) {
		try {
			var orderIdInt = Integer.valueOf(orderId);
			if(manager.withdrawOrder(orderIdInt)) {
				reporter.delId(orderIdInt);
			}
		} catch (NumberFormatException e) {
			System.err.println("Order format exception: withdrawOrder");
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String args[]) {
		
		try{
			var orderIdReporter = new OrderIdReporter();
			var client = new Client(args[0], Integer.valueOf(args[1]), args[2], orderIdReporter);
			var window = new ClientWindow(client);
			orderIdReporter.window = window;
		} catch (NumberFormatException e) {
			System.out.println("Wrong argument format: hostname, port, managerName");
			System.exit(1);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public IClient export() throws Exception {
		var ssluro = new SSLUnicastRemoteObject();
		return (IClient) ssluro.exportObject(this, 0);
	}
	
	public boolean placeOrder(String text, String duration) {
		var order = new Order();
		order.advertText = text;
		order.client = stub;
		try{
			order.displayPeriod = Duration.ofSeconds(Integer.valueOf(duration));
		} catch (NumberFormatException e) {
			System.err.println("number format exception: duration");
			return false;
		}
		try {
			return manager.placeOrder(order);
		} catch (RemoteException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public Client(String host, int port, String managerName, OrderIdReporter reporter) throws Exception {
		try{
			manager = getManager(host, port, managerName);
			this.reporter = reporter;
			stub = export();
		} catch (Exception e) {
			throw e;
		}
	}
	
	private IManager getManager(String host, int port, String managerName) throws RemoteException, NotBoundException{
		System.setProperty("javax.net.ssl.keyStore", "rmisslcert.jks");
		System.setProperty("javax.net.ssl.keyStorePassword", "pass123");
		System.setProperty("javax.net.ssl.trustStore", "rmisslcert.jks");
		System.setProperty("javax.net.ssl.trustStorePassword", "pass123");
		
		try {
			var registry = LocateRegistry.getRegistry(host, port, new SslRMIClientSocketFactory());
			return (IManager) registry.lookup(managerName);
		} catch (RemoteException | NotBoundException e) {
			e.printStackTrace();
			throw e;
		}
	}
}
