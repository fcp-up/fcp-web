package com.defu.fcp.terminal;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.defu.fcp.HttpSocketServer;
import com.defu.fcp.atom.db.Database.Device;
import com.defu.fcp.atom.db.Database.Terminal;
import com.defu.fcp.atom.db.Database.Terminalonlinercd;
import com.defu.fcp.atom.service.ISvcTerminalonlinercd;
import com.defu.fcp.device.ISvcDevice;

@Service
public class SvcTerminal extends com.defu.fcp.atom.service.impl.SvcTerminal implements ISvcTerminal {
	@Autowired ISvcTerminalonlinercd rcdsvc;
	@Autowired ISvcDevice devsvc;
	
	public Map<String, Object> postOnline(List<Map<String, Object>> list) {
		Map<String, Object> params = new HashMap<>(), rst = new HashMap<>();
		String no;
		String time;
		
		for(Map<String, Object> t: list) {
			no = (String)t.get(Terminalonlinercd.terminalNo.prop);
			if(no == null) continue;
			
			time = dateFormat.format(t.get(Terminalonlinercd.time.prop) != null ? new Date(Long.parseLong(t.get(Terminalonlinercd.state.prop).toString())) : new Date());
			
			try{
				params.clear();
				params.put(Terminal.no.prop, no);
				if(dao.list(params).size() < 1) {
					//终端不存在库中
					params.put(Terminal.lastOnlineState.prop, t.get(Terminalonlinercd.state.prop));
					params.put(Terminal.lastSignal.prop, t.get(Terminalonlinercd.terminalSignal.prop));
					params.put(Terminal.lastOnlineTime.prop, time);
					getDao().add(params);
				}
				else {
					//更新最近一次上线时间和上下线状态
					Map<String, Object> upd = new HashMap<>(), obj = new HashMap<>();
					
					obj.put(Terminal.lastOnlineState.prop, t.get(Terminalonlinercd.state.prop));
					obj.put(Terminal.lastSignal.prop, t.get(Terminalonlinercd.terminalSignal.prop));
					obj.put(Terminal.lastOnlineTime.prop, time);
					
					upd.put("tag", params);
					upd.put("obj", obj);
					dao.update(upd);
				}
				
				//添加终端上下线记录
				t.put(Terminalonlinercd.time.prop, time);
				rcdsvc.add(t);
				
				rst.put(no, 0);

				//通知到web客户端
				//获取设备
				params.clear();
				params.put(Terminal.no.prop, no);
				params = single(params);
				
				HttpSocketServer.notifyTermOnline(params, t);

			}
			catch(Exception ex){
				rst.put(no, 1);
			}
			
		}
		
		return rst;
	}

	@Override
	public Map<String, Object> setAlarmPhone(List<Map<String, Object>> list) {
		Map<String, Object> params = new HashMap<>(), rst = new HashMap<>(), mp;
		String no;
		
		for(Map<String, Object> t: list) {
			no = (String)t.get(Device.terminalNo.prop);
			if(no == null) continue;
			
			try{
				mp = new HashMap<>();
				mp.put(Device.terminalNo.prop, no);
				params.put("tag", mp);

				mp = new HashMap<>();
				mp.put(Device.alarmPhone.prop, t.get(Device.alarmPhone.prop));
				params.put("obj", mp);
				
				devsvc.update(params);

				
				mp = new HashMap<>();
				mp.put(Terminal.no.prop, no);
				params.put("tag", mp);

				mp = new HashMap<>();
				mp.put(Terminal.alarmPhone.prop, t.get(Device.alarmPhone.prop));
				params.put("obj", mp);
				
				update(params);
				
				rst.put(no, 0);
			}
			catch(Exception ex){
				rst.put(no, 1);
			}
			
		}
		
		return rst;
	}
	
	
}
