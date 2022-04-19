package com.mk.rmissl;

import java.io.IOException;
import java.net.Socket;
import java.rmi.server.RMIClientSocketFactory;

import javax.net.ssl.SSLSocketFactory;

public class CustomClientSocketFactory implements RMIClientSocketFactory{

	@Override
	public Socket createSocket(String host, int port) throws IOException {
		SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
		return factory.createSocket(host, port);
	}

}
