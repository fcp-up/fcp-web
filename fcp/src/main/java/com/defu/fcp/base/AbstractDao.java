package com.defu.fcp.base;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface AbstractDao {
	/**
	 * 请求记录总数
	 * @param params 请求参数
	 * @return
	 */
	int count(@Param("params")Map<String, Object> params);
	/**
	 * 请求一个列表
	 * @param params 请求参数
	 * @return map对象列表
	 */
	List<Map<String, Object>> list(@Param("params")Map<String, Object> params);
	
	/**
	 * 请求一个对象
	 * @param params 请求参数
	 * @return map对象
	 */
	Map<String, Object> single(@Param("params")Map<String, Object> params);

	/**
	 * 批量添加对象
	 * @param params 待添加对象
	 * @return 库端影响记录数
	 */
	int addBatch(@Param("params")List<Map<String, Object>> params);

	/**
	 * 添加对象
	 * @param params 待添加对象
	 * @return 库端影响记录数
	 */
	int add(@Param("params")Map<String, Object> params);

	/**
	 * 删除对象
	 * @param params 待删除对象
	 * @return 库端影响记录数
	 */
	int delete(@Param("params")Map<String, Object> params);

	/**
	 * 更新对象-伪批量更新
	 * @param params 待更新参数，包含一个大map&lt;obj:{}, tag:{}&gt;；obj为新对象属性；tag为更新目标
	 * @return 库端影响记录数
	 */
	int update(Map<String, Object> params);

}
