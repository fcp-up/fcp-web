package com.defu.element.cache;

/**
 * 缓存区
 * @author glj
 *
 * @param <K> 被缓存的数据的数据类型
 * @param <T> 被缓存数据的数据标志类型
 */
public interface ICache<K, T> {
	/**
	 * 推入一个缓存数据
	 * @param d 缓存数据
	 * @param j 缓存数据的数据标志
	 * @return 缓存数据的包装容器
	 * @see IData
	 */
	public IData<K> pushData(K d, T j);
	
	/**
	 * 根据数据标志查询缓存数据的包装容器
	 * @param j 数据标志
	 * @return 缓存数据的包装容器
	 */
	public IData<K> lookup(T j);
	
}
