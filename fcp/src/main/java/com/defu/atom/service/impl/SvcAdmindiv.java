package com.defu.atom.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.defu.atom.db.Database.Admindiv;

import com.defu.atom.AbstractDao;
import com.defu.atom.AbstractService;
import com.defu.atom.dao.IDaoAdmindiv;
import com.defu.atom.service.ISvcAdmindiv;

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
public class SvcAdmindiv extends AbstractService implements ISvcAdmindiv {
	@Autowired protected IDaoAdmindiv dao;

	public AbstractDao getDao() {
		return dao;
	}

	public boolean beforeAdd(List<Map<String, Object>> params) {
		if(params == null || params.size() < 1) return false;
		for(Map<String, Object> e: params) {
			if(!e.containsKey(Admindiv.no.prop)) e.put(Admindiv.no.prop, null);
			if(!e.containsKey(Admindiv.name.prop)) e.put(Admindiv.name.prop, null);
			if(!e.containsKey(Admindiv.parentNo.prop)) e.put(Admindiv.parentNo.prop, null);
			if(!e.containsKey(Admindiv.level.prop)) e.put(Admindiv.level.prop, null);
			if(!e.containsKey(Admindiv.kind.prop)) e.put(Admindiv.kind.prop, null);
		}
		return true;
	}

	
	

	
}
