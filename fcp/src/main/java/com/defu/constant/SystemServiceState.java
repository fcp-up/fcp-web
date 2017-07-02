package com.defu.constant;

public enum SystemServiceState {
	/**
	 * 不存在
	 */
	NOTEXISTS,
	/**
	 * 已停止
	 */
	STOPPED,
	/**
	 * 正在启动
	 */
	START_PENDING,
	/**
	 * 正在停止
	 */
	STOP_PENDING,
	/**
	 * 已启动
	 */
	RUNNING,
	/**
	 * 正在恢复，从已暂停到已启动的过程
	 */
	CONTINUE_PENDING, 
	/**
	 * 正在暂停
	 */
	PAUSE_PENDING,
	/**
	 * 已暂停
	 */
	PAUSED
}
