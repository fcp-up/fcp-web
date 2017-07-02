package com.defu.util;


public interface ICrypto {
	/**
	 * 加密
	 * @param content 原字符串
	 * @return 被加密的字符串
	 * @throws Exception
	 */
	String encrypt(String content) throws Exception;

	/**
	 * 解密
	 * @param content 被加密的字符串
	 * @return 原字符串
	 * @throws Exception
	 */
	String decrypt(String content) throws Exception;
	
}
