package com.defu.atom.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import com.defu.atom.IAbstractService;
import com.defu.atom.service.ISvcTerminal;
import com.defu.atom.AbstractAction;

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
@RequestMapping(value = "terminal")
public class ActTerminal extends AbstractAction {
	@Autowired protected ISvcTerminal svc;
	
	public IAbstractService getService() {
		return svc;
	}

}