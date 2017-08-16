package com.defu.fcp.atom;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.defu.fcp.atom.AbstractDao;

public abstract class AbstractService implements IAbstractService {
	protected static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Override
	public long count(Map<String, Object> params) {
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
	public int add(Map<String, Object> params) {
		if(!beforeAdd(params)) return 0;
		int i = getDao().add(params);
		afterAdd(params, i);
		return i;
	}

	@Transactional
	@Override
	public int add(List<Map<String, Object>> params) {
		if(!beforeAdd(params)) return 0;
		int c = 0;
		int al = params.size(), max = 300, s = al / max, l;
		if(al % max != 0) s = s + 1;
		for(int i = 0; i < s; i++) {
			l = (i + 1) * max;
			if(l > al) l = al;
			c = c + getDao().addBatch(params.subList(i * max, l));
		}
		
		afterAdd(params, c);
		return c;
	}

	@Override
	public int delete(Map<String, Object> params) {
		if(!beforeDelete(params)) return 0;
		int i = getDao().delete(params);
		afterDelete(params, i);
		return i;
	}

	@Override
	public int update(Map<String, Object> params) {
		if(!beforeUpdate(params)) return 0;
		int i = getDao().update(params);
		afterUpdate(params, i);
		return i;
	}

	@Override
	public int updateList(List<Map<String, Object>> params) {
		if(!beforeUpdate(params)) return 0;
		int i = 0;
		for(Map<String, Object> e: params) i = i + update(e);
		afterUpdate(params, i);
		return i;
	}
	
	public AbstractDao getDao() {
		return null;
	}

	public boolean beforeAdd(Map<String, Object> params) {
		return params != null;
	}

	public boolean beforeAdd(List<Map<String, Object>> params) {
		return params != null;
	}

	public void afterAdd(Map<String, Object> params, int count) {
	}

	public void afterAdd(List<Map<String, Object>> params, int count) {
	}
	
	public boolean beforeDelete(Map<String, Object> params) {
		return params != null;
	}

	public void afterDelete(Map<String, Object> params, int count) {
	}
	
	public boolean beforeUpdate(Map<String, Object> params) {
		return params != null && params.get("obj") != null && params.get("tag") != null;
	}

	public boolean beforeUpdate(List<Map<String, Object>> params) {
		return params != null;
	}

	public void afterUpdate(Map<String, Object> params, int count) {
	}

	public void afterUpdate(List<Map<String, Object>> params, int count) {
	}
	
}
