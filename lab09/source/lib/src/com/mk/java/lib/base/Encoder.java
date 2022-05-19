package com.mk.java.lib.base;

import java.io.InputStream;
import java.io.OutputStream;
import java.security.Key;

public abstract class Encoder {
	protected String transformation;
	
	public enum Algorithm {
		RSA,
		AES
	}
	
	public abstract void encrypt(InputStream is, OutputStream os,
			Key key, Algorithm alg) throws Exception;
	public abstract void decrypt(InputStream is, OutputStream os,
			Key key, Algorithm alg) throws Exception;
}
