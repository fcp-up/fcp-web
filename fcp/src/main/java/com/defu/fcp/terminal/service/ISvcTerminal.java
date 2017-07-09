package com.defu.fcp.terminal.service;

import java.util.List;
import java.util.Map;

import com.defu.fcp.base.IAbstractService;

public interface ISvcTerminal extends IAbstractService {
	/**
	 * 终端上下线上报
	 * @param list 终端上下线列表<pre>
	 * {
	 * 	no: 终端编号
	 * 	state: 在线状态。0：离线；1：在线
	 * }
	 * 若列表中终端出现重复，则以最后一条记录为准保存终端的最新在线状态
	 * </pre>
	 * @return 处理结果，每个终端一个结果<pre>
	 * {
	 * 	no: 终端编号
	 * 	flag: 处理结果。0：处理成功；1：数据库访问出错
	 * }
	 * </pre>
	 */
	public Map<String, Object> postOnline(List<Map<String, Object>> list);
}
