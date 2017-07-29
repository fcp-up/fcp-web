package com.defu.atom.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.defu.atom.db.Database.Terminal;

import com.defu.atom.AbstractDao;
import com.defu.atom.AbstractService;
import com.defu.atom.dao.IDaoTerminal;
import com.defu.atom.service.ISvcTerminal;

/**
 * 所有键<pre>{
 * no	f_no	终端编号,
 * lastOnlineState	f_lastOnlineState	最近一次终端在线状态。1：在线；0：离线,
 * lastSignal	f_lastSignal	最近一次信号强度,
 * lastOnlineTime	f_lastOnlineTime	最近一次上线时间,
 * longitude	f_longitude	终端经度,
 * latitude	f_latitude	终端维度,
 * adminDivNo	f_adminDivNo	行政区划编号,
 * address	f_address	终端位置,
 * visible	f_visible	是否显示。1：显示；0：不显示,
 * alarmPhone	f_alarmPhone	报警电话,
 * name	f_name	终端名称
 * }</pre>
 */
public class SvcTerminal extends AbstractService implements ISvcTerminal {
	@Autowired protected IDaoTerminal dao;

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
		v = params.get(Terminal.lastOnlineTime.prop);
		if(v != null) {
			if(Pattern.matches("^\\d+$", v.toString())) {
				params.put(Terminal.lastOnlineTime.prop, dateFormat.format(new Date(Long.parseLong(v.toString()))));
			}
		}
		
	}

}
