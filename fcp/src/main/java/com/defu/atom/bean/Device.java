package com.defu.atom.bean;

public class Device {
	/**
	 * 设备编号
	 */
	private String no;
	/**
	 * 设备名称
	 */
	private String name;
	/**
	 * 最近信号强度
	 */
	private Integer lastSignal;
	/**
	 * 最近一次报警时间
	 */
	private java.util.Date lastAlarmTime;
	/**
	 * 设备经度
	 */
	private Double longitude;
	/**
	 * 设备维度
	 */
	private Double latitude;
	/**
	 * 报警电话
	 */
	private String alarmPhone;
	/**
	 * 设备位置
	 */
	private String address;
	/**
	 * 终端编号
	 */
	private String terminalNo;
	/**
	 * 是否显示。1：显示；0：不显示
	 */
	private Integer visible;

	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public Integer getLastSignal() {
		return lastSignal;
	}
	public void setLastSignal(Integer lastSignal) {
		this.lastSignal = lastSignal;
	}
	
	public java.util.Date getLastAlarmTime() {
		return lastAlarmTime;
	}
	public void setLastAlarmTime(java.util.Date lastAlarmTime) {
		this.lastAlarmTime = lastAlarmTime;
	}
	
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	
	public String getAlarmPhone() {
		return alarmPhone;
	}
	public void setAlarmPhone(String alarmPhone) {
		this.alarmPhone = alarmPhone;
	}
	
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getTerminalNo() {
		return terminalNo;
	}
	public void setTerminalNo(String terminalNo) {
		this.terminalNo = terminalNo;
	}
	
	public Integer getVisible() {
		return visible;
	}
	public void setVisible(Integer visible) {
		this.visible = visible;
	}
	
}
