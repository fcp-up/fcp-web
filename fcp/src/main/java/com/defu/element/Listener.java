package com.defu.element;

/**
 * 监听器，观察者，监听一个监听对象
 * @author glj
 *
 */
public interface Listener {
	/**
	 * 更新方法，由被监听对象调用
	 * @param tag 被监听对象
	 * @param arg 可选参数，调用时传递
	 */
	public void update(Listenable<? extends Listener> tag, Object arg);
	
	
}
