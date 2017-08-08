package com.defu.fcp.alarm.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface IDaoAlarmExt {
	List<Map<String, Object>> deviceAlarmList(@Param("params")Map<String, Object> params);
	long deviceAlarmCount(@Param("params")Map<String, Object> params);
}
