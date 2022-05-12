package com.mk.java.lib.base;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

public class Helper {
	public static Key getKey(String keyPath, boolean privateKey) throws 
	IOException, NoSuchAlgorithmException, InvalidKeySpecException {
		File publicKeyFile = new File(keyPath);
		byte[] publicKeyBytes = Files.readAllBytes(publicKeyFile.toPath());
		
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes, "RSA");
		return privateKey? keyFactory.generatePrivate(publicKeySpec)
				: keyFactory.generatePublic(publicKeySpec);
	}
}
