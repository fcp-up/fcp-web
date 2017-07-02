package com.defu.element;

/**
 * 异步监听器，须提供一个超时方法
 * @author glj
 * 
 */
public interface AsyncListener extends Listener {
	/**
	 * 超时方法，由被监听对象调用
	 * @param tag 被监听对象
	 * @param arg 可选参数，调用时传递
	 */
	public void timeout(AsyncListenable<? extends AsyncListener> tag, Object arg);
}
