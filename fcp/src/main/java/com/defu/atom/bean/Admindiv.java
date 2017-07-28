package com.defu.atom.bean;

public class Admindiv {
	/**
	 * 区划编号
	 */
	private String no;
	/**
	 * 区划名称
	 */
	private String name;
	/**
	 * 上级区划编号
	 */
	private String parentNo;
	/**
	 * 区划级别。
1：省级（省、自治区、直辖市）；
2：地级（地级市、地区）；
3：县级（县、县级市、市辖区）；
4：乡级（乡、镇、街道、类似乡级单位）；
5：村级（村民委员会、居民委员会、类似村民委员会、类似居民委员会）；
	 */
	private Integer level;
	/**
	 * 区划类型。
111：表示主城区；
112：表示城乡结合区；
121：表示镇中心区；
122：表示镇乡结合区；
123：表示特殊区域；
210：表示乡中心区；
220：表示村庄；'
	 */
	private Integer kind;

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
	
	public String getParentNo() {
		return parentNo;
	}
	public void setParentNo(String parentNo) {
		this.parentNo = parentNo;
	}
	
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	
	public Integer getKind() {
		return kind;
	}
	public void setKind(Integer kind) {
		this.kind = kind;
	}
	
}
