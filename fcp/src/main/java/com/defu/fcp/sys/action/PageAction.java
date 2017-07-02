package com.defu.fcp.sys.action;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.defu.fcp.base.AbstractAction;


@Controller
public class PageAction extends AbstractAction {
	@RequestMapping("index")
	public String index() {
		return "index";
	}

}
