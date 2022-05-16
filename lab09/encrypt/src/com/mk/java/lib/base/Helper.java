package com.mk.java.lib.base;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class Helper {
	public static PublicKey getPublicKey(String keyPath) throws Exception{
		return (PublicKey) getKey(keyPath, false);
	}
	
	public static PrivateKey getPrivateKey(String keyPath) throws Exception{
		return (PrivateKey) getKey(keyPath, true);
	}
	
	private static Key getKey(String keyPath, boolean privateKey) throws 
	IOException, NoSuchAlgorithmException, InvalidKeySpecException {
		File publicKeyFile = new File(keyPath);
		byte[] publicKeyBytes = Files.readAllBytes(publicKeyFile.toPath());
		
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		KeySpec publicKeySpec = privateKey? new PKCS8EncodedKeySpec(publicKeyBytes)
				: new X509EncodedKeySpec(publicKeyBytes);
		return privateKey? keyFactory.generatePrivate(publicKeySpec)
				: keyFactory.generatePublic(publicKeySpec);
	}
}
