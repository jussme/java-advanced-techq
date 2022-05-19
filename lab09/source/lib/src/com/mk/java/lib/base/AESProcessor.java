package com.mk.java.lib.base;

import java.io.InputStream;
import java.io.OutputStream;
import java.security.Key;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.IvParameterSpec;

class AESProcessor {
	protected String transformation = "AES/CBC/PKCS5Padding";
	
	void encrypt(InputStream is, OutputStream os, Key publicKey) throws Exception{
		byte[] buff = new byte[1024];
		Cipher encryptCipher = Cipher.getInstance(transformation);
		encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey, SecureRandom.getInstanceStrong());
		os.write(encryptCipher.getIV());
		try (var cipherOutputStream = new CipherOutputStream(os, encryptCipher)) {
			int read;
			while((read = is.read(buff)) > 0) {
				cipherOutputStream.write(buff, 0, read);
			}
			cipherOutputStream.flush();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	void decrypt(InputStream is, OutputStream os, Key privateKey) throws Exception {
		byte[] buff = new byte[1024];
		Cipher encryptCipher = Cipher.getInstance(transformation);
		byte[] ivBytes = new byte[16];
		is.readNBytes(ivBytes, 0, 16);
		var ivSpec = new IvParameterSpec(ivBytes);
		encryptCipher.init(Cipher.DECRYPT_MODE, privateKey, ivSpec);
		
		try (var cipherOutputStream = new CipherOutputStream(os, encryptCipher)) {
			int read;
			while((read = is.read(buff)) > 0) {
				cipherOutputStream.write(buff, 0, read);
			}
			cipherOutputStream.flush();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}
