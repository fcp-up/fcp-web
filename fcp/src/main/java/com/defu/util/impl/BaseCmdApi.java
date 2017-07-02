package com.defu.util.impl;



import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class BaseCmdApi {
	protected final Log log = LogFactory.getLog(getClass());

	// @Override
	public Process systemCmd(String cmd) {
		// TODO Auto-generated method stub
		try {
			//cmd = cmd.replaceAll("\\\\", "/");
			if(log.isDebugEnabled()) log.debug("发起cmd指令：" + cmd);
			Process proc = Runtime.getRuntime().exec(cmd);
			
			return proc;
		}
		catch(Exception ex) {
			if(log.isDebugEnabled()) log.debug("发起cmd指令异常：" + ex.getMessage());
		}
		return null;
	}
}
