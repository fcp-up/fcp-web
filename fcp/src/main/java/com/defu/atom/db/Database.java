package com.defu.atom.db;

public final class Database {
	public static final class Admindiv {
		/**
		 * 区划编号
		 */
		public static final Field no = new Field("tb_admindiv", "f_no", "no", 12, "String", false, false, "");

		/**
		 * 区划名称
		 */
		public static final Field name = new Field("tb_admindiv", "f_name", "name", 12, "String", false, false, null);

		/**
		 * 上级区划编号
		 */
		public static final Field parentNo = new Field("tb_admindiv", "f_parentNo", "parentNo", 12, "String", false, false, null);

		/**
		 * 区划级别。
1：省级（省、自治区、直辖市）；
2：地级（地级市、地区）；
3：县级（县、县级市、市辖区）；
4：乡级（乡、镇、街道、类似乡级单位）；
5：村级（村民委员会、居民委员会、类似村民委员会、类似居民委员会）；
		 */
		public static final Field level = new Field("tb_admindiv", "f_level", "level", 5, "Integer", false, true, null);

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
		public static final Field kind = new Field("tb_admindiv", "f_kind", "kind", 4, "Integer", false, true, null);

	
		public static final String _table = "tb_admindiv";
		public static final Field _key = new Field("tb_admindiv", "f_no", "no", 12, "String", false, false, "");
		public static final Field _increase = null;
	}

	public static final class Alarm {
		/**
		 * 记录ID
		 */
		public static final Field id = new Field("tb_alarm", "f_id", "id", -5, "java.math.BigInteger", true, false, null);

		/**
		 * 设备编号
		 */
		public static final Field deviceNo = new Field("tb_alarm", "f_deviceNo", "deviceNo", 12, "String", false, false, null);

		/**
		 * 终端编号
		 */
		public static final Field terminalNo = new Field("tb_alarm", "f_terminalNo", "terminalNo", 12, "String", false, false, null);

		/**
		 * 是否报警
		 */
		public static final Field isAlarm = new Field("tb_alarm", "f_isAlarm", "isAlarm", -6, "Integer", false, true, null);

		/**
		 * 压力值
		 */
		public static final Field pressure = new Field("tb_alarm", "f_pressure", "pressure", 12, "String", false, false, null);

		/**
		 * 发生时间
		 */
		public static final Field time = new Field("tb_alarm", "f_time", "time", 93, "java.util.Date", false, false, null);

		/**
		 * 
		 */
		public static final Field state = new Field("tb_alarm", "f_state", "state", -6, "Integer", false, true, null);

		/**
		 * 发送报警时间
		 */
		public static final Field sendTime = new Field("tb_alarm", "f_sendTime", "sendTime", 93, "java.util.Date", false, false, null);

		/**
		 * 发送到的电话号码，多个之间以英文逗号,隔开
		 */
		public static final Field toPhone = new Field("tb_alarm", "f_toPhone", "toPhone", 12, "String", false, false, null);

		/**
		 * 设备信号强度
		 */
		public static final Field deviceSignal = new Field("tb_alarm", "f_deviceSignal", "deviceSignal", 4, "Integer", false, true, null);

	
		public static final String _table = "tb_alarm";
		public static final Field _key = new Field("tb_alarm", "f_id", "id", -5, "java.math.BigInteger", true, false, null);
		public static final Field _increase = new Field("tb_alarm", "f_id", "id", -5, "java.math.BigInteger", true, false, null);
	}

	public static final class Device {
		/**
		 * 设备编号
		 */
		public static final Field no = new Field("tb_device", "f_no", "no", 12, "String", false, false, "");

		/**
		 * 设备名称
		 */
		public static final Field name = new Field("tb_device", "f_name", "name", 12, "String", false, false, null);

		/**
		 * 最近信号强度
		 */
		public static final Field lastSignal = new Field("tb_device", "f_lastSignal", "lastSignal", 4, "Integer", false, true, null);

		/**
		 * 最近一次报警时间
		 */
		public static final Field lastAlarmTime = new Field("tb_device", "f_lastAlarmTime", "lastAlarmTime", 93, "java.util.Date", false, false, null);

		/**
		 * 设备经度
		 */
		public static final Field longitude = new Field("tb_device", "f_longitude", "longitude", 8, "Double", false, true, null);

		/**
		 * 设备维度
		 */
		public static final Field latitude = new Field("tb_device", "f_latitude", "latitude", 8, "Double", false, true, null);

		/**
		 * 报警电话
		 */
		public static final Field alarmPhone = new Field("tb_device", "f_alarmPhone", "alarmPhone", 12, "String", false, false, null);

		/**
		 * 设备位置
		 */
		public static final Field address = new Field("tb_device", "f_address", "address", 12, "String", false, false, null);

		/**
		 * 终端编号
		 */
		public static final Field terminalNo = new Field("tb_device", "f_terminalNo", "terminalNo", 12, "String", false, false, null);

		/**
		 * 是否显示。1：显示；0：不显示
		 */
		public static final Field visible = new Field("tb_device", "f_visible", "visible", -6, "Integer", false, true, null);

	
		public static final String _table = "tb_device";
		public static final Field _key = new Field("tb_device", "f_no", "no", 12, "String", false, false, "");
		public static final Field _increase = null;
	}

	public static final class Terminal {
		/**
		 * 终端编号
		 */
		public static final Field no = new Field("tb_terminal", "f_no", "no", 12, "String", false, false, "");

		/**
		 * 最近一次终端在线状态。1：在线；0：离线
		 */
		public static final Field lastOnlineState = new Field("tb_terminal", "f_lastOnlineState", "lastOnlineState", -6, "Integer", false, true, "0");

		/**
		 * 最近一次信号强度
		 */
		public static final Field lastSignal = new Field("tb_terminal", "f_lastSignal", "lastSignal", 4, "Integer", false, true, null);

		/**
		 * 最近一次上线时间
		 */
		public static final Field lastOnlineTime = new Field("tb_terminal", "f_lastOnlineTime", "lastOnlineTime", 93, "java.util.Date", false, false, null);

		/**
		 * 终端经度
		 */
		public static final Field longitude = new Field("tb_terminal", "f_longitude", "longitude", 8, "Double", false, true, null);

		/**
		 * 终端维度
		 */
		public static final Field latitude = new Field("tb_terminal", "f_latitude", "latitude", 8, "Double", false, true, null);

		/**
		 * 行政区划编号
		 */
		public static final Field adminDivNo = new Field("tb_terminal", "f_adminDivNo", "adminDivNo", 12, "String", false, false, null);

		/**
		 * 终端位置
		 */
		public static final Field address = new Field("tb_terminal", "f_address", "address", 12, "String", false, false, null);

		/**
		 * 是否显示。1：显示；0：不显示
		 */
		public static final Field visible = new Field("tb_terminal", "f_visible", "visible", -6, "Integer", false, true, null);

		/**
		 * 报警电话
		 */
		public static final Field alarmPhone = new Field("tb_terminal", "f_alarmPhone", "alarmPhone", 12, "String", false, false, null);

	
		public static final String _table = "tb_terminal";
		public static final Field _key = new Field("tb_terminal", "f_no", "no", 12, "String", false, false, "");
		public static final Field _increase = null;
	}

	public static final class Terminalonlinercd {
		/**
		 * 记录ID
		 */
		public static final Field id = new Field("tb_terminalonlinercd", "f_id", "id", -5, "java.math.BigInteger", true, false, null);

		/**
		 * 终端编号
		 */
		public static final Field terminalNo = new Field("tb_terminalonlinercd", "f_terminalNo", "terminalNo", 12, "String", false, false, null);

		/**
		 * 状态改变时间
		 */
		public static final Field time = new Field("tb_terminalonlinercd", "f_time", "time", 93, "java.util.Date", false, false, null);

		/**
		 * 在线状态。1：上线；0：离线。
		 */
		public static final Field state = new Field("tb_terminalonlinercd", "f_state", "state", -6, "Integer", false, true, null);

		/**
		 * 终端信号强度
		 */
		public static final Field terminalSignal = new Field("tb_terminalonlinercd", "f_terminalSignal", "terminalSignal", 4, "Integer", false, true, null);

	
		public static final String _table = "tb_terminalonlinercd";
		public static final Field _key = new Field("tb_terminalonlinercd", "f_id", "id", -5, "java.math.BigInteger", true, false, null);
		public static final Field _increase = new Field("tb_terminalonlinercd", "f_id", "id", -5, "java.math.BigInteger", true, false, null);
	}

}
