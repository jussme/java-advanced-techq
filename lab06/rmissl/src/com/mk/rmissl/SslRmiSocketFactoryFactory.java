package com.mk.rmissl;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.security.KeyStore;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.rmi.ssl.SslRMIClientSocketFactory;
import javax.rmi.ssl.SslRMIServerSocketFactory;

public class SslRmiSocketFactoryFactory {
	public static SslRMIClientSocketFactory getClientSocketFactory(String keyStorePath, String keyStorePass,
			String trustStorePath, String trustStorePass) throws Exception{
		var context = SSLContext.getInstance("TLS");
		KeyManager[] keyManagers = null;
		if(keyStorePath != null && !keyStorePath.trim().isEmpty()) {
			keyManagers = getKeyManagers(keyStorePath, keyStorePass, "JKS", "SunX509");	
		}
		var trustManagers = getTrustManagers(trustStorePath, trustStorePass, "JKS", "SunX509");
		
		context.init(keyManagers, trustManagers, null);
		var factory = context.getSocketFactory();
		return new SslRMIClientSocketFactory() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public Socket createSocket(String host, int port) throws IOException{
				return factory.createSocket(host, port);
			}
		};
	}
	
	private static KeyManager[] getKeyManagers(String keyStorePath, String keyStorePass, String keyStoreType, String algorithm)
			throws Exception {
		KeyManagerFactory keyManagerFactory;
		KeyStore keyStore;
		
		keyStore = KeyStore.getInstance(keyStoreType);
		keyStore.load(new FileInputStream(keyStorePath), keyStorePass.toCharArray());
		
		keyManagerFactory = KeyManagerFactory.getInstance(algorithm);
		keyManagerFactory.init(keyStore, keyStorePass.toCharArray());
		
		return keyManagerFactory.getKeyManagers();
	}
	
	private static TrustManager[] getTrustManagers(String trustStorePath, String trustStorePass, String trustStoreType, String algorithm)
			throws Exception {
		TrustManagerFactory trustManagerFactory;
		KeyStore keyStore;
		
		keyStore = KeyStore.getInstance(trustStoreType);
		keyStore.load(new FileInputStream(trustStorePath), trustStorePass.toCharArray());
		
		trustManagerFactory = TrustManagerFactory.getInstance(algorithm);
		trustManagerFactory.init(keyStore);
		
		return trustManagerFactory.getTrustManagers();
	}
	
	public static SslRMIServerSocketFactory getServerSocketFactory(String keyStorePath, String keyStorePass, boolean authentication)
			throws Exception {
		var context = SSLContext.getInstance("TLS");
		var managers = getKeyManagers(keyStorePath, keyStorePass, "JKS", "SunX509");
		context.init(managers, null, null);
		
		return new SslRMIServerSocketFactory(context, null, null, authentication);
	}
}
