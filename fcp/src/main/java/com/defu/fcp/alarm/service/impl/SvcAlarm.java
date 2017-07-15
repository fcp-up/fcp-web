package com.defu.fcp.alarm.service.impl;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.defu.fcp.ThreadPool;
import com.defu.fcp.alarm.attr.Alarm;
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
	@Autowired @Qualifier("catsms") IPhoneMessage catsms;
	@Autowired @Qualifier("yunpiansms") IPhoneMessage yunpiansms;

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
			e.put(Alarm.crtTime, new Date());
			detail.put(e, add(e));
			i++;
		}
		rst.put("detail", detail);
		rst.put("count", i);
		return rst;
	}

	public void beforeAdd(Map<String, Object> params) {
		String tel = (String)params.get(Alarm.toTelephone);
		
		if(tel == null || tel.trim().length() < 1) {
			final List<String> phonenos = new ArrayList<>();
			List<Map<String, Object>> phones = phoneSvc.list(new HashMap<String, Object>());
			for(Map<String, Object> p: phones) phonenos.add(p.get(com.defu.fcp.phone.attr.Phone.phoneNo).toString());
			params.put(Alarm.toTelephone, StringUtils.join(phonenos.toArray(), ","));
		}
		
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
	
	private boolean needAlarm(final Map<String, Object> mp) {
		return "1".equals(mp.get(Alarm.isAlarm).toString());
	}
	
	private void alarm(final Map<String, Object> mp) {
		if(!needAlarm(mp)) return;
		final List<String> phonenos = new ArrayList<>();
		String tel = (String)mp.get(Alarm.toTelephone);
		
		if(tel != null && tel.trim().length() > 0) {
			phonenos.addAll(Arrays.asList(mp.get(Alarm.toTelephone).toString().split(",")));
		}
		
		if(phonenos.size() < 1) return;
		
		//通过云片发送语音短信
		ThreadPool.execute(new Runnable(){
			public void run(){
				alarmPhonicByYunpian(mp, phonenos);
			}
		});

		//通过短信猫发送文本短信
		ThreadPool.execute(new Runnable(){
			public void run(){
				alarmTextByCat(mp, phonenos);
			}
		});
	}
	
	protected void alarmTextByCat(Map<String, Object> mp, List<String> phoneNo) {
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append(mp.get(Alarm.deviceNo).toString());
			sb.append("发生险情");
			
			for(String no: phoneNo) {
				catsms.sendTextMsg(sb.toString(), no);
			}
			
		}
		catch(Exception ex){
		}
	}
	
	protected void alarmPhonicByCat(Map<String, Object> mp, List<String> phoneNo) {
		return;
	}
	
	protected void alarmTextByYunpian(Map<String, Object> mp, List<String> phoneNo) {
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append(URLEncoder.encode("#devicename#", ENCODING));
			sb.append("=");
			sb.append(URLEncoder.encode(mp.get(Alarm.deviceNo).toString(), ENCODING));
			sb.append("&");
			sb.append(URLEncoder.encode("#currentstatus#", ENCODING));
			sb.append("=");
			sb.append(URLEncoder.encode("发生险情", ENCODING));
			
			for(String no: phoneNo) {
				yunpiansms.sendTextMsg(sb.toString(), no, "1842546");
			}
			
		}
		catch(Exception ex){
		}
	}
	
	protected void alarmPhonicByYunpian(Map<String, Object> mp, List<String> phoneNo) {
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append(URLEncoder.encode("devicename", ENCODING));
			sb.append("=");
			sb.append(URLEncoder.encode(mp.get(Alarm.deviceNo).toString(), ENCODING));
			
			yunpiansms.sendPhonicMsg(URLEncoder.encode(sb.toString(), ENCODING), phoneNo, "1842616");
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
				try {
					alarm(mp);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				try {
					//休息3秒，防止大量短信爆击，导致短信猫挂了。
					Thread.sleep(3000);
				} catch (Exception ex) {}
			}
			else {
				try {
					Thread.sleep(1000);
				} catch (Exception ex) {}
			}
		}
	}
	
}
