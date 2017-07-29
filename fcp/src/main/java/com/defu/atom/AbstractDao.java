package com.defu.atom;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface AbstractDao {
	/**
	 * 请求记录总数
	 * @param params 请求参数，针对每个字段<pre>{
	 * 	prop: 指定属性值。指定键且值为null，则查属性为null的记录数；指定键且值为指定值，则查属性为指定值的记录数；不指定则不查该属性
	 *	propList: 指定集合。查属性在某个集合内的记录数，对应sql中的in
	 *	minProp: 指定最小值。查属性值大于指定最小值的记录数，包含最小值
	 *	maxProp: 指定最大值。查属性值对于指定最大值的记录数，不包含最大值
	 *	subProp: 指定包含值。查属性值包含指定值的记录数，针对字符串型字段有效
	 *	startsWithProp: 指定开头值。查属性值以指定值开头，针对字符串型字段有效
	 *	endsWithProp: 指定结尾值。查属性值以指定值结尾，针对字符串型字段有效
	 * }
	 * </pre>
	 * @return 记录条数
	 */
	long count(@Param("params")Map<String, Object> params);
	/**
	 * 请求记录集合
	 * @param params 请求参数，针对每个字段<pre>{
	 * 	prop: 指定属性值。指定键且值为null，则查属性为null的记录数；指定键且值为指定值，则查属性为指定值的记录数；不指定则不查该属性
	 *	propList: 指定集合。查属性在某个集合内的记录数，对应sql中的in
	 *	minProp: 指定最小值。查属性值大于指定最小值的记录数，包含最小值
	 *	maxProp: 指定最大值。查属性值对于指定最大值的记录数，不包含最大值
	 *	subProp: 指定包含值。查属性值包含指定值的记录数，针对字符串型字段有效
	 *	startsWithProp: 指定开头值。查属性值以指定值开头，针对字符串型字段有效
	 *	endsWithProp: 指定结尾值。查属性值以指定值结尾，针对字符串型字段有效
	 * }, 其他参数：
	 * {
	 *	_start:	指定记录下标。
	 *	_limit:	指定记录长度。
	 *	_sort:	指定排序规则。[
	 *		{
	 *			name: 排序字段,
	 *			sort: 排序规则，默认为升序
	 *		}
	 *	]
	 * }
	 * </pre>
	 * @return 记录集合
	 */
	List<Map<String, Object>> list(@Param("params")Map<String, Object> params);
	
	/**
	 * 请求一个单一对象
	 * @param params 请求参数，针对每个字段<pre>{
	 * 	prop: 指定属性值。指定键且值为null，则查属性为null的记录数；指定键且值为指定值，则查属性为指定值的记录数；不指定则不查该属性
	 *	propList: 指定集合。查属性在某个集合内的记录数，对应sql中的in
	 *	minProp: 指定最小值。查属性值大于指定最小值的记录数，包含最小值
	 *	maxProp: 指定最大值。查属性值对于指定最大值的记录数，不包含最大值
	 *	subProp: 指定包含值。查属性值包含指定值的记录数，针对字符串型字段有效
	 *	startsWithProp: 指定开头值。查属性值以指定值开头，针对字符串型字段有效
	 *	endsWithProp: 指定结尾值。查属性值以指定值结尾，针对字符串型字段有效
	 * }, 其他参数：
	 * {
	 *	_sort:	指定排序规则。[
	 *		{
	 *			name: 排序字段,
	 *			sort: 排序规则，默认为升序
	 *		}
	 *	]
	 * }
	 * </pre>
	 * @return 单个记录，若查询到多条，范围第一条
	 */
	Map<String, Object> single(@Param("params")Map<String, Object> params);

	/**
	 * 批量添加对象
	 * @param params 待添加对象，不做动态sql，必须指定所有属性，为空的也需要指定null，不做非空检查
	 * @return 库端影响记录数
	 */
	int addBatch(List<Map<String, Object>> params);

	/**
	 * 添加对象
	 * @param params 待添加对象，动态sql，可指定属性添加，不做非空检查。自增长属性不指定，添加成功后会在参数中指定自增长属性结果值
	 * @return 库端影响记录数
	 */
	int add(@Param("params")Map<String, Object> params);

	/**
	 * 删除对象
	 * @param params 删除参数，针对每个字段<pre>{
	 * 	prop: 指定属性值。指定键且值为null，则查属性为null的记录数；指定键且值为指定值，则查属性为指定值的记录数；不指定则不查该属性
	 *	propList: 指定集合。查属性在某个集合内的记录数，对应sql中的in
	 *	minProp: 指定最小值。查属性值大于指定最小值的记录数，包含最小值
	 *	maxProp: 指定最大值。查属性值对于指定最大值的记录数，不包含最大值
	 *	subProp: 指定包含值。查属性值包含指定值的记录数，针对字符串型字段有效
	 *	startsWithProp: 指定开头值。查属性值以指定值开头，针对字符串型字段有效
	 *	endsWithProp: 指定结尾值。查属性值以指定值结尾，针对字符串型字段有效
	 * }
	 * </pre>
	 * @return 库端影响记录数
	 */
	int delete(@Param("params")Map<String, Object> params);

	/**
	 * 更新对象-伪批量更新
	 * @param params 待更新参数，包含一个大map&lt;obj:{}, tag:{}&gt;；obj为新对象属性；tag为更新目标。<pre>
	 * 新对象属性。动态sql，指定键且值为null，会更新对应字段值为null；指定键值不为null，会更新对应字段为指定值；不指定键，不更新对应字段。
	 * 更新目标参数。，针对每个字段<pre>{
	 * 	prop: 指定属性值。指定键且值为null，则查属性为null的记录数；指定键且值为指定值，则查属性为指定值的记录数；不指定则不查该属性
	 *	propList: 指定集合。查属性在某个集合内的记录数，对应sql中的in
	 *	minProp: 指定最小值。查属性值大于指定最小值的记录数，包含最小值
	 *	maxProp: 指定最大值。查属性值对于指定最大值的记录数，不包含最大值
	 *	subProp: 指定包含值。查属性值包含指定值的记录数，针对字符串型字段有效
	 *	startsWithProp: 指定开头值。查属性值以指定值开头，针对字符串型字段有效
	 *	endsWithProp: 指定结尾值。查属性值以指定值结尾，针对字符串型字段有效
	 * }
	 * </pre>
	 * @return 库端影响记录数
	 */
	int update(Map<String, Object> params);
}
