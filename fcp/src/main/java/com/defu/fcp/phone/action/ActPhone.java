package com.defu.fcp.phone.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.defu.fcp.base.AbstractAction;
import com.defu.fcp.base.IAbstractService;
import com.defu.fcp.phone.service.ISvcPhone;

@Controller
@RequestMapping(value = "phone")
public class ActPhone extends AbstractAction {
	@Autowired ISvcPhone svc;
	
	public IAbstractService getService() {
		return svc;
	}

	@RequestMapping("index")
	public String index() {
		return "phone";
	}

}
