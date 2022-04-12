package com.mk.base;

import java.rmi.RemoteException;
import java.time.Duration;

import bilboards.IBillboard;

public class Billboard implements IBillboard{

	@Override
	public boolean addAdvertisement(String advertText, Duration displayPeriod, int orderId) throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeAdvertisement(int orderId) throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int[] getCapacity() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setDisplayInterval(Duration displayInterval) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean start() throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean stop() throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

}
