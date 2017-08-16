package com.defu.fcp.terminal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.defu.fcp.alarm.ISvcAlarm;
import com.defu.fcp.atom.IAbstractService;
import com.defu.fcp.atom.db.Database.Device;
import com.defu.util.Tools;

@Controller
public class ActTerminal extends com.defu.fcp.atom.action.ActTerminal {
	@Autowired ISvcAlarm alarmSvc;
	@Autowired ISvcTerminal svc;
	
	protected final Log log = LogFactory.getLog(getClass());

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "postOnline")
	public @ResponseBody Map<String, Object> postOnline(@RequestParam(value = "params", required = false) String params, HttpSession session, HttpServletRequest req, HttpServletResponse rsp) {
		if (log.isDebugEnabled())
			log.debug("终端上下线：" + params);
		
		List<Map<String, Object>> list = null;
		Map<String, Object> rst = new HashMap<>();
		
		try{
			list = (List<Map<String, Object>>)Tools.jsonStrtoList(params);
		}
		catch(Exception ex){
			rst.put("code", 1);
			return rst;
		}

		rst.put("code", 0);
		rst.put("data", svc.postOnline(list));
		
		return rst;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "alarmPhone")
	public @ResponseBody Map<String, Object> alarmPhone(@RequestParam(value = "params", required = false) String params, HttpSession session, HttpServletRequest req, HttpServletResponse rsp) {

		List<Map<String, Object>> list = null;
		Map<String, Object> rst = new HashMap<>();
		
		try{
			list = (List<Map<String, Object>>)Tools.jsonStrtoList(params);
			for(Map<String, Object> e: list) {
				e.put(Device.alarmPhone.prop, e.get("phoneNo"));
				e.put(Device.terminalNo.prop, e.get("terminalNo"));
			}
		}
		catch(Exception ex){
			rst.put("code", 1);
			return rst;
		}

		rst.put("code", 0);
		rst.put("data", svc.setAlarmPhone(list));
		
		return rst;
	}

	public IAbstractService getService() {
		return svc;
	}


}
