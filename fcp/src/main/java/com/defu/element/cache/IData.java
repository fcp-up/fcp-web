package com.defu.element.cache;

/**
 * 缓存数据容器
 * <pre>
 * 任意一个远程数据都可以作缓存，降低系统压力
 * </pre>
 * @author glj
 *
 * @param <T> 被缓存的数据的数据类型
 */
public interface IData<T> {
	/**
	 * 更新缓存数据
	 * @param d 新的数据
	 */
	public void update(T d);
	
	/**
	 * 获取被缓存的数据
	 * @return
	 */
	public T getData();
	
	/**
	 * 获取最后一次更新的时间戳
	 * @return 时间戳，离北京时间 1970-01-01 08:00:00 的毫秒数
	 */
	public long getLastUpdateTime();
	
	/**
	 * 缓存数据是否在读取中
	 * @return 如果当前缓存数据正在读取，返回true，否则返回false
	 */
	public boolean isReading();
	
	/**
	 * 设置是否正在读取缓存数据
	 * @param isReading
	 */
	public void setReading(boolean isReading);
	
	/**
	 * 获取最后一次读取缓存数据的异常
	 * @return 
	 */
	public Exception getReadEx();
	
	/**
	 * 设置读取缓存数据的异常
	 * @param readEx 异常
	 */
	public void setReadEx(Exception readEx);
}
