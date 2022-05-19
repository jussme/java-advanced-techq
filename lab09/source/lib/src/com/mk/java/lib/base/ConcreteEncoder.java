package com.mk.java.lib.base;

import java.io.InputStream;
import java.io.OutputStream;
import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.SecretKey;

public class ConcreteEncoder extends Encoder{

	@Override
	public void encrypt(InputStream is, OutputStream os, Key key, Algorithm alg) throws Exception {
		switch(alg) {
		case RSA:
			new RSAProcessor().encrypt(is, os, (PublicKey) key);
			break;
		case AES:
			new AESProcessor().encrypt(is, os, (SecretKey) key);
			break;
		}
	}

	@Override
	public void decrypt(InputStream is, OutputStream os, Key key, Algorithm alg) throws Exception {
		switch(alg) {
		case RSA:
			new RSAProcessor().decrypt(is, os, (PrivateKey) key);
			break;
		case AES:
			new AESProcessor().decrypt(is, os, (SecretKey) key);
			break;
		}
	}

}
