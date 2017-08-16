package com.defu.fcp.device;

import java.util.List;
import java.util.Map;

public interface ISvcDevice extends com.defu.fcp.atom.service.ISvcDevice {

	/**
	 * 设置报警电话
	 * @param list 报警电话列表<pre>
	 * {
	 * 	deviceNo: 设备编号
	 * 	phoneNo: 电话号码，多个之间以英文逗号隔开
	 * }
	 * 若列表中设备出现重复，则以最后一条记录为准保存终端的最新在线状态
	 * </pre>
	 * @return 处理结果，每个终端一个结果<pre>
	 * {
	 * 	deviceNo: 设备编号
	 * 	code: 处理结果。0：处理成功；1：数据库访问出错
	 * }
	 * </pre>
	 * @return
	 */
	Map<String, Object> setAlarmPhone(List<Map<String, Object>> list);
}
