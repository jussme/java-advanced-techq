package com.mk.rmissl;

import java.rmi.server.UnicastRemoteObject;

import javax.rmi.ssl.SslRMIClientSocketFactory;

public class SSLUnicastRemoteObject extends UnicastRemoteObject{
	private static final long serialVersionUID = 1L;

	public SSLUnicastRemoteObject() throws Exception {
		//super(0, new CustomClientSocketFactory(), new CustomServerSocketFactory());
		super(0, new SslRMIClientSocketFactory(), new CustomServerSocketFactory());
	}
}
