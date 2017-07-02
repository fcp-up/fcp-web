package com.defu.element;

import java.util.ArrayList;
import java.util.List;

/**
 * 被监听对象，被观察者
 * @author glj
 *
 * @param <T> 监听器类型，是一个Listener
 * @see Listener
 */
public class Listenable<T extends Listener> {
	protected List<T> ls;
	
	public Listenable(){
		ls = new ArrayList<T>();
	}
	
	/**
	 * 添加一个监听器
	 * @param o 待添加的监听器
	 */
	public synchronized void addListener(T o) {
		if(o == null) throw new NullPointerException();
		if(!ls.contains(o)) ls.add(o);
	}
	
	/**
	 * 删除一个监听器
	 * @param o 待删除的监听器
	 */
	public synchronized void removeListener(T o) {
		ls.remove(o);
	}
	
	/**
	 * 通知监听器更新
	 * @param arg 可选参数
	 */
	public synchronized void notifyUpdate(Object arg) {
		for(T o: ls) o.update(this, arg);
	}
	
	/**
	 * 删除所有监听器
	 */
	public synchronized void removeListeners() {
		ls.clear();
	}
	
	/**
	 * 获取监听器个数
	 * @return 监听器个数
	 */
	public synchronized int countListeners() {
		return ls.size();
	}
	
}
