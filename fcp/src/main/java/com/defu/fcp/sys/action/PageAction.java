package com.defu.fcp.sys.action;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class PageAction {
	@RequestMapping("index")
	public String index() {
		return "index";
	}

	@RequestMapping("alarm/index")
	public String alarm() {
		return "alarm";
	}

	@RequestMapping("device/index")
	public String device() {
		return "device";
	}

	@RequestMapping("terminal/index")
	public String terminal() {
		return "terminal";
	}

}
