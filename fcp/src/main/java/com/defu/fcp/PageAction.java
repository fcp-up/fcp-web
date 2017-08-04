package com.defu.fcp;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.defu.util.Tools;
import com.fasterxml.jackson.core.JsonProcessingException;


@Controller
public class PageAction {
	@RequestMapping("socket")
	public String index(Model m, HttpSession session, HttpServletRequest req, HttpServletResponse rsp) {
		setCommon(m, session, req, rsp);
		return "socket";
	}

	@RequestMapping("alarm/index")
	public String alarm(Model m, HttpSession session, HttpServletRequest req, HttpServletResponse rsp) {
		setCommon(m, session, req, rsp);
		return "alarm";
	}

	@RequestMapping("device/index")
	public String device(Model m, HttpSession session, HttpServletRequest req, HttpServletResponse rsp) {
		setCommon(m, session, req, rsp);
		return "device";
	}

	@RequestMapping("terminal/index")
	public String terminal(Model m, HttpSession session, HttpServletRequest req, HttpServletResponse rsp) {
		setCommon(m, session, req, rsp);
		return "terminal";
	}

	@RequestMapping("session.js")
	public String session(Model m, HttpSession session, HttpServletRequest req, HttpServletResponse rsp) {
		setCommon(m, session, req, rsp);
		return "session";
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void setCommon(Model m, HttpSession session, HttpServletRequest req, HttpServletResponse rsp) {
		Object o = session.getAttribute(Const.SessionKey.curUser);
		if(o != null) {
			try {
				m.addAttribute("usrStr", Tools.mapToJsonStr((Map)o));
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
			m.addAttribute("usr", o);
		}
		m.addAttribute("sessionId", session.getId());
		m.addAttribute("serverTime", System.currentTimeMillis());
	}

}
