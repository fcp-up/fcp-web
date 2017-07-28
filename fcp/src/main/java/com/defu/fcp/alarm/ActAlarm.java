package com.defu.fcp.alarm;

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

import com.defu.atom.IAbstractService;
import com.defu.util.Tools;

@Controller
public class ActAlarm extends com.defu.atom.action.ActAlarm {
	@Autowired ISvcAlarm svc;

	protected final Log log = LogFactory.getLog(getClass());

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "deviceAlarm")
	public @ResponseBody Map<String, Object> deviceAlarm(@RequestParam(value = "params", required = false) String params, HttpSession session, HttpServletRequest req, HttpServletResponse rsp) {
		if (log.isDebugEnabled())
			log.debug("请求告警：" + params);
		
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
		rst.put("data", svc.add(list));
		
		return rst;
	}

	@RequestMapping(value = "deviceAlarm/list")
	public @ResponseBody Map<String, Object> deviceAlarmList(@RequestParam(value = "params", required = false) String params, HttpSession session, HttpServletRequest req, HttpServletResponse rsp) {
		if (log.isDebugEnabled())
			log.debug("请求告警：" + params);
		
		Map<String, Object> rst = new HashMap<>(), mp = new HashMap<>();
		
		
		try{
			mp = Tools.jsonStrtoMap(params);
		}
		catch(Exception ex){
			rst.put("code", 1);
			return rst;
		}

		rst.put("code", 0);
		rst.put("data", svc.list(mp));
		if(mp.get("pageSize") != null) {
			long c = svc.count(mp), s = Long.parseLong(mp.get("pageSize").toString());
			if(c % s != 0) s = c / s + 1;
			else s = c / s;
			rst.put("pageCount", s);
		}
		
		return rst;
	}
	
	

	public IAbstractService getService() {
		return svc;
	}

}
