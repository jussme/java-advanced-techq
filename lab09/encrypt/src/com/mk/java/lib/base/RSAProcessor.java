package com.mk.java.lib.base;

import java.io.InputStream;
import java.io.OutputStream;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.Cipher;

public class RSAProcessor extends Encoder{
	protected String transformation = "RSA/ECB/PKCS1Padding";
	
	@Override
	public void encrypt(InputStream is, OutputStream os, PublicKey publicKey) throws Exception{
		byte[] buff = new byte[245];
		Cipher encryptCipher = Cipher.getInstance(transformation);
		encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);
		while(is.read(buff) > 0) {
			var finl = encryptCipher.doFinal(buff);
			os.write(finl);
		}
		os.flush();
	}
	
	@Override
	public void decrypt(InputStream is, OutputStream os, PrivateKey privateKey) throws Exception {
		byte[] buff = new byte[256];
		Cipher encryptCipher = Cipher.getInstance(transformation);
		encryptCipher.init(Cipher.DECRYPT_MODE, privateKey);
		while(is.read(buff) > 0) {
			var fnl = encryptCipher.doFinal(buff);
			os.write(fnl);
		}
		os.flush();
	}
}
