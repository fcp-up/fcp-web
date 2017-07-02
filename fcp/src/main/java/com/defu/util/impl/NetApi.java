package com.defu.util.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;

import com.defu.util.INetApi;

public class NetApi implements INetApi {

	@Override
	public String long2ip(long ipLong) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		if (ipLong > 4294967295l || ipLong < 0)
			throw new IllegalArgumentException("参数不合法");

		StringBuilder sb = new StringBuilder();

		boolean apd = false;
		long x = 0xFFFFFFFFl;
		for (int i = 3; i > -1; i--) {
			if (apd)
				sb.append(".");
			apd = true;
			sb.append((ipLong & x) >>> i * 8);
			x = x >>> 8;
		}

		return sb.toString();
	}

	@Override
	public long ip2long(String ip) throws IllegalArgumentException,
			NullPointerException, NumberFormatException {
		// TODO Auto-generated method stub
		String[] ips = ip.split("\\.");
		if (ips.length != 4)
			throw new IllegalArgumentException("参数不合法");
		long ii = 0;
		long x;
		for (int i = 0, il = ips.length; i < il; i++) {
			x = Long.parseLong(ips[i]);
			if (x > 255 || x < 0)
				throw new IllegalArgumentException("参数不合法");
			ii += x << (3 - i) * 8;
		}
		return ii;
	}

	@Override
	public boolean checkUrl(String url) {
		// TODO Auto-generated method stub
		int count = 0;
		URL u = null;
		try {
			u = new URL(url);
			HttpURLConnection c;
			while (count < 5) {
				try {
					c = (HttpURLConnection) u.openConnection();
					c.setConnectTimeout(2000);
					c.setReadTimeout(2000);
					if (c.getResponseCode() == 200)
						return true;
				} catch (Exception ex) {
					count++;
				}
			}
		} catch (Exception ex) {
		}
		return false;
	}

	@Override
	public String urlIp(String url) throws UnknownHostException {
		// TODO Auto-generated method stub
		url = url.replaceAll("(?i)^https?://", "");
		url = url.replaceAll("(:\\d+)?(/.*)?$", "");
		InetAddress adr = InetAddress.getByName(url);
		return adr.getHostAddress();
	}

	@Override
	public String urlContent(String urlStr, String encode) throws IOException {
		URL url = new URL(urlStr);
		HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
		InputStreamReader input = new InputStreamReader(httpConn.getInputStream(), encode);
		BufferedReader bufReader = new BufferedReader(input);
		String line = "";
		boolean append = false;
		StringBuilder contentBuf = new StringBuilder();
		while ((line = bufReader.readLine()) != null) {
			if(append) contentBuf.append("\r");
			append = true;
			contentBuf.append(line);
		}
		return contentBuf.toString();
	}
	
}
