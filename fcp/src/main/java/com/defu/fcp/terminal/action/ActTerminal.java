package com.defu.fcp.terminal.action;

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

import com.defu.fcp.alarm.service.ISvcAlarm;
import com.defu.util.Tools;

@Controller
@RequestMapping(value = "terminal")
public class ActTerminal {
	@Autowired ISvcAlarm alarmSvc;

	protected final Log log = LogFactory.getLog(getClass());

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "requestAlarm")
	public @ResponseBody Map<String, Object> requestAlarm(@RequestParam(value = "params", required = false) String params, HttpSession session, HttpServletRequest req, HttpServletResponse rsp) {
		if (log.isDebugEnabled())
			log.debug("请求告警：" + params);
		
		List<Map<String, Object>> list = null;
		Map<String, Object> rst = new HashMap<>();
		
		try{
			list = (List<Map<String, Object>>)Tools.jsonStrtoList(params);
		}
		catch(Exception ex){
			rst.put("flag", 1);
			return rst;
		}

		rst.put("flag", 0);
		rst.put("data", alarmSvc.add(list));
		
		return rst;
	}
	

}
