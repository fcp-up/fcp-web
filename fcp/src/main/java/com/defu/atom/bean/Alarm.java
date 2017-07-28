package com.defu.atom.bean;

public class Alarm {
	/**
	 * 记录ID
	 */
	private java.math.BigInteger id;
	/**
	 * 设备编号
	 */
	private String deviceNo;
	/**
	 * 终端编号
	 */
	private String terminalNo;
	/**
	 * 是否报警
	 */
	private Integer isAlarm;
	/**
	 * 压力值
	 */
	private String pressure;
	/**
	 * 发生时间
	 */
	private java.util.Date time;
	/**
	 * 
	 */
	private Integer state;
	/**
	 * 发送报警时间
	 */
	private java.util.Date sendTime;
	/**
	 * 发送到的电话号码，多个之间以英文逗号,隔开
	 */
	private String toPhone;
	/**
	 * 设备信号强度
	 */
	private Integer deviceSignal;

	public java.math.BigInteger getId() {
		return id;
	}
	public void setId(java.math.BigInteger id) {
		this.id = id;
	}
	
	public String getDeviceNo() {
		return deviceNo;
	}
	public void setDeviceNo(String deviceNo) {
		this.deviceNo = deviceNo;
	}
	
	public String getTerminalNo() {
		return terminalNo;
	}
	public void setTerminalNo(String terminalNo) {
		this.terminalNo = terminalNo;
	}
	
	public Integer getIsAlarm() {
		return isAlarm;
	}
	public void setIsAlarm(Integer isAlarm) {
		this.isAlarm = isAlarm;
	}
	
	public String getPressure() {
		return pressure;
	}
	public void setPressure(String pressure) {
		this.pressure = pressure;
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
	
	public java.util.Date getSendTime() {
		return sendTime;
	}
	public void setSendTime(java.util.Date sendTime) {
		this.sendTime = sendTime;
	}
	
	public String getToPhone() {
		return toPhone;
	}
	public void setToPhone(String toPhone) {
		this.toPhone = toPhone;
	}
	
	public Integer getDeviceSignal() {
		return deviceSignal;
	}
	public void setDeviceSignal(Integer deviceSignal) {
		this.deviceSignal = deviceSignal;
	}
	
}
