package com.mk.java.lib.base;

import java.io.InputStream;
import java.io.OutputStream;
import java.security.PrivateKey;
import java.security.PublicKey;

public abstract class Encoder {
	protected String transformation;
	
	public abstract void encrypt(InputStream is, OutputStream os, PublicKey publicKey) throws Exception;
	public abstract void decrypt(InputStream is, OutputStream os, PrivateKey privateKey) throws Exception;
}
