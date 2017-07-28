package com.defu.atom.service;

import com.defu.atom.IAbstractService;

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
public interface ISvcAlarm extends IAbstractService {

}
