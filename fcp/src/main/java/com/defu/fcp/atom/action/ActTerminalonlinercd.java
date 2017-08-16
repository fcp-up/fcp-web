package com.defu.fcp.atom.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import com.defu.fcp.atom.IAbstractService;
import com.defu.fcp.atom.service.ISvcTerminalonlinercd;
import com.defu.fcp.atom.AbstractAction;

/**
 * 所有键<pre>{
 * id	f_id	记录ID,
 * terminalNo	f_terminalNo	终端编号,
 * time	f_time	状态改变时间,
 * state	f_state	在线状态。1：上线；0：离线。,
 * terminalSignal	f_terminalSignal	终端信号强度
 * }</pre>
 */
@RequestMapping(value = "terminalonlinercd")
public class ActTerminalonlinercd extends AbstractAction {
	@Autowired protected ISvcTerminalonlinercd svc;
	
	public IAbstractService getService() {
		return svc;
	}

}
