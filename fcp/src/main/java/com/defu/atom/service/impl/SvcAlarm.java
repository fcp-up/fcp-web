package com.defu.atom.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.defu.atom.db.Database.Alarm;

import com.defu.atom.AbstractDao;
import com.defu.atom.AbstractService;
import com.defu.atom.dao.IDaoAlarm;
import com.defu.atom.service.ISvcAlarm;

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
		if(params == null || params.size() < 1) return false;
		for(Map<String, Object> e: params) {
			if(!e.containsKey(Alarm.id.prop)) e.put(Alarm.id.prop, null);
			if(!e.containsKey(Alarm.deviceNo.prop)) e.put(Alarm.deviceNo.prop, null);
			if(!e.containsKey(Alarm.terminalNo.prop)) e.put(Alarm.terminalNo.prop, null);
			if(!e.containsKey(Alarm.isAlarm.prop)) e.put(Alarm.isAlarm.prop, null);
			if(!e.containsKey(Alarm.pressure.prop)) e.put(Alarm.pressure.prop, null);
			if(!e.containsKey(Alarm.time.prop)) e.put(Alarm.time.prop, null);
			if(!e.containsKey(Alarm.state.prop)) e.put(Alarm.state.prop, null);
			if(!e.containsKey(Alarm.sendTime.prop)) e.put(Alarm.sendTime.prop, null);
			if(!e.containsKey(Alarm.toPhone.prop)) e.put(Alarm.toPhone.prop, null);
			if(!e.containsKey(Alarm.deviceSignal.prop)) e.put(Alarm.deviceSignal.prop, null);
		}
		return true;
	}

	
	

	
}
