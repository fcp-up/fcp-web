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

import com.defu.fcp.HttpSocketServer;
import com.defu.fcp.ThreadPool;
import com.defu.fcp.alarm.dao.IDaoAlarmExt;
import com.defu.fcp.atom.db.Database.*;
import com.defu.fcp.device.ISvcDevice;
import com.defu.fcp.terminal.ISvcTerminal;
import com.defu.sms.IPhoneMessage;

@Service
public class SvcAlarm extends com.defu.fcp.atom.service.impl.SvcAlarm implements Runnable, ISvcAlarm {
	@Autowired @Qualifier("catsms") IPhoneMessage catsms;
	@Autowired @Qualifier("yunpiansms") IPhoneMessage yunpiansms;
	@Autowired ISvcTerminal termsvc;
	@Autowired ISvcDevice devsvc;
	@Autowired IDaoAlarmExt dao;

	// 编码格式。发送编码格式统一用UTF-8
	private static String ENCODING = "UTF-8";
	
	protected final Log log = LogFactory.getLog(getClass());
	private int queueSize = 1000;
	private Queue<Map<String, Object>> msg = new ConcurrentLinkedQueue<>();
	boolean working = false;
	
	public List<Map<String, Object>> deviceAlarmList(Map<String, Object> params) {
		return dao.deviceAlarmList(params);
	}
	
	public long deviceAlarmCount(Map<String, Object> params) {
		return dao.deviceAlarmCount(params);
	}
	
	@Override
	public boolean beforeAdd(List<Map<String, Object>> params) {
		boolean rst = super.beforeAdd(params);
		if(rst) {
			Map<String, Object> mp = new HashMap<>();
			String tel;
			
			for(Map<String, Object> e: params) {
				//终端处理
				mp.clear();
				mp.put(Terminal.no.prop, e.get(Alarm.terminalNo.prop));
				if(termsvc.list(mp).size() < 1) {
					//终端不存在库中
					termsvc.add(mp);
				}
				
				//设备处理
				mp.clear();
				mp.put(Device.no.prop, e.get(Alarm.deviceNo.prop));
				mp.put(Device.terminalNo.prop, e.get(Alarm.terminalNo.prop));
				if(devsvc.list(mp).size() < 1) {
					//终端不存在库中
					mp.put(Device.lastAlarmTime.prop, dateFormat.format(new Date()));
					mp.put(Device.lastSignal.prop, e.get(Alarm.deviceSignal.prop));
					devsvc.add(mp);
				}
				else {
					//更新设备状态
					Map<String, Object> obj = new HashMap<>(), upd = new HashMap<>();

					obj.put(Device.lastAlarmTime.prop, dateFormat.format(new Date()));
					obj.put(Device.lastSignal.prop, e.get(Alarm.deviceSignal.prop));
					upd.put("obj", obj);
					
					upd.put("tag", mp);
					
					devsvc.update(upd);
				}
				
				//报警时间
				if(!e.containsKey(Alarm.time.prop)) e.put(Alarm.time.prop, dateFormat.format(new Date()));
				
				//报警电话
				tel = (String)e.get(Alarm.toPhone.prop);
				if(tel == null || tel.trim().length() < 1) {
					//未指定报警电话，采用终端报警电话
					mp.clear();
					mp.put(Terminal.no.prop, e.get(Alarm.terminalNo.prop));
					mp = termsvc.single(mp);
					
					if(mp == null) continue;
					e.put(Alarm.toPhone.prop, mp.get(Terminal.alarmPhone.prop));
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

				//通知到web客户端
				//获取设备
				mp.clear();
				mp.put(Device.no.prop, e.get(Alarm.deviceNo.prop));
				mp = devsvc.single(mp);
				
				HttpSocketServer.notifyDeviceAlarm(mp, e);

			}
		}

		return true;
	}
	
	private boolean needAlarm(final Map<String, Object> mp) {
		return "1".equals(mp.get(Alarm.isAlarm.prop).toString());
	}
	
	private void alarm(final Map<String, Object> mp) {
		if(!needAlarm(mp)) return;
		final List<String> phonenos = new ArrayList<>();
		final Map<String, Boolean> rst = new HashMap<>();
		
		String tel = (String)mp.get(Alarm.toPhone.prop);
		if(tel != null && tel.trim().length() > 0) {
			phonenos.addAll(Arrays.asList(mp.get(Alarm.toPhone.prop).toString().split(",")));
		}
		
		if(phonenos.size() < 1) return;
		
		rst.put("txt", false);
		rst.put("phonic", false);
		
		//通过云片发送语音短信
		ThreadPool.execute(new Runnable(){
			public void run(){
				alarmPhonicByYunpian(mp, phonenos);
				synchronized (rst) {
					rst.put("phonic", true);
					if(rst.get("txt")) {
						sendedAlarm(mp, phonenos);
					}
					
				}
			}
		});

		//通过短信猫发送文本短信
		ThreadPool.execute(new Runnable(){
			public void run(){
				alarmTextByCat(mp, phonenos);
				synchronized (rst) {
					rst.put("txt", true);
					if(rst.get("phonic")) {
						sendedAlarm(mp, phonenos);
					}
					
				}
			}
		});
	}
	
	protected void sendedAlarm(Map<String, Object> alarm, List<String> phonenos) {
		Object id = alarm.get(Alarm.id.prop);
		if(id != null) {
			Map<String, Object> obj = new HashMap<>(), tag = new HashMap<>(), upd = new HashMap<>();
			
			obj.put(Alarm.sendTime.prop, dateFormat.format(new Date()));
			obj.put(Alarm.toPhone.prop, alarm.get(Alarm.toPhone.prop));
			upd.put("obj", obj);
			
			tag.put(Alarm.id.prop, id);
			upd.put("tag", tag);
			
			getDao().update(upd);
		}
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
