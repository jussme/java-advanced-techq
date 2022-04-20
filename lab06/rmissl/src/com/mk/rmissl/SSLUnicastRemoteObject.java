package com.mk.rmissl;

import java.rmi.server.UnicastRemoteObject;

public class SSLUnicastRemoteObject extends UnicastRemoteObject{
	private static final long serialVersionUID = 1L;

	public SSLUnicastRemoteObject() throws Exception {
		//SslRMIClientSocketFactory clientf, SslRMIServerSocketFactory serverf
		super(0, SslRmiSocketFactoryFactory.getClientSocketFactory(null, null, "rmisslcert.jks", "pass123"),
				SslRmiSocketFactoryFactory.getServerSocketFactory("rmisslcert.jks", "pass123", true));
	}
}
