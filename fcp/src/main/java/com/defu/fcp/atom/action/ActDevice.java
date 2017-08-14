package com.defu.fcp.atom.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import com.defu.fcp.atom.IAbstractService;
import com.defu.fcp.atom.service.ISvcDevice;
import com.defu.fcp.atom.AbstractAction;

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
@RequestMapping(value = "device")
public class ActDevice extends AbstractAction {
	@Autowired protected ISvcDevice svc;
	
	public IAbstractService getService() {
		return svc;
	}

}
