package com.defu.fcp.alarm;

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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.defu.atom.db.Database.Alarm;
import com.defu.atom.db.Database.Device;
import com.defu.fcp.ThreadPool;
import com.defu.fcp.device.ISvcDevice;
import com.defu.sms.IPhoneMessage;

@Service
public class SvcAlarm extends com.defu.atom.service.impl.SvcAlarm implements Runnable, ISvcAlarm {
	@Autowired @Qualifier("catsms") IPhoneMessage catsms;
	@Autowired @Qualifier("yunpiansms") IPhoneMessage yunpiansms;
	@Autowired ISvcDevice devsvc;

	// 编码格式。发送编码格式统一用UTF-8
	private static String ENCODING = "UTF-8";
	
	protected final Log log = LogFactory.getLog(getClass());
	private int queueSize = 1000;
	private Queue<Map<String, Object>> msg = new ConcurrentLinkedQueue<>();
	boolean working = false;
	
	@Override
	public boolean beforeAdd(List<Map<String, Object>> params) {
		if(params == null || params.size() < 1) return false;

		Map<String, Object> mp = new HashMap<>();
		String tel;
		
		for(Map<String, Object> e: params) {
			if(e.get(Alarm.time.prop) != null) e.put(Alarm.time.prop, new Date(Long.parseLong(e.get(Alarm.time.prop).toString())));
			if(!e.containsKey(Alarm.id.prop)) e.put(Alarm.id.prop, null);
			if(!e.containsKey(Alarm.deviceNo.prop)) e.put(Alarm.deviceNo.prop, null);
			if(!e.containsKey(Alarm.terminalNo.prop)) e.put(Alarm.terminalNo.prop, null);
			if(!e.containsKey(Alarm.isAlarm.prop)) e.put(Alarm.isAlarm.prop, null);
			if(!e.containsKey(Alarm.pressure.prop)) e.put(Alarm.pressure.prop, null);
			if(!e.containsKey(Alarm.time.prop)) e.put(Alarm.time.prop, new Date());
			if(!e.containsKey(Alarm.state.prop)) e.put(Alarm.state.prop, null);
			if(!e.containsKey(Alarm.sendTime.prop)) e.put(Alarm.sendTime.prop, null);
			if(!e.containsKey(Alarm.toPhone.prop)) e.put(Alarm.toPhone.prop, null);
			if(!e.containsKey(Alarm.deviceSignal.prop)) e.put(Alarm.deviceSignal.prop, null);
			
			tel = (String)e.get(Alarm.toPhone.prop);

			if(tel == null || tel.trim().length() < 1) {
				mp.clear();
				mp.put(Device.no.prop, e.get(Alarm.deviceNo.prop));
				mp = devsvc.single(mp);
				
				if(mp == null) continue;
				e.put(Alarm.toPhone.prop, mp.get(Device.alarmPhone.prop));
			}

			if (log.isDebugEnabled())
				log.debug("请求告警：" + e);
			if(msg.size() > queueSize) {
				//队列满了，移除第一条指令
				if (log.isDebugEnabled())
					log.debug("请求告警：" + e + "\t队列已满，移除队列中第一条告警");
				msg.poll();
			}
			
			msg.offer(e);
			
		}
		
		return true;
	}
	
	private boolean needAlarm(final Map<String, Object> mp) {
		return "1".equals(mp.get(Alarm.isAlarm.prop).toString());
	}
	
	private void alarm(final Map<String, Object> mp) {
		if(!needAlarm(mp)) return;
		final List<String> phonenos = new ArrayList<>();
		String tel = (String)mp.get(Alarm.toPhone.prop);
		
		if(tel != null && tel.trim().length() > 0) {
			phonenos.addAll(Arrays.asList(mp.get(Alarm.toPhone.prop).toString().split(",")));
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
			sb.append(mp.get(Alarm.deviceNo.prop).toString());
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
			sb.append(URLEncoder.encode(mp.get(Alarm.deviceNo.prop).toString(), ENCODING));
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
			sb.append(URLEncoder.encode(mp.get(Alarm.deviceNo.prop).toString(), ENCODING));
			
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
