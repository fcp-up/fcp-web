package com.defu.util.impl;

import com.defu.util.ICrypto;


public class Base64 implements ICrypto {
	public String encrypt(String content) {
		return new String(org.apache.commons.codec.binary.Base64.encodeBase64(content.getBytes()));
	}
	public String decrypt(String content) {
		return new String(org.apache.commons.codec.binary.Base64.decodeBase64(content.getBytes()));
	}
}
