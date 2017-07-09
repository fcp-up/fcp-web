package com.defu.fcp.base;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractService implements IAbstractService {
	
	@Override
	public int count(Map<String, Object> params) {
		if(params == null) params = new HashMap<>();
		return getDao().count(params);
	}

	@Override
	public List<Map<String, Object>> list(Map<String, Object> params) {
		if(params == null) params = new HashMap<>();
		return getDao().list(params);
	}

	@Override
	public Map<String, Object> single(Map<String, Object> params) {
		if(params == null) params = new HashMap<>();
		return getDao().single(params);
	}

	@Override
	public Map<String, Object> add(Map<String, Object> params) {
		Map<String, Object> rst = new HashMap<>();
		beforeAdd(params);
		int i = params == null ? 0 : getDao().add(params);
		rst.put("count", i);
		afterAdd(params, i, rst);
		return rst;
	}

	@Override
	public Map<String, Object> add(List<Map<String, Object>> params) {
		Map<String, Object> rst = new HashMap<>();
		beforeAdd(params);
		int i = params == null ? 0 : getDao().addBatch(params);
		rst.put("count", i);
		afterAdd(params, i, rst);
		return rst;
	}

	@Override
	public int delete(Map<String, Object> params) {
		int i = params == null ? 0 : getDao().delete(params);
		afterDelete(params, i);
		return i;
	}

	@Override
	public int update(Map<String, Object> params) {
		int i = params == null ? 0 : getDao().update(params);
		afterUpdate(params, i);
		return i;
	}

	@Override
	public int updateList(List<Map<String, Object>> params) {
		if(params == null) return 0;
		int i = 0;
		for(Map<String, Object> e: params) i = i + update(e);
		return i;
	}
	
	public AbstractDao getDao() {
		return null;
	}

	/**
	 * 添加前动作
	 * @param params 被添加的对象
	 * @param count 影响数据库的记录数
	 * @param rst 添加的执行结果
	 */
	public void beforeAdd(Map<String, Object> params) {
	}

	/**
	 * 添加前动作
	 * @param params 被添加的对象
	 * @param count 影响数据库的记录数
	 * @param rst 添加的执行结果
	 */
	public void beforeAdd(List<Map<String, Object>> params) {
	}

	/**
	 * 添加后动作
	 * @param params 被添加的对象
	 * @param count 影响数据库的记录数
	 * @param rst 添加的执行结果
	 */
	public void afterAdd(Map<String, Object> params, int count, Map<String, Object> rst) {
	}

	/**
	 * 添加后动作
	 * @param params 被添加的对象
	 * @param count 影响数据库的记录数
	 * @param rst 添加的执行结果
	 */
	public void afterAdd(List<Map<String, Object>> params, int count, Map<String, Object> rst) {
	}
	
	public void afterDelete(Map<String, Object> params, int count) {
	}

	public void afterUpdate(Map<String, Object> params, int count) {
	}
	
}
