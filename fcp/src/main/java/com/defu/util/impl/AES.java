package com.defu.util.impl;

import java.security.SecureRandom;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.defu.util.ICrypto;

public class AES implements ICrypto {
	private static final String keyPwd = "likeajin@.com";

	@Override
	public String encrypt(String content) throws Exception{
		// TODO Auto-generated method stub
		content = content + (new Random().nextInt(9000) + 1000);
		KeyGenerator gen = KeyGenerator.getInstance("AES");
		gen.init(128, new SecureRandom(keyPwd.getBytes()));
		SecretKey k = gen.generateKey();
		byte[] f = k.getEncoded();
		SecretKeySpec sk = new SecretKeySpec(f, "AES");
		Cipher c = Cipher.getInstance("AES");
		c.init(Cipher.ENCRYPT_MODE, sk);
		byte[] x = c.doFinal(content.getBytes());
		return byteArray2String(x);
	}

	@Override
	public String decrypt(String content) throws Exception{
		// TODO Auto-generated method stub
		KeyGenerator gen = KeyGenerator.getInstance("AES");
		gen.init(128, new SecureRandom(keyPwd.getBytes()));
		SecretKey k = gen.generateKey();
		byte[] f = k.getEncoded();
		SecretKeySpec sk = new SecretKeySpec(f, "AES");
		Cipher c = Cipher.getInstance("AES");
		c.init(Cipher.DECRYPT_MODE, sk);
		byte[] x = String2byteArray(content);
		return new String(c.doFinal(x)).replaceAll(".{4}$", "");
	}

	private String byteArray2String(byte [] bits) {
		StringBuilder sb = new StringBuilder();
		for(byte b: bits) {
			sb.append((char)b);
		}
		return sb.toString();
	}
	
	private byte[] String2byteArray(String hex){
		char[] x = hex.toCharArray();
		byte[] r = new byte[x.length];
		for(int i = 0, il = x.length; i < il; i++) r[i] = (byte)x[i];
		return r;
	}
}
