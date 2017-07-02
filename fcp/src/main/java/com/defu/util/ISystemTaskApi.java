package com.defu.util;

import java.util.List;

public interface ISystemTaskApi {
	/**
	 * 根据进程名称返回进程PID
	 * @param taskName 进程名称
	 * @return PID列表，如果异常，返回null。如果没找到对应进程，返回空列表
	 */
	public List<Integer> taskPidsByName(String taskName);
	/**
	 * 启动一个.exe程序
	 * @param path .exe程序路径
	 * @return 一个Process对象，如果异常，返回null
	 */
	public Process startExe(String path);
	
	/**
	 * 根据进程名称停止进程
	 * @param taskName 进程名称
	 * @return 一个Process对象，如果异常，返回null
	 */
	public Process stopSystemTaskByName(String taskName);

	/**
	 * 根据父进程名称停止进程组
	 * @param parentTaskName 父进程名称
	 * @return 一个Process对象，如果异常，返回null
	 */
	public Process stopSystemTaskGroupByParentName(String parentTaskName);
	
	/**
	 * 通过pid返回进程名称
	 * @param pid 进程PID
	 * @return 进程名称
	 */
	public String taskNameByPid(Integer pid);
	
}
