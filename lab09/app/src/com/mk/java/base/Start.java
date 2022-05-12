package com.mk.java.base;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;

import com.mk.java.gui.AppWindow;
import com.mk.java.lib.base.Encoder;
import com.mk.java.lib.base.Helper;

public class Start {
	
	private static class DemoMediator implements AppWindow.Mediator {
		private Path inputFile;
		private Path outputFile;
		private Key privateKey;
		private Key publicKey;
		
		@Override
		public void chooseInputFile() {
			inputFile = AppWindow.chooseFile();
		}

		@Override
		public void chooseOutputFile() {
			outputFile = AppWindow.chooseFile();
		}

		@Override
		public void chooseKey(boolean privateKey) {
			outputFile = AppWindow.chooseFile();
			try {
				var key = Helper.getKey(outputFile.toString(), privateKey);
				if(privateKey) {
					this.privateKey = key;
				} else {
					publicKey = key;
				}
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidKeySpecException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		@Override
		public void encrypt() {
			try {
				Encoder.encrypt(inputFile.toString(), publicKey);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public void decrypt() {
			try {
				Encoder.decrypt(inputFile.toString(), privateKey);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public static void main(String[] args) throws NoSuchAlgorithmException {
		Security.setProperty("crypto.policy", "unlimited");
		int maxKeySize;
		try {
			maxKeySize = javax.crypto.Cipher.getMaxAllowedKeyLength("AES");
			System.out.println("Max Key Size for AES : " + maxKeySize);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
		generator.initialize(2048);
		KeyPair pair = generator.generateKeyPair();
		try (FileOutputStream fos = new FileOutputStream("public.key")) {
		    fos.write(pair.getPublic().getEncoded());
		} catch (Exception e) {
			e.printStackTrace();
		}
		new AppWindow(new DemoMediator());
	}
}
