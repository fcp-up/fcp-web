package com.defu.element;

/**
 * 异步监听对象，异步监听对象需要开启一个线程执行
 * @author glj
 *
 * @param <T> 监听器类型
 * @see com.defu.element.Listenable
 * @see Runnable
 */
public abstract class AsyncListenable<T extends AsyncListener> extends Listenable<T> implements Runnable {

}
