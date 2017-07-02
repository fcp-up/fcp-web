package com.defu.element;

/**
 * 状态监视对象
 * @author glj
 * <pre>
 * 1.一个状态监听对象必须包含一个监视目标
 * 2.须提供一个获取状态的方法，该方法参数为监视目标
 * 3.须提供一个判断是否结束的方法
 * 4.可选提供一个监视周期，默认为50ms
 * 5.基于一些被监视对象一旦线程开始就被阻塞，须提供 监视目标、起始状态、结束状态 的初始化获取方法，构造函数中不初始化。
 * 6.监视目标、起始状态、结束状态的初始化动作在线程开始后调用初始化获取方法进行
 * 7.可选提供一个timeout属性，表示最多监听时长，单位为秒，默认为1
 * 8.监视对象线程启动timeout秒后还没到结束状态，则通知所有监视器timeout，调用它们的timeout方法。
 * 9.构造函数提供监视周期和监视时长的设置，单位分别为毫秒、秒，默认值分别为50、1
 * 10.若timeout<=0 则表示永不超时，线程将会一直执行直到结束状态。
 * 11.增加delay属性，表示第一次检查状态的延时，单位为毫秒，即在启动后延时delay毫秒开始检测状态
 * </pre>
 * @param <T> 被监视目标类型
 * @param <K> 监视器类型，继承自AsyncListener 
 */
public abstract class StatusListenable<T, K extends AsyncListener> extends AsyncListenable<K> {
	private T t;
	private int s;
	private int i = 50;
	private long st;
	private float to = 1;
	private long d = 0;
	
	private boolean finished = false;

	/**
	 * 监视目标的初始化获取方法
	 * @return 监视目标
	 */
	public abstract T getInitTarget();
	
	/**
	 * 起始状态的初始化获取方法
	 * @return 起始状态
	 */
	public abstract int getInitStatus();
	
	/**
	 * 判断是否已经结束，此方法使用状态来判断，使用监视目标来判断会引起误差
	 * @return
	 */
	public abstract boolean isFinish();
	
	/**
	 * 通过监视目标获取状态
	 * @param tag 监视目标
	 * @return 状态
	 */
	public abstract int getStatusByTarget(T tag);
	
	/**
	 * 获取监视目标
	 * @return 监视目标
	 */
	public T getTarget() {
		return t;
	}

	/**
	 * 获取当前状态
	 * @return 当前状态
	 */
	public int getStatus() {
		return s;
	}

	/**
	 * 对外提供设置状态接口
	 * @param status 状态
	 */
	public void setStatus(int status) {
		if(s != status) {
			this.s = status;
			finished = isFinish();
			notifyUpdate(null);
		}
	}

	public StatusListenable(){}
	
	/**
	 * 构造一个监视对象
	 * @param interval 监视周期，单位为毫秒，若为负数，则取默认值50
	 */
	public StatusListenable(int interval){
		this();
		if(interval > 0) i = interval;
	}
	
	/**
	 * 
	 * 构造一个监视对象
	 * @param delay 第一次检测延时时长，单位为毫秒，若<=0，则启动后就开始检测
	 * @param timeout 监视时长，单位为秒，如果<=0表示永不超时
	 * @param interval 监视周期，单位为毫秒，若为<=0，则取默认值50
	 */
	public StatusListenable(long delay, float timeout, int interval){
		this(interval);
		d = delay;
		to = timeout;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		t = getInitTarget();
		if(t == null) return;
		
		s = getInitStatus();
		
		if(d > 0) {
			try{
				Thread.sleep(d);
			}
			catch(Exception ex){}
		}
		
		st = System.currentTimeMillis();
		
		while(!finished) {
			if(to > 0 && System.currentTimeMillis() - st > to * 1000) {
				notifyTimeout(null);
				break;
			}
			setStatus(getStatusByTarget(t));
			if(!finished) try{
				Thread.sleep(i);
			}
			catch(Exception ex){}
		}
		
		t = null;
	}

	/**
	 * 通知监视器超时
	 * @param arg 可选参数
	 */
	public synchronized void notifyTimeout(Object arg){
		for(AsyncListener o: ls) o.timeout(this, arg);
	}
	
	
}
