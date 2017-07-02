package com.defu.element.socket;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * socket连接线程
 * @author glj<pre>
 * 1.一旦启动一个socket连接线程，就开始连接socket服务器。
 * 2.如果提供连接配置，则连接失败会根据配置尝试，直到连上或超时。否则只会连接一次。
 * 3.socket连接上服务器后，将执行managConnectedSocket方法。
 * 
 * 本线程不主动关闭socket
 * </pre>
 * @see Configuration
 * @see Server
 */
public abstract class ConnectThread implements Runnable {
	/**
	 * 初始化异常代码
	 */
	public final static int INITEXCEPTION = 1;
	/**
	 * 连接异常代码
	 */
	public final static int CONNECTEXCEPTION = 2;

	protected Socket so;
	protected Server sv;
	protected Configuration cfg;

	/**
	 * 处理异常
	 * @param exceptionCode 异常代码
	 * 1.INITEXCEPTION 初始化异常：初始化线程出错时抛出。
	 * 2.CONNECTEXCEPTION 连接异常：连接异常时抛出。
	 * @param ex 异常对象
	 */
	public abstract void processException(int exceptionCode, Exception ex);
	/**
	 * 使用已经连接的socket
	 * @param so 已经连接的socket
	 */
	public abstract void managConnectedSocket(Socket so);
	
	/**
	 * 构造socket连接线程
	 * @param server socket服务端
	 * @param config socket连接配置
	 * @see Server
	 * @see Configuration
	 */
	public ConnectThread(Server server, Configuration config) {
		sv = server;
		cfg = config;
	}
	
	private Socket createSocket() {
		Socket so = new Socket();
		try {
			so.setKeepAlive(true);
			so.setTcpNoDelay(!cfg.isNagle());
		}
		catch(Exception ex) {
		}
		return so;
	}
	
	public Socket getSocket(){
		return so;
	}
	
	private void connect(SocketAddress sa, int acquireTimeout, int acquireRetryDelay) {
		try {
			if(acquireTimeout > 0) so.connect(sa, acquireTimeout);
			else so.connect(sa);
		}
		catch(Exception ex) {
			processException(CONNECTEXCEPTION, ex);
			if(acquireRetryDelay > 0)
				try {
					Thread.sleep(acquireRetryDelay);
				}
				catch(Exception eex){}
		}
	}
	
	private void connectByCfg(SocketAddress sa) {
		try {
			if(cfg.getTimeout() > 0) so.setSoTimeout(cfg.getTimeout());
		}
		catch(Exception ex){}
		int ac = cfg.getAcquireRetryAttempts(), acquireTimeout = cfg.getAcquireTimeout(), delay = cfg.getAcquireRetryDelay();
		if(ac < 0) {
			while(!so.isConnected()) {
				connect(sa, acquireTimeout, delay);
			}
		}
		else if(ac == 0) {
			connect(sa, acquireTimeout, 0);
		}
		else {
			int c = 0;
			connect(sa, acquireTimeout, delay);
			while(!so.isConnected() && c < ac) {
				connect(sa, acquireTimeout, delay);
				c++;
			}
		}
	}
	
	public void connect() {
		if(so != null) {
			try{
				so.close();
			}
			catch(Exception ex){}
		}
		so = createSocket();
		
		SocketAddress sa = new InetSocketAddress(sv.getIp(), sv.getPort());
		
		if(cfg != null) {
			if(cfg.getAcquireRetryDelay() < 1000) cfg.setAcquireRetryDelay(1000);
			connectByCfg(sa);
		}
		else try {
			so.connect(sa);
		}
		catch(Exception ex) {}
		
		if(so.isConnected()) managConnectedSocket(so);
		else processException(CONNECTEXCEPTION, new Exception("get connection timeout"));
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		connect();
	}
	
	public boolean isConnected() {
		return so != null && so.isConnected() && !so.isClosed();
	}
	
}
