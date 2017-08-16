package com.defu.fcp.atom.dao;

import com.defu.fcp.atom.AbstractDao;

/**
 * 所有键<pre>{
 * no	f_no	区划编号,
 * name	f_name	区划名称,
 * parentNo	f_parentNo	上级区划编号,
 * level	f_level	区划级别。
1：省级（省、自治区、直辖市）；
2：地级（地级市、地区）；
3：县级（县、县级市、市辖区）；
4：乡级（乡、镇、街道、类似乡级单位）；
5：村级（村民委员会、居民委员会、类似村民委员会、类似居民委员会）；,
 * kind	f_kind	区划类型。
111：表示主城区；
112：表示城乡结合区；
121：表示镇中心区；
122：表示镇乡结合区；
123：表示特殊区域；
210：表示乡中心区；
220：表示村庄；'
 * }</pre>
 */
public interface IDaoAdmindiv extends AbstractDao {}
