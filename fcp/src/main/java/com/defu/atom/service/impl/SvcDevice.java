package com.defu.atom.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.defu.atom.db.Database.Device;

import com.defu.atom.AbstractDao;
import com.defu.atom.AbstractService;
import com.defu.atom.dao.IDaoDevice;
import com.defu.atom.service.ISvcDevice;

/**
 * 所有键<pre>{
 * no	f_no	设备编号,
 * name	f_name	设备名称,
 * lastSignal	f_lastSignal	最近信号强度,
 * lastAlarmTime	f_lastAlarmTime	最近一次报警时间,
 * longitude	f_longitude	设备经度,
 * latitude	f_latitude	设备维度,
 * alarmPhone	f_alarmPhone	报警电话,
 * address	f_address	设备位置,
 * terminalNo	f_terminalNo	终端编号,
 * visible	f_visible	是否显示。1：显示；0：不显示
 * }</pre>
 */
public class SvcDevice extends AbstractService implements ISvcDevice {
	@Autowired protected IDaoDevice dao;

	public AbstractDao getDao() {
		return dao;
	}

	public boolean beforeAdd(List<Map<String, Object>> params) {
		if(params == null || params.size() < 1) return false;
		for(Map<String, Object> e: params) {
			if(!e.containsKey(Device.no.prop)) e.put(Device.no.prop, null);
			if(!e.containsKey(Device.name.prop)) e.put(Device.name.prop, null);
			if(!e.containsKey(Device.lastSignal.prop)) e.put(Device.lastSignal.prop, null);
			if(!e.containsKey(Device.lastAlarmTime.prop)) e.put(Device.lastAlarmTime.prop, null);
			if(!e.containsKey(Device.longitude.prop)) e.put(Device.longitude.prop, null);
			if(!e.containsKey(Device.latitude.prop)) e.put(Device.latitude.prop, null);
			if(!e.containsKey(Device.alarmPhone.prop)) e.put(Device.alarmPhone.prop, null);
			if(!e.containsKey(Device.address.prop)) e.put(Device.address.prop, null);
			if(!e.containsKey(Device.terminalNo.prop)) e.put(Device.terminalNo.prop, null);
			if(!e.containsKey(Device.visible.prop)) e.put(Device.visible.prop, null);
		}
		return true;
	}

	
	

	
}
