package com.defu.fcp.base;

import java.util.List;
import java.util.Map;

public interface IAbstractService {
	/**
	 * 请求记录总数
	 * @param params 请求参数
	 * @return
	 */
	int count(Map<String, Object> params);
	/**
	 * 请求一个列表
	 * @param params 请求参数
	 * @return map对象列表
	 */
	List<Map<String, Object>> list(Map<String, Object> params);
	
	/**
	 * 请求一个对象
	 * @param params 请求参数
	 * @return map对象
	 */
	Map<String, Object> single(Map<String, Object> params);

	/**
	 * 批量添加对象
	 * @param params 待添加对象
	 * @return 返回一个处理结果的map<pre>
	 * {
	 * 	count: 影响数据库的记录数
	 * 	...: 自定义
	 * }
	 * </pre>
	 */
	Map<String, Object> add(List<Map<String, Object>> params);

	/**
	 * 添加对象
	 * @param params 待添加对象
	 * @return 返回一个处理结果的map<pre>
	 * {
	 * 	count: 影响数据库的记录数
	 * 	...: 自定义
	 * }
	 * </pre>
	 */
	Map<String, Object> add(Map<String, Object> params);

	/**
	 * 删除对象
	 * @param params 待删除对象
	 * @return 库端影响记录数
	 */
	int delete(Map<String, Object> params);

	/**
	 * 更新对象
	 * @param params 待更新参数，包含一个大map&lt;obj:{}, tag:{}&gt;；obj为新对象属性；tag为更新目标
	 * @return 库端影响记录数
	 */
	int update(Map<String, Object> params);

	/**
	 * 批量更新对象
	 * @param params 待更新参数，包含一个大map&lt;obj:{}, tag:{}&gt;；obj为新对象属性；tag为更新目标
	 * @return 库端影响记录数
	 */
	int updateList(List<Map<String, Object>> params);

}
