package com.defu.fcp.alarm;

import java.util.List;
import java.util.Map;

public interface ISvcAlarm extends com.defu.atom.service.ISvcAlarm {
	void startWatchAlarm();
	void stopWatchAlarm();
	
	List<Map<String, Object>> deviceAlarmList(Map<String, Object> params);
	long deviceAlarmCount(Map<String, Object> params);
}
