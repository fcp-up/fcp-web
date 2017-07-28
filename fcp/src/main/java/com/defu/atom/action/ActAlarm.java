package com.defu.atom.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import com.defu.atom.IAbstractService;
import com.defu.atom.service.ISvcAlarm;
import com.defu.atom.AbstractAction;

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
@RequestMapping(value = "alarm")
public class ActAlarm extends AbstractAction {
	@Autowired protected ISvcAlarm svc;
	
	public IAbstractService getService() {
		return svc;
	}

}
