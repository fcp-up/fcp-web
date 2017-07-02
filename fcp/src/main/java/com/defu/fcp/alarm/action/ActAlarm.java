package com.defu.fcp.alarm.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.defu.fcp.alarm.service.ISvcAlarm;
import com.defu.fcp.base.AbstractAction;
import com.defu.fcp.base.IAbstractService;

@Controller
@RequestMapping(value = "alarm")
public class ActAlarm extends AbstractAction {
	@Autowired ISvcAlarm svc;
	
	public IAbstractService getService() {
		return svc;
	}

}
