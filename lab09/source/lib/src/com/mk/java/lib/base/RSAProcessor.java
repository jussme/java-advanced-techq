package com.mk.java.lib.base;

import java.io.InputStream;
import java.io.OutputStream;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.Cipher;

class RSAProcessor{
	protected String transformation = "RSA/ECB/PKCS1Padding";
	
	void encrypt(InputStream is, OutputStream os, PublicKey publicKey) throws Exception{
		byte[] buff = new byte[245];
		//byte[] buff = new byte[256];
		
		Cipher encryptCipher = Cipher.getInstance(transformation);
		encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);
		/*try (var cipherOutputStream = new CipherOutputStream(os, encryptCipher)) {
			while(is.read(buff) > 0) {
				System.out.println(buff[0]);
				cipherOutputStream.write(buff);
			}
			cipherOutputStream.flush();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}*/
		int read;
		while((read = is.read(buff)) > 0) {
			System.out.println("read:" + read);
			var finl = encryptCipher.doFinal(buff, 0, read);
			System.out.println("write: " + finl.length);
			os.write(finl);
		}
		System.out.println("");
		os.flush();
	}
	
	void decrypt(InputStream is, OutputStream os, PrivateKey privateKey) throws Exception {
		byte[] buff = new byte[256];
		Cipher encryptCipher = Cipher.getInstance(transformation);
		encryptCipher.init(Cipher.DECRYPT_MODE, privateKey);
		/*try (var cipherOutputStream = new CipherOutputStream(os, encryptCipher)) {
			while(is.read(buff) > 0) {
				cipherOutputStream.write(buff);
			}
			cipherOutputStream.flush();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}*/
		int read;
		while((read = is.read(buff)) > 0) {
			System.out.println("read:" + read);
			var fnl = encryptCipher.doFinal(buff, 0, read);
			System.out.println("write: " + fnl.length);
			os.write(fnl);
		}
		os.flush();
	}
}
