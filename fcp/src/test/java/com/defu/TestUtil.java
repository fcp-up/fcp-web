package com.defu;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.defu.gen.Gen;

public class TestUtil {

	@Test
	public void test() throws Exception {
		genAlarmXml();
		
	}

	public static void genPhoneXml() throws Exception {
		Map<String, String> map = new HashMap<>();
		map.put("phoneNo", "f_phoneNo");
		map.put("id", "f_id");
		
		Map<String, String> fn = new HashMap<>();
		fn.put("count", "count");
		fn.put("list", "list");
		fn.put("single", "single");
		fn.put("add", "add");
		fn.put("addBatch", "addBatch");
		fn.put("delete", "delete");
		fn.put("update", "update");
		
		Map<String, String> cfg = new HashMap<>();
		cfg.put("interface", "com.defu.fcp.phone.dao.IDaoPhone");
		cfg.put("increase", "id");
		
		Gen.auto("tb_phone", fn, map, cfg, "mysql.vm");
	}

	public static void genAlarmXml() throws Exception {
		Map<String, String> map = new HashMap<>();
		map.put("id", "f_id");
		map.put("devId", "f_devId");
		map.put("centerNo", "f_centerNo");
		map.put("isAlarm", "f_isAlarm");
		map.put("pressure", "f_pressure");
		map.put("crtTime", "f_crtTime");
		map.put("status", "f_status");
		map.put("sendTime", "f_sendTime");
		
		Map<String, String> fn = new HashMap<>();
		fn.put("count", "count");
		fn.put("list", "list");
		fn.put("single", "single");
		fn.put("add", "add");
		fn.put("addBatch", "addBatch");
		fn.put("delete", "delete");
		fn.put("update", "update");
		
		Map<String, String> cfg = new HashMap<>();
		cfg.put("interface", "com.defu.fcp.alarm.dao.IDaoAlarm");
		cfg.put("increase", "id");
		
		Gen.auto("tb_alarm", fn, map, cfg, "mysql.vm");
	}

}
