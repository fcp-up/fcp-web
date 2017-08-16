package com.defu.fcp.device;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.defu.fcp.atom.IAbstractService;
import com.defu.fcp.atom.db.Database.Device;
import com.defu.util.Tools;

@Controller
public class ActDevice extends com.defu.fcp.atom.action.ActDevice {
	@Autowired ISvcDevice svc;

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "alarmPhone", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> alarmPhone(@RequestParam(value = "params", required = false) String params, HttpSession session, HttpServletRequest req, HttpServletResponse rsp) {

		List<Map<String, Object>> list = null;
		Map<String, Object> rst = new HashMap<>();
		
		try{
			list = (List<Map<String, Object>>)Tools.jsonStrtoList(params);
			for(Map<String, Object> e: list) {
				e.put(Device.alarmPhone.prop, e.get("phoneNo"));
				e.put(Device.no.prop, e.get("deviceNo"));
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
