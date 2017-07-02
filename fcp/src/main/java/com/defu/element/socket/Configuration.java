package com.defu.element.socket;

/**
 * socket configuration
 * 
 * @author glj
 * <pre>
 * 配置一个socket的连接参数
 * 1.timeout：接收消息的超时时长，单位为毫秒。<=0表示不设置
 * 2.acquireTimeout：获取socket连接的超时时长，单位为毫秒。<=0表示不设超时时长；>0表示超时时长。
 * 3.acquireRetryDelay：两次获取连接的时间间隔，单位为毫秒。>=1000，最少1秒
 * 4.acquireRetryAttempts：获取连接的尝试次数 。<0表示一直不停尝试，直至连接成功；0表示不尝试，则只会连接一次，若成功，则连接，否则不再尝试；>0表示尝试次数
 * 5.其余配置参考java.net.SocketOptions。用到什么配置写什么配置，目前只用到Nagle是否开启配置。
 * </pre>
 * @see java.net.SocketOptions
 */
public class Configuration {
	/**
	 * 接收消息的超时时长，单位为毫秒。<=0表示不设置
	 */
	private int timeout;
	/**
	 * 获取socket连接的超时时长，单位为毫秒。<=0表示不设超时时长；>0表示超时时长
	 */
	private int acquireTimeout;
	/**
	 * 两次获取连接的时间间隔，单位为毫秒。>=1000，最少1秒
	 */
	private int acquireRetryDelay;
	/**
	 * 获取连接的尝试次数 。<0表示一直不停尝试，直至连接成功；0表示不尝试，则只会连接一次，若成功，则连接，否则不再尝试；>0表示尝试次数
	 */
	private int acquireRetryAttempts;	
	/**
	 * Nagle算法是否开启，开启后系统会将较小的包合并到较大的包后，一起发送，否则不合并，直接发送，默认为true，开启
	 */
	private boolean nagle = true;

	/**
	 * 返回读取消息的超时时长，单位为毫秒
	 * 
	 * @return
	 */
	public int getTimeout() {
		return timeout;
	}

	/**
	 * 设置读取消息的超时时长，单位为毫秒
	 * 
	 * @param timeout
	 */
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	/**
	 * 返回获取连接的超时时长，单位为毫秒
	 * 
	 * @return
	 */
	public int getAcquireTimeout() {
		return acquireTimeout;
	}

	/**
	 * 设置获取连接的超时时长，单位为毫秒
	 * 
	 * @param acquireTimeout
	 */
	public void setAcquireTimeout(int acquireTimeout) {
		this.acquireTimeout = acquireTimeout;
	}

	/**
	 * 返回两次获取连接的间隔时长，单位为毫秒
	 * 
	 * @return
	 */
	public int getAcquireRetryDelay() {
		return acquireRetryDelay;
	}

	/**
	 * 设置两次获取连接的间隔时长，单位为毫秒
	 * 
	 * @param acquireRetryDelay
	 */
	public void setAcquireRetryDelay(int acquireRetryDelay) {
		this.acquireRetryDelay = acquireRetryDelay;
	}

	/**
	 * 返回获取连接的尝试次数
	 * 
	 * @return
	 */
	public int getAcquireRetryAttempts() {
		return acquireRetryAttempts;
	}

	/**
	 * 设置获取连接的尝试次数 。
	 * 
	 * @param acquireRetryAttempts
	 *			-1表示一直不停尝试，直至连接成功；0表示不尝试，则只会连接一次，若成功，则连接，否则不再尝试；>0表示尝试次数
	 */
	public void setAcquireRetryAttempts(int acquireRetryAttempts) {
		this.acquireRetryAttempts = acquireRetryAttempts;
	}

	/**
	 * 返回是否开启Nagle
	 * @return
	 */
	public boolean isNagle() {
		return nagle;
	}

	/**
	 * 设置是否开启Nagle
	 * @param nagle
	 */
	public void setNagle(boolean nagle) {
		this.nagle = nagle;
	}

}
