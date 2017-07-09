package com.defu;

import gnu.io.PortInUseException;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.TooManyListenersException;

import org.junit.Test;

import com.defu.gen.Gen;
import com.p3rd.SmsCat;

public class TestUtil {
	static Map<String, String> fnMap = new HashMap<>();
	@Test
	public void test() throws Exception {
		genAlarmXml();
		
	}

	public static void smsCatTest() {
		try {
			new SmsCat("smscat", "COM4", 115200, "", "", "").tel("13888041700");
		} catch (PortInUseException | UnsupportedCommOperationException
				| TooManyListenersException | IOException
				| InterruptedException e) {
			e.printStackTrace();
		}
		
	}

	public static void genTerminalOnlineRcdXml() throws Exception {
		Map<String, String> map = new HashMap<>();
		map.put("id", "f_id");
		map.put("terminalNo", "f_terminalNo");
		map.put("state", "f_state");
		map.put("time", "f_time");
		
		Map<String, String> cfg = new HashMap<>();
		cfg.put("interface", "com.defu.fcp.terminal.dao.IDaoTerminalOnlineRcd");
		cfg.put("increase", "id");
		
		Gen.auto("tb_terminalonlinercd", fnMap, map, cfg, "mysql.vm");
	}

	public static void genTerminalXml() throws Exception {
		Map<String, String> map = new HashMap<>();
		map.put("no", "f_no");
		map.put("onlineState", "f_onlineState");
		
		Map<String, String> cfg = new HashMap<>();
		cfg.put("interface", "com.defu.fcp.terminal.dao.IDaoTerminal");
		
		Gen.auto("tb_terminal", fnMap, map, cfg, "mysql.vm");
	}

	public static void genPhoneXml() throws Exception {
		Map<String, String> map = new HashMap<>();
		map.put("phoneNo", "f_phoneNo");
		map.put("id", "f_id");
		
		Map<String, String> cfg = new HashMap<>();
		cfg.put("interface", "com.defu.fcp.phone.dao.IDaoPhone");
		cfg.put("increase", "id");
		
		Gen.auto("tb_phone", fnMap, map, cfg, "mysql.vm");
	}

	public static void genAlarmXml() throws Exception {
		Map<String, String> map = new HashMap<>();
		map.put("id", "f_id");
		map.put("deviceNo", "f_deviceNo");
		map.put("centerNo", "f_centerNo");
		map.put("isAlarm", "f_isAlarm");
		map.put("pressure", "f_pressure");
		map.put("crtTime", "f_crtTime");
		map.put("state", "f_state");
		map.put("sendTime", "f_sendTime");
		map.put("toTelephone", "f_toTelephone");
		
		
		Map<String, String> cfg = new HashMap<>();
		cfg.put("interface", "com.defu.fcp.alarm.dao.IDaoAlarm");
		cfg.put("increase", "id");
		
		Gen.auto("tb_alarm", fnMap, map, cfg, "mysql.vm");
	}

	{
		fnMap.put("count", "count");
		fnMap.put("list", "list");
		fnMap.put("single", "single");
		fnMap.put("add", "add");
		fnMap.put("addBatch", "addBatch");
		fnMap.put("delete", "delete");
		fnMap.put("update", "update");
	}

}
