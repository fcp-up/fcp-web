package com.defu.fcp;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.defu.sms.IPhoneMessage;
import com.defu.util.Tools;

@Service
public class YunpianMessage implements IPhoneMessage {
	private com.p3rd.YunpianMessage ypm;
	protected final Log log = LogFactory.getLog(getClass());
	
	@PostConstruct()
	public void init() {
		ypm = new com.p3rd.YunpianMessage();
		ypm.setApikey(Const.yunpiankey);
	}

	@Override
	public Map<String, Object> sendTextMsg(String msg, String phoneNo, Object... args) throws IOException {
		if (log.isDebugEnabled()) log.debug("发送文本短信：" + msg);
		msg = ypm.sendTextMsg(msg, phoneNo, args[0].toString());
		if (log.isDebugEnabled()) log.debug("云片响应：" + msg);
		return Tools.jsonStrtoMap(msg);
	}

	@Override
	public Map<String, Object> sendTextMsg(String msg, List<String> phoneNo, Object... args) throws IOException {
		if (log.isDebugEnabled()) log.debug("发送文本短信：" + msg);
		msg = ypm.sendTextMsg(msg, phoneNo, args[0].toString());
		if (log.isDebugEnabled()) log.debug("云片响应：" + msg);
		return Tools.jsonStrtoMap(msg);
	}

	@Override
	public Map<String, Object> sendPhonicMsg(String msg, List<String> phoneNo, Object... args) throws IOException {
		if (log.isDebugEnabled()) log.debug("发送语音短信：" + msg);
		Map<String, Object> rst = new HashMap<>();
		for(String no: phoneNo) {
			msg = ypm.sendPhonicMsg(msg, no, args[0].toString());
			rst.put(no, Tools.jsonStrtoMap(msg));
			if (log.isDebugEnabled()) log.debug("云片响应：" + msg);
		}
		return rst;
	}

	@Override
	public Map<String, Object> sendPhonicMsg(String msg, String phoneNo, Object... args) throws IOException {
		if (log.isDebugEnabled()) log.debug("发送语音短信：" + msg);
		msg = ypm.sendPhonicMsg(msg, phoneNo, args[0].toString());
		if (log.isDebugEnabled()) log.debug("云片响应：" + msg);
		return Tools.jsonStrtoMap(msg);
	}
}
