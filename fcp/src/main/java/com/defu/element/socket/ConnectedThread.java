package com.defu.element.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * socket读写线程
 * @author glj<pre>
 * 注：本线程socket是一个长连接，线程不主动关闭socket
 * 一旦socket线程开始启动，将不断读取服务端传递过来的数据
 * 1.调用pause方法，停止接受服务端传递过来的数据；
 * 2.调用goon方法，继续接受；
 * 3.调用finish方法，结束线程；
 * 
 * 因为该socket是长连接，所以无法区分一段消息是否结束完毕（没有-1），可以在read方法中自行处理
 * </pre>
 */
public abstract class ConnectedThread implements Runnable {
	/**
	 * 初始化异常代码
	 */
	public final static int INITEXCEPTION = 1;
	/**
	 * 读数据异常代码
	 */
	public final static int READEXCEPTION = 2;
	/**
	 * 连接异常代码
	 */
	public final static int CONNECTEXCEPTION = 4;

	protected Socket so;
	protected int batchSize;

	protected InputStream in;
	protected OutputStream out;

	protected boolean canceled;
	protected boolean finished;
	

	/**
	 * 处理异常
	 * @param exceptionCode 异常代码
	 * 1.INITEXCEPTION 初始化异常：初始化线程出错时抛出。
	 * 2.READEXCEPTION 读取异常：读取数据异常时抛出。
	 * 4.CONNECTEXCEPTION 连接异常：重连异常时抛出。
	 * @param ex 异常对象
	 */
	public void processException(int exceptionCode, Exception ex) {}

	/**
	 * 读数据，数据来源于socket
	 * @param buf socket读到的byte buffer
	 * @param size 实际数据长度
	 */
	public void read(byte[] buf, int size) {}

	/**
	 * 构造socket线程
	 * @param so socket
	 * @param batchSize 读数据的buffer长度
	 */
	public ConnectedThread(Socket so, int batchSize) {
		this.batchSize = batchSize;
		this.so = so;
		InputStream tin = null;
		OutputStream tout = null;

		canceled = false;
		finished = false;
		
		try {
			tin = so.getInputStream();
			tout = so.getOutputStream();
		} catch (Exception ex) {
			processException(INITEXCEPTION, ex);
			return;
		}

		in = tin;
		out = tout;
	}

	/**
	 * 向服务器写数据
	 * @param data 数据
	 */
	public void write(byte[] data) throws IOException {
		out.write(data);
		canceled = false;
		out.flush();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		byte[] buf = new byte[batchSize];
		int size;
		while (!finished) {
			if (!canceled) {
				try {
					while ((size = in.read(buf)) != -1) {
						read(buf, size);
					}
				} catch (Exception ex) {
					processException(READEXCEPTION, ex);
					try {
						Thread.sleep(1000);
					}
					catch(Exception exx) {}
				}
			}
			try {
				Thread.sleep(500);
			}
			catch(Exception exx) {}
		}
	}
	
	public void destroy() {
		try {
			finish();
			so.close();			
		}
		catch(Exception ex){}
		so = null;
		out = null;
		in = null;
	}

	/**
	 * 设置结束，关闭该线程
	 */
	public void finish() {
		finished = true;
	}

	/**
	 * 停止读取
	 */
	public void pause() {
		canceled = true;
	}
	
	/**
	 * 继续读取
	 */
	public void goon(){
		canceled = false;
	}

	/**
	 * 判断是否连接
	 * @return
	 */
	public boolean isConnected() {
		return so != null && so.isConnected() && !so.isClosed();
	}
	
	public Socket getSocket() {
		return so;
	}

}
