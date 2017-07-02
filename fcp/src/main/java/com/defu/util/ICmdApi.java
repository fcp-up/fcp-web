package com.defu.util;

public interface ICmdApi {
	/**
	 * 发起一个系统cmd命令
	 * @param cmd cmd命令串
	 * @return 一个Process对象，如果异常，返回null
	 */
	public Process systemCmd(String cmd);
}
