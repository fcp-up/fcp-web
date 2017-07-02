package com.defu.fcp.sms;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.defu.fcp.Const;
import com.defu.sms.IPhoneMessage;

@Service("catsms")
public class SmsCat implements IPhoneMessage {
	private com.p3rd.SmsCat cat;

	protected final Log log = LogFactory.getLog(getClass());

	@PostConstruct()
	public void init() {
		cat = new com.p3rd.SmsCat(Const.SmsCatCfg.comId, Const.SmsCatCfg.com, Const.SmsCatCfg.baudRate, Const.SmsCatCfg.manufacturer, Const.SmsCatCfg.model, Const.SmsCatCfg.simPin);
		cat.startService();
	}

	@PreDestroy
	public void beforeDestroy(){
		cat.stopService();
	}
	
	@Override
	public Map<String, Object> sendTextMsg(String msg, String phoneNo, Object... args) throws IOException {
		Map<String, Object> mp = new HashMap<>();
		mp.put("success", cat.sendTextMsg(msg, phoneNo));
		return mp;
	}

	@Override
	public Map<String, Object> sendTextMsg(String msg, List<String> phoneNo, Object... args) throws IOException {
		boolean success = false;
		for(String no: phoneNo) {
			if(cat.sendTextMsg(msg, no)) {
				success = true;
			}
		}
		Map<String, Object> mp = new HashMap<>();
		mp.put("success", success);
		return mp;
	}

	@Override
	public Map<String, Object> sendPhonicMsg(String msg, List<String> phoneNo, Object... args) throws IOException {
		return null;
	}

	@Override
	public Map<String, Object> sendPhonicMsg(String msg, String phoneNo, Object... args) throws IOException {
		return null;
	}
}
