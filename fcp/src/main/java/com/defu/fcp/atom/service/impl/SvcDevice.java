package com.defu.fcp.atom.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.defu.fcp.atom.db.Database.Device;

import com.defu.fcp.atom.AbstractDao;
import com.defu.fcp.atom.AbstractService;
import com.defu.fcp.atom.dao.IDaoDevice;
import com.defu.fcp.atom.service.ISvcDevice;

/**
 * 所有键<pre>{
 * no	f_no	设备编号,
 * terminalNo	f_terminalNo	终端编号,
 * name	f_name	设备名称,
 * lastSignal	f_lastSignal	最近信号强度,
 * lastAlarmTime	f_lastAlarmTime	最近一次报警时间,
 * lastPressure	f_lastPressure	最近电池压力,
 * lastIsAlarm	f_lastIsAlarm	最近一次消息是否报警,
 * longitude	f_longitude	设备经度,
 * latitude	f_latitude	设备维度,
 * alarmPhone	f_alarmPhone	报警电话,
 * address	f_address	设备位置,
 * visible	f_visible	是否显示。1：显示；0：不显示
 * }</pre>
 */
public class SvcDevice extends AbstractService implements ISvcDevice {
	@Autowired protected IDaoDevice dao;

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
		v = params.get(Device.lastAlarmTime.prop);
		if(v != null) {
			if(Pattern.matches("^\\d+$", v.toString())) {
				params.put(Device.lastAlarmTime.prop, format.format(new Date(Long.parseLong(v.toString()))));
			}
		}
		
	}

}
