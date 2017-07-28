package com.defu.atom.bean;

public class Terminal {
	/**
	 * 终端编号
	 */
	private String no;
	/**
	 * 最近一次终端在线状态。1：在线；0：离线
	 */
	private Integer lastOnlineState;
	/**
	 * 最近一次信号强度
	 */
	private Integer lastSignal;
	/**
	 * 最近一次上线时间
	 */
	private java.util.Date lastOnlineTime;
	/**
	 * 终端经度
	 */
	private Double longitude;
	/**
	 * 终端维度
	 */
	private Double latitude;
	/**
	 * 行政区划编号
	 */
	private String adminDivNo;
	/**
	 * 终端位置
	 */
	private String address;
	/**
	 * 是否显示。1：显示；0：不显示
	 */
	private Integer visible;
	/**
	 * 报警电话
	 */
	private String alarmPhone;

	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	
	public Integer getLastOnlineState() {
		return lastOnlineState;
	}
	public void setLastOnlineState(Integer lastOnlineState) {
		this.lastOnlineState = lastOnlineState;
	}
	
	public Integer getLastSignal() {
		return lastSignal;
	}
	public void setLastSignal(Integer lastSignal) {
		this.lastSignal = lastSignal;
	}
	
	public java.util.Date getLastOnlineTime() {
		return lastOnlineTime;
	}
	public void setLastOnlineTime(java.util.Date lastOnlineTime) {
		this.lastOnlineTime = lastOnlineTime;
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
	
	public String getAdminDivNo() {
		return adminDivNo;
	}
	public void setAdminDivNo(String adminDivNo) {
		this.adminDivNo = adminDivNo;
	}
	
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	public Integer getVisible() {
		return visible;
	}
	public void setVisible(Integer visible) {
		this.visible = visible;
	}
	
	public String getAlarmPhone() {
		return alarmPhone;
	}
	public void setAlarmPhone(String alarmPhone) {
		this.alarmPhone = alarmPhone;
	}
	
}
