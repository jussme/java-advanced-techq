package com.mk.java.lib.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.Key;

import javax.crypto.Cipher;

public class Encoder {
	
	
	public static void encrypt(String filePath, Key publicKey) throws Exception {
		var file = new File(filePath);
		var outputFile = new File("encrypted_output");
		try(var fileInputStream = new FileInputStream(file);
			var encryptedOutputStream = new FileOutputStream(outputFile)) {
			byte[] buff = new byte[1024];
			Cipher encryptCipher = Cipher.getInstance("RSA");
			encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);
			while(fileInputStream.read(buff) != -1) {
				encryptedOutputStream.write(encryptCipher.update(buff));
			}
			encryptCipher.doFinal();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	public static void decrypt(String filePath, Key privateKey) throws Exception {
		var file = new File(filePath);
		var outputFile = new File("decrypted_output");
		try(var fileInputStream = new FileInputStream(file);
			var decryptedOutputStream = new FileOutputStream(outputFile)) {
			byte[] buff = new byte[1024];
			Cipher encryptCipher = Cipher.getInstance("RSA");
			encryptCipher.init(Cipher.DECRYPT_MODE, privateKey);
			while(fileInputStream.read(buff) != -1) {
				decryptedOutputStream.write(encryptCipher.update(buff));
			}
			encryptCipher.doFinal();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}
