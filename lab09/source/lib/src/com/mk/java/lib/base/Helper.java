package com.mk.java.lib.base;

import java.io.File;
import java.nio.file.Files;
import java.security.Key;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class Helper {
	public enum KeyType {
		PUBLIC,
		PRIVATE,
		SECRET
	}
	
	public static PublicKey getPublicKey(String keyPath) throws Exception{
		return (PublicKey) getKey(keyPath, KeyType.PUBLIC);
	}
	
	public static PrivateKey getPrivateKey(String keyPath) throws Exception{
		return (PrivateKey) getKey(keyPath, KeyType.PRIVATE);
	}
	
	public static SecretKey getSecretKey(String keyPath) throws Exception {
		return (SecretKey) getKey(keyPath, KeyType.SECRET);
	}
	
	public static Key getKey(String keyPath, KeyType keyType) throws 
		Exception {
		File keyFile = new File(keyPath);
		byte[] keyBytes = Files.readAllBytes(keyFile.toPath());
		
		switch(keyType) {
		case PUBLIC:
			return getPublicRSA(keyBytes);
		case PRIVATE:
			return getPrivateRSA(keyBytes);
		case SECRET:
			return getSecretAES(keyBytes);
		}
		return null;
	}
	
	private static PublicKey getPublicRSA(byte[] keyBytes) throws Exception{
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		KeySpec keySpec = new X509EncodedKeySpec(keyBytes);
		return keyFactory.generatePublic(keySpec);
	}
	
	private static PrivateKey getPrivateRSA(byte[] keyBytes) throws Exception {
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		KeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
		return keyFactory.generatePrivate(keySpec);
	}
	
	private static SecretKey getSecretAES(byte[] keyBytes)  throws Exception {
		//SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("AES");
		KeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
		//return keyFactory.generateSecret(keySpec);
		return (SecretKey) keySpec;
	}
}
