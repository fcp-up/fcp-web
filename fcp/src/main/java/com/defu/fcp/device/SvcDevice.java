package com.defu.fcp.device;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.defu.fcp.atom.db.Database.Device;

@Service
public class SvcDevice extends com.defu.fcp.atom.service.impl.SvcDevice implements ISvcDevice {

	@Override
	public Map<String, Object> setAlarmPhone(List<Map<String, Object>> list) {
		Map<String, Object> params = new HashMap<>(), rst = new HashMap<>(), mp;
		String no;
		
		for(Map<String, Object> t: list) {
			no = (String)t.get(Device.no.prop);
			if(no == null) continue;
			
			try{
				mp = new HashMap<>();
				mp.put(Device.no.prop, no);
				mp.put(Device.terminalNo.prop, t.get(Device.terminalNo.prop));
				params.put("tag", mp);

				mp = new HashMap<>();
				mp.put(Device.alarmPhone.prop, t.get(Device.alarmPhone.prop));
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
