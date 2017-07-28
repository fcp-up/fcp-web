package com.defu.atom.db;

public class Field {
	/**
	 * 所属数据库表名
	 */
	public final String table;
	/**
	 * 数据库字段名
	 */
	public final String name;
	/**
	 * java代码中的实体属性名
	 */
	public final String prop;
	/**
	 * 数据库类型
	 */
	public final int dbtype;
	/**
	 * java类型
	 */
	public final String javatype;
	/**
	 * 是否字段增长
	 */
	public final boolean autoIncrement;
	/**
	 * 有误符号
	 */
	public final boolean signed;
	/**
	 * 默认值
	 */
	public final String defaults;

	/**
	 * 字段构造函数
	 * @param name 字段名 对应库中字段名
	 * @param prop 字段属性 对应java代码中所用属性
	 * @param dbtype 字段类型 对应数据库中的类型
	 * @param javatype 字段类型 对应java中的类型
	 * @param autoIncrement 是否自动增长
	 * @param signed 有无符号
	 * @param defaults 默认值
	 */
	public Field(String table, String name, String prop, int dbtype, String javatype,
			boolean autoIncrement, boolean signed, String defaults) {
		super();
		this.table = table;
		this.name = name;
		this.prop = prop;
		this.dbtype = dbtype;
		this.javatype = javatype;
		this.autoIncrement = autoIncrement;
		this.signed = signed;
		this.defaults = defaults;
	}

}
