package com.defu.element.cache;

import java.util.HashMap;
import java.util.Map;

/**
 * 一个支持热度、周期刷新的缓冲区
 * <pre>
 * 若缓存区满了，优先级最低的先进先出的模式填充
 * </pre>
 * @author glj
 *
 * @param <K> 数据类型
 * @param <T> 数据标志类型
 */
public class HeatCache<K, T extends Flag<T>> implements ICache<K, T> {
	protected int size;
	protected long refreshInterval;
	protected Map<Flag<T>, CacheData<K>> cache;
	
	@Override
	public synchronized IData<K> pushData(K d, T j) {
		// TODO Auto-generated method stub
		IData<K> r = null;
		if(cache.size() < size) r = cache.put(new Flag<T>(j), new CacheData<K>(d));
		else {
			Flag<T> mk = null;
			for(Flag<T> x: cache.keySet()) {
				if(mk == null) mk = x;
				else if(cache.get(x).heat < cache.get(mk).heat) mk = x;
			}
			cache.remove(mk);
			cache.put(new Flag<T>(j), new CacheData<K>(d));
		}
		return r;
	}

	@Override
	public IData<K> lookup(T j) {
		// TODO Auto-generated method stub
		return cache.get(new Flag<T>(j));
	}

	public long getRefreshInterval() {
		return refreshInterval;
	}

	public void setRefreshInterval(long refreshInterval) {
		this.refreshInterval = refreshInterval;
	}
	
	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
		cache = new HashMap<Flag<T>, CacheData<K>>(size);
	}

	private static class CacheData<T> extends DataImpl<T>{
		private int heat;
		
		public CacheData(T d){
			super(d);
			heat = Integer.MIN_VALUE;
		}
		
	}
	
}
