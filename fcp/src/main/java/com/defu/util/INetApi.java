package com.defu.util;

import java.io.IOException;
import java.net.UnknownHostException;

public interface INetApi {
	/**
	 * 将一个长整形数据转为一个ipv4字符串
	 * @param ipLong 一个合法的长整形数据
	 * @return ipv4字符串
	 * @throws IllegalArgumentException 如果参数不合法ipLong > 4294967295l || ipLong < 0，抛出异常
	 */
	public String long2ip(long ipLong) throws IllegalArgumentException;
	/**
	 * 将一个ipv4字符串转为一个长整形
	 * @param ip 合法的ipv4字符串
	 * @return 长整形
	 * @throws IllegalArgumentException 参数不合法，不是一个ipv4
	 * @throws NullPointerException 参数为空
	 * @throws NumberFormatException 参数不合法，不能转为整数
	 */
	public long ip2long(String ip) throws IllegalArgumentException, NullPointerException, NumberFormatException;
	/**
	 * 检测一个url是否可用
	 * @param url url
	 * @return 可用返回true，否则返回false
	 */
	public boolean checkUrl(String url);
	/**
	 * 获取一个url的ip
	 * @param url
	 * @return url的IP
	 * @throws UnknownHostException 不存在的host
	 */
	public String urlIp(String url) throws UnknownHostException;
	
	public String urlContent(String url, String encode) throws IOException;
	
}
