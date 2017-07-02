package com.defu.util;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

/**
 * 信息摘要，数据加密
 * @author glj
 *
 */
public interface IMD {
	/**
	 * 根据指定编码获取指定字符串的信息摘要，即加密
	 * @param orgValue 指定字符串
	 * @param encode 编码
	 * @return 加密后的字符串
	 * @throws NoSuchAlgorithmException 未找到对应加密算法，抛出异常
	 * @throws UnsupportedEncodingException 不支持指定编码，抛出异常
	 */
	public String encrypt(String orgValue, String encode) throws NoSuchAlgorithmException, UnsupportedEncodingException;
}
