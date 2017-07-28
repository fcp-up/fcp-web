package com.defu.atom.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

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
 * alarmPhone	f_alarmPhone	报警电话
 * }</pre>
 */
public class SvcTerminal extends AbstractService implements ISvcTerminal {
	@Autowired protected IDaoTerminal dao;

	public AbstractDao getDao() {
		return dao;
	}

	public boolean beforeAdd(List<Map<String, Object>> params) {
		if(params == null || params.size() < 1) return false;
		for(Map<String, Object> e: params) {
			if(!e.containsKey(Terminal.no.prop)) e.put(Terminal.no.prop, null);
			if(!e.containsKey(Terminal.lastOnlineState.prop)) e.put(Terminal.lastOnlineState.prop, null);
			if(!e.containsKey(Terminal.lastSignal.prop)) e.put(Terminal.lastSignal.prop, null);
			if(!e.containsKey(Terminal.lastOnlineTime.prop)) e.put(Terminal.lastOnlineTime.prop, null);
			if(!e.containsKey(Terminal.longitude.prop)) e.put(Terminal.longitude.prop, null);
			if(!e.containsKey(Terminal.latitude.prop)) e.put(Terminal.latitude.prop, null);
			if(!e.containsKey(Terminal.adminDivNo.prop)) e.put(Terminal.adminDivNo.prop, null);
			if(!e.containsKey(Terminal.address.prop)) e.put(Terminal.address.prop, null);
			if(!e.containsKey(Terminal.visible.prop)) e.put(Terminal.visible.prop, null);
			if(!e.containsKey(Terminal.alarmPhone.prop)) e.put(Terminal.alarmPhone.prop, null);
		}
		return true;
	}

	
	

	
}
