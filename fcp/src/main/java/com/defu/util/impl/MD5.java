package com.defu.util.impl;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.defu.util.IMD;

/**
 * MD5加密，支持16位和32位加密。默认32位加密，要换16位设置mode为2
 * @author glj
 *
 */
public class MD5 implements IMD {
	private static final char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
	
	/**
	 * 加密方式。1为32位加密，2位16位加密。
	 */
	protected int mode = 1;
	
	/**
	 * 默认构造器，加密方式为1，32位加密。
	 */
	public MD5(){
	}
	
	/**
	 * 指定加密方式构造
	 * @param mode 加密方式，1为32位加密，2为16位加密。
	 */
	public MD5(int mode) {
		this.mode = mode;
	}
	
	private String encrypt(byte[] bytes) throws NoSuchAlgorithmException {
		if(bytes != null) {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.reset();
			md.update(bytes);
			
			byte[] b = md.digest();
			char[] sb = new char[b.length * 2];
			
			int c = 0;
			for(byte x: b) {
				sb[c++] = hexDigits[x >>> 4 & 0xf];
				sb[c++] = hexDigits[x & 0xf];
			}
			
			return new String(sb);
		}
		return null;
	}
	
	@Override
	public String encrypt(String orgValue, String encode)
			throws NoSuchAlgorithmException, UnsupportedEncodingException {
		// TODO Auto-generated method stub
		if(orgValue != null) {
			orgValue = encrypt(orgValue.getBytes(encode));
			switch(mode) {
			case 1: return orgValue;
			case 2: return orgValue.substring(8, 24);
			}
		}
		
		return null;
	}

	public int getMode() {
		return mode;
	}

	/**
	 * 设置加密方式。1为32位加密，2为16位加密。
	 * @param mode 加密方式
	 */
	public void setMode(int mode) {
		this.mode = mode;
	}
	
	

}
