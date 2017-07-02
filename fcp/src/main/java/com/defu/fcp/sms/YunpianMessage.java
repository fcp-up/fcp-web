package com.defu.fcp.sms;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.defu.fcp.Const;
import com.defu.sms.IPhoneMessage;
import com.defu.util.Tools;

@Service("yunpiansms")
public class YunpianMessage implements IPhoneMessage {
	private com.p3rd.YunpianMessage ypm;
	
	@PostConstruct()
	public void init() {
		ypm = new com.p3rd.YunpianMessage();
		ypm.setApikey(Const.yunpiankey);
	}

	@Override
	public Map<String, Object> sendTextMsg(String msg, String phoneNo, Object... args) throws IOException {
		msg = ypm.sendTextMsg(msg, phoneNo, args[0].toString());
		return Tools.jsonStrtoMap(msg);
	}

	@Override
	public Map<String, Object> sendTextMsg(String msg, List<String> phoneNo, Object... args) throws IOException {
		msg = ypm.sendTextMsg(msg, phoneNo, args[0].toString());
		return Tools.jsonStrtoMap(msg);
	}

	@Override
	public Map<String, Object> sendPhonicMsg(String msg, List<String> phoneNo, Object... args) throws IOException {
		Map<String, Object> rst = new HashMap<>();
		for(String no: phoneNo) {
			msg = ypm.sendPhonicMsg(msg, no, args[0].toString());
			rst.put(no, Tools.jsonStrtoMap(msg));
		}
		return rst;
	}

	@Override
	public Map<String, Object> sendPhonicMsg(String msg, String phoneNo, Object... args) throws IOException {
		msg = ypm.sendPhonicMsg(msg, phoneNo, args[0].toString());
		return Tools.jsonStrtoMap(msg);
	}
}
