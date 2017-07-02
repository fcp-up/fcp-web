package com.defu.fcp.alarm.service;

import com.defu.fcp.base.IAbstractService;

public interface ISvcAlarm extends IAbstractService {
	void startWatchAlarm();
	void stopWatchAlarm();
}
