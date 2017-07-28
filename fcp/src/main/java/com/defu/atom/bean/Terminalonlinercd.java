package com.defu.atom.bean;

public class Terminalonlinercd {
	/**
	 * 记录ID
	 */
	private java.math.BigInteger id;
	/**
	 * 终端编号
	 */
	private String terminalNo;
	/**
	 * 状态改变时间
	 */
	private java.util.Date time;
	/**
	 * 在线状态。1：上线；0：离线。
	 */
	private Integer state;
	/**
	 * 终端信号强度
	 */
	private Integer terminalSignal;

	public java.math.BigInteger getId() {
		return id;
	}
	public void setId(java.math.BigInteger id) {
		this.id = id;
	}
	
	public String getTerminalNo() {
		return terminalNo;
	}
	public void setTerminalNo(String terminalNo) {
		this.terminalNo = terminalNo;
	}
	
	public java.util.Date getTime() {
		return time;
	}
	public void setTime(java.util.Date time) {
		this.time = time;
	}
	
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	
	public Integer getTerminalSignal() {
		return terminalSignal;
	}
	public void setTerminalSignal(Integer terminalSignal) {
		this.terminalSignal = terminalSignal;
	}
	
}
