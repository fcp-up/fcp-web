package com.defu.fcp.terminal;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.defu.atom.db.Database.Device;
import com.defu.atom.db.Database.Terminal;
import com.defu.atom.db.Database.Terminalonlinercd;
import com.defu.atom.service.ISvcTerminalonlinercd;
import com.defu.fcp.device.ISvcDevice;

@Service
public class SvcTerminal extends com.defu.atom.service.impl.SvcTerminal implements ISvcTerminal {
	@Autowired ISvcTerminalonlinercd rcdsvc;
	@Autowired ISvcDevice devsvc;
	
	public Map<String, Object> postOnline(List<Map<String, Object>> list) {
		Map<String, Object> params = new HashMap<>(), rst = new HashMap<>();
		String no;
		
		for(Map<String, Object> t: list) {
			no = (String)t.get(Terminalonlinercd.terminalNo.prop);
			if(no == null) continue;
			
			try{
				params.clear();
				params.put(Terminal.no.prop, no);
				if(dao.list(params).size() < 1) {
					//终端不存在库中
					params.put(Terminal.lastOnlineState.prop, t.get(Terminalonlinercd.state.prop));
					params.put(Terminal.no.prop, t.get(Terminalonlinercd.terminalNo.prop));
					getDao().add(params);
				}
				
				if(t.get(Terminalonlinercd.time.prop) != null) {
					t.put(Terminalonlinercd.time.prop, new Date(Long.parseLong(t.get(Terminalonlinercd.time.prop).toString())));
				}
				else {
					t.put(Terminalonlinercd.time.prop, new Date());
				}
				rcdsvc.add(t);
				
				rst.put(no, 0);
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
