package com.mk.rmissl;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.rmi.server.RMIServerSocketFactory;
import java.security.KeyStore;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocketFactory;

public class CustomServerSocketFactory implements RMIServerSocketFactory{
	private final SSLServerSocketFactory ssf;
	
	public CustomServerSocketFactory() throws Exception {
		SSLContext context;
		KeyManagerFactory keyManagerFactory;
		KeyStore keyStore;
		
		var pass = "pass123".toCharArray();
		keyStore = KeyStore.getInstance("JKS");
		keyStore.load(new FileInputStream("rmisslcert.jks"), pass);
		
		keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
		keyManagerFactory.init(keyStore, pass);
		
		context = SSLContext.getInstance("TLS");
		context.init(keyManagerFactory.getKeyManagers(), null, null);
		
		ssf = context.getServerSocketFactory();
	}
	
	@Override
	public ServerSocket createServerSocket(int port) throws IOException {
		return ssf.createServerSocket(port);
	}
	
	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		} else if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		return true;
	}
}
