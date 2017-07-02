package com.defu.util;

import com.defu.constant.SystemServiceState;

public interface ISystemServiceApi {
	/**
	 * 根据服务名获取一个服务的状态
	 * @param serviceName 服务名
	 * @return 服务状态，如果异常，返回null
	 */
	public SystemServiceState serviceState(String serviceName);
	
}
