package com.defu.fcp.terminal.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.defu.fcp.base.AbstractDao;
import com.defu.fcp.base.AbstractService;
import com.defu.fcp.terminal.attr.OnlineRcd;
import com.defu.fcp.terminal.attr.Terminal;
import com.defu.fcp.terminal.dao.IDaoTerminal;
import com.defu.fcp.terminal.dao.IDaoTerminalOnlineRcd;
import com.defu.fcp.terminal.service.ISvcTerminal;

@Service
public class SvcTerminal extends AbstractService implements ISvcTerminal {
	@Autowired IDaoTerminal dao;
	@Autowired IDaoTerminalOnlineRcd rcddao;
	
	public Map<String, Object> postOnline(List<Map<String, Object>> list) {
		Map<String, Object> params = new HashMap<>(), rst = new HashMap<>();
		String no;
		
		for(Map<String, Object> t: list) {
			no = (String)t.get(OnlineRcd.terminalNo);
			if(no == null) continue;
			
			try{
				params.clear();
				params.put(Terminal.no, no);
				if(dao.list(params).size() < 1) {
					//终端不存在库中
					params.put(Terminal.onlineState, t.get(OnlineRcd.state));
					dao.add(params);
				}
				
				params.clear();
				params.put(OnlineRcd.state, t.get(OnlineRcd.state));
				params.put(OnlineRcd.time, new Date());
				params.put(OnlineRcd.terminalNo, t.get(OnlineRcd.terminalNo));
				rcddao.add(params);
				
				rst.put(no, 0);
			}
			catch(Exception ex){
				rst.put(no, 1);
			}
			
		}
		
		return null;
	}

	public AbstractDao getDao() {
		return dao;
	}

}
