package com.defu.fcp.atom.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.defu.fcp.atom.db.Database.Terminalonlinercd;

import com.defu.fcp.atom.AbstractDao;
import com.defu.fcp.atom.AbstractService;
import com.defu.fcp.atom.dao.IDaoTerminalonlinercd;
import com.defu.fcp.atom.service.ISvcTerminalonlinercd;

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
		v = params.get(Terminalonlinercd.time.prop);
		if(v != null) {
			if(Pattern.matches("^\\d+$", v.toString())) {
				params.put(Terminalonlinercd.time.prop, format.format(new Date(Long.parseLong(v.toString()))));
			}
		}
		
	}

}
