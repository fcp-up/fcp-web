package com.defu.atom.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.defu.atom.db.Database.Terminalonlinercd;

import com.defu.atom.AbstractDao;
import com.defu.atom.AbstractService;
import com.defu.atom.dao.IDaoTerminalonlinercd;
import com.defu.atom.service.ISvcTerminalonlinercd;

/**
 * 所有键<pre>{
 * id	f_id	记录ID,
 * terminalNo	f_terminalNo	终端编号,
 * time	f_time	状态改变时间,
 * state	f_state	在线状态。1：上线；0：离线。,
 * terminalSignal	f_terminalSignal	终端信号强度
 * }</pre>
 */
public class SvcTerminalonlinercd extends AbstractService implements ISvcTerminalonlinercd {
	@Autowired protected IDaoTerminalonlinercd dao;

	public AbstractDao getDao() {
		return dao;
	}

	public boolean beforeAdd(List<Map<String, Object>> params) {
		if(params == null || params.size() < 1) return false;
		for(Map<String, Object> e: params) {
			if(!e.containsKey(Terminalonlinercd.id.prop)) e.put(Terminalonlinercd.id.prop, null);
			if(!e.containsKey(Terminalonlinercd.terminalNo.prop)) e.put(Terminalonlinercd.terminalNo.prop, null);
			if(!e.containsKey(Terminalonlinercd.time.prop)) e.put(Terminalonlinercd.time.prop, null);
			if(!e.containsKey(Terminalonlinercd.state.prop)) e.put(Terminalonlinercd.state.prop, null);
			if(!e.containsKey(Terminalonlinercd.terminalSignal.prop)) e.put(Terminalonlinercd.terminalSignal.prop, null);
		}
		return true;
	}

	
	

	
}
