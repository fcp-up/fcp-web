package com.defu.fcp.alarm.service.impl;

import java.net.URLEncoder;
import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.defu.fcp.ThreadPool;
import com.defu.fcp.alarm.dao.IDaoAlarm;
import com.defu.fcp.alarm.service.ISvcAlarm;
import com.defu.fcp.base.AbstractDao;
import com.defu.fcp.base.AbstractService;
import com.defu.fcp.phone.service.ISvcPhone;
import com.defu.sms.IPhoneMessage;

@Service
public class SvcAlarm extends AbstractService implements Runnable, ISvcAlarm {
	@Autowired IDaoAlarm dao;
	@Autowired ISvcPhone phoneSvc;
	@Autowired IPhoneMessage phonesms;

	// 编码格式。发送编码格式统一用UTF-8
	private static String ENCODING = "UTF-8";
	
	protected final Log log = LogFactory.getLog(getClass());
	private int queueSize = 1000;
	private Queue<Map<String, Object>> msg = new ConcurrentLinkedQueue<>();
	boolean working = false;
	
	public AbstractDao getDao() {
		return dao;
	}

	@Override
	public Map<String, Object> add(List<Map<String, Object>> params) {
		Map<String, Object> rst = new HashMap<>();
		Map<Map<String, Object>, Map<String, Object>> detail = new HashMap<>();
		int i = 0;
		for(Map<String, Object> e: params) {
			e.put("crtTime", new Date());
			detail.put(e, add(e));
			i++;
		}
		rst.put("detail", detail);
		rst.put("count", i);
		return rst;
	}

	public void afterAdd(Map<String, Object> params, int count, Map<String, Object> rst) {
		if (log.isDebugEnabled())
			log.debug("请求告警：" + params);
		if(msg.size() > queueSize) {
			//队列满了，移除第一条指令
			if (log.isDebugEnabled())
				log.debug("请求告警：" + params + "\t队列已满，移除队列中第一条告警");
			msg.poll();
		}
		
		msg.offer(params);
		
	}
	
	private void alarm(Map<String, Object> mp) {
		List<Map<String, Object>> phones = phoneSvc.list(new HashMap<String, Object>());
		
		List<String> phonenos = new ArrayList<>();
		for(Map<String, Object> p: phones) phonenos.add(p.get("phoneNo").toString());
		
		if(phonenos.size() < 1) return;
		
		alarmPhonic(mp, phonenos);
		alarmText(mp, phonenos);
	}
	
	protected void alarmText(Map<String, Object> mp, List<String> phoneNo) {
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append(URLEncoder.encode("#devicename#", ENCODING));
			sb.append("=");
			sb.append(URLEncoder.encode(mp.get("devId").toString(), ENCODING));
			sb.append("&");
			sb.append(URLEncoder.encode("#currentstatus#", ENCODING));
			sb.append("=");
			sb.append(URLEncoder.encode("发生险情", ENCODING));
			
			for(String no: phoneNo) {
				phonesms.sendTextMsg(sb.toString(), no, "1842546");
			}
			
		}
		catch(Exception ex){
		}
	}
	
	protected void alarmPhonic(Map<String, Object> mp, List<String> phoneNo) {
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append(URLEncoder.encode("devicename", ENCODING));
			sb.append("=");
			sb.append(URLEncoder.encode(mp.get("devId").toString(), ENCODING));
			
			phonesms.sendPhonicMsg(URLEncoder.encode(sb.toString(), ENCODING), phoneNo, "1842616");
		}
		catch(Exception ex){
		}
	}
	
	@PostConstruct
	public void init(){
		startWatchAlarm();
	}

	@PreDestroy
	public void beforeDestroy(){
		stopWatchAlarm();
	}
	
	public void startWatchAlarm() {
		working = true;
		ThreadPool.execute(this);
	}
	
	public void stopWatchAlarm() {
		working = false;
	}
	
	public void run() {
		while(working) {
			Map<String, Object> mp = msg.poll();
			if(mp != null) {
				alarm(mp);
			}
			else {
				try {
					Thread.sleep(1000);
				} catch (Exception ex) {}
			}
		}
	}
	
}
