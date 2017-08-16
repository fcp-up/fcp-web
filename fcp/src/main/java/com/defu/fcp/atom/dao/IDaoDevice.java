package com.defu.fcp.atom.dao;

import com.defu.fcp.atom.AbstractDao;

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
public interface IDaoDevice extends AbstractDao {}