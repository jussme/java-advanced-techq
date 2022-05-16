package com.mk.java.base;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;

import com.mk.java.gui.AppWindow;
import com.mk.java.lib.base.Helper;
import com.mk.java.lib.base.RSAProcessor;

public class Start {
	
	private static class DemoMediator implements AppWindow.Mediator {
		private Path inputFile;
		private Path outputFile;
		private PrivateKey privateKey;
		private PublicKey publicKey;
		
		@Override
		public String chooseInputFile() {
			inputFile = AppWindow.chooseFile();
			return inputFile.toString();
		}

		@Override
		public String chooseOutputFile() {
			outputFile = AppWindow.chooseFile();
			if(outputFile == null) {
				return null;
			}
			try {
				outputFile.toFile().createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
			return outputFile.toString();
		}

		@Override
		public String chooseKey(boolean privateKey) {
			var key = AppWindow.chooseFile();
			if(key == null) {
				return null;
			}
			try {
				if(privateKey) {
					this.privateKey = Helper.getPrivateKey(key.toString());
				} else {
					publicKey = Helper.getPublicKey(key.toString());
				}
				return publicKey.toString();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		private InputStream getFileInputStream(Path path) throws Exception{
			var file = path.toFile();
			file.createNewFile();
			return new FileInputStream(file);
		}
		
		private OutputStream getFileOutputStream(Path path) throws Exception{
			var file = path.toFile();
			return new FileOutputStream(file);
		}
		
		@Override
		public void encrypt() {
			try (var input = getFileInputStream(inputFile);
					var output = getFileOutputStream(outputFile)) {
				new RSAProcessor().encrypt(input, output, publicKey);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public void decrypt() {
			try (var input = getFileInputStream(inputFile);
					var output = getFileOutputStream(outputFile)) {
				new RSAProcessor().decrypt(input, output, privateKey);
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
