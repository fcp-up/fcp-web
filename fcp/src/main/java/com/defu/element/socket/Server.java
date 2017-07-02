package com.defu.element.socket;

/**
 * socket server
 * @author glj
 * <pre>
 * 1.ip：服务器IP地址
 * 2.port：socket服务端监听端口
 * </pre>
 */
public class Server {
	/**
	 * 服务器IP
	 */
	private String ip;
	/**
	 * 端口
	 */
	private int port;
	
	public Server(){}
	
	public Server(String ip, int port) {
		this.ip = ip;
		this.port = port;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

}
