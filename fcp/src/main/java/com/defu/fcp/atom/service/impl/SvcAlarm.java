package com.defu.fcp.atom.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.defu.fcp.atom.AbstractDao;
import com.defu.fcp.atom.AbstractService;
import com.defu.fcp.atom.dao.IDaoAlarm;
import com.defu.fcp.atom.db.Database.Alarm;
import com.defu.fcp.atom.service.ISvcAlarm;

/**
 * 所有键<pre>{
 * id	f_id	记录ID,
 * deviceNo	f_deviceNo	设备编号,
 * terminalNo	f_terminalNo	终端编号,
 * isAlarm	f_isAlarm	是否报警,
 * pressure	f_pressure	压力值,
 * time	f_time	发生时间,
 * state	f_state	,
 * sendTime	f_sendTime	发送报警时间,
 * toPhone	f_toPhone	发送到的电话号码，多个之间以英文逗号,隔开,
 * deviceSignal	f_deviceSignal	设备信号强度
 * }</pre>
 */
public class SvcAlarm extends AbstractService implements ISvcAlarm {
	@Autowired protected IDaoAlarm dao;

	public AbstractDao getDao() {
		return dao;
	}
	public boolean beforeAdd(List<Map<String, Object>> params) {
		boolean rst = super.beforeAdd(params);
		if(rst) {
    		for(Map<String, Object> e: params) {
				checkTimeField(e);
    		}
		}
		return rst;
	}

	public boolean beforeAdd(Map<String, Object> params) {
		boolean rst = super.beforeAdd(params);
		if(rst) {
			checkTimeField(params);
		}
		return rst;
	}
	
	private void checkTimeField(Map<String, Object> params) {
		Object v;
		v = params.get(Alarm.time.prop);
		if(v != null) {
			if(Pattern.matches("^\\d+$", v.toString())) {
				params.put(Alarm.time.prop, dateFormat.format(new Date(Long.parseLong(v.toString()))));
			}
		}
		
		v = params.get(Alarm.sendTime.prop);
		if(v != null) {
			if(Pattern.matches("^\\d+$", v.toString())) {
				params.put(Alarm.sendTime.prop, dateFormat.format(new Date(Long.parseLong(v.toString()))));
			}
		}
		
	}

}
