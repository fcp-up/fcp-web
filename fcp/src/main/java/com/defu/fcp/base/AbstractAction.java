package com.defu.fcp.base;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.defu.fcp.Const;
import com.defu.util.Tools;
import com.fasterxml.jackson.core.JsonProcessingException;

public abstract class AbstractAction {
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void setCommon(Model m, HttpSession session, HttpServletRequest req, HttpServletResponse rsp) {
		Object o = session.getAttribute(Const.SessionKey.curUser);
		if(o != null) {
			try {
				m.addAttribute("usrStr", Tools.mapToJsonStr((Map)o));
			} catch (JsonProcessingException e) {
			}
			m.addAttribute("usr", o);
		}
		m.addAttribute("sessionId", session.getId());
		m.addAttribute("serverTime", System.currentTimeMillis());
	}

	
	protected Map<String, Object> countParams(Map<String, Object> params) {
		return params;
	}

	protected Map<String, Object> listParams(Map<String, Object> params) {
		return params;
	}

	protected Map<String, Object> singleParams(Map<String, Object> params) {
		return params;
	}

	protected Map<String, Object> addParams(Map<String, Object> params) {
		return params;
	}

	protected List<Map<String, Object>> addParams(List<Map<String, Object>> params) {
		return params;
	}

	protected Map<String, Object> deleteParams(Map<String, Object> params) {
		return params;
	}

	protected Map<String, Object> updateParams(Map<String, Object> params) {
		return params;
	}

	protected List<Map<String, Object>> updateParams(List<Map<String, Object>> params) {
		return params;
	}

	/**
	 * 请求一个列表长度
	 * @param params 请求参数，json串
	 * @return map对象<pre>
	 * 	state: 	状态码，0表示成功，1表示访问库错误,
	 * 	data:	成功时为请求的数据，失败时为数据库异常信息
	 * </pre>
	 */
	@RequestMapping(value = "count", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> count(@RequestParam(value = "params", required = false) String params, HttpSession session, HttpServletRequest req, HttpServletResponse rsp) {
		
		Map<String, Object> rst = new HashMap<>();
		try{
			rst.put("data", getService().count(this.countParams(Tools.jsonStrtoMap(params))));
			rst.put("state", 0);
		}
		catch(Exception ex){
			rst.put("data", ex.getMessage());
			rst.put("state", 1);
		}
		return rst;
	}

	/**
	 * 请求一个列表
	 * @param params 请求参数，json串
	 * @return map对象<pre>
	 * 	state: 	状态码，0表示成功，1表示访问库错误,
	 * 	data:	成功时为请求的数据，失败时为数据库异常信息
	 * 	total:	成功时为请求的记录数，失败时为空，无意义
	 * </pre>
	 */
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> list(@RequestParam(value = "params", required = false) String params, HttpSession session, HttpServletRequest req, HttpServletResponse rsp) {
		Map<String, Object> rst = new HashMap<>();
		try{
			IAbstractService svc = getService();
			Map<String, Object> p = this.listParams(Tools.jsonStrtoMap(params));
			rst.put("data", svc.list(p));
			rst.put("total", svc.count(p));
			rst.put("state", 0);
		}
		catch(Exception ex){
			rst.put("data", ex.getMessage());
			rst.put("state", 1);
		}
		return rst;
	}

	/**
	 * 请求一个对象
	 * @param params 请求参数，json串
	 * @return map对象<pre>
	 * 	state: 	状态码，0表示成功，1表示访问库错误,
	 * 	data:	成功时为请求的数据，失败时为数据库异常信息
	 * </pre>
	 */
	@RequestMapping(value = "", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> single(@RequestParam(value = "params", required = false) String params, HttpSession session, HttpServletRequest req, HttpServletResponse rsp) {
		Map<String, Object> rst = new HashMap<>();
		try{			
			rst.put("data", getService().single(this.singleParams(Tools.jsonStrtoMap(params))));
			rst.put("state", 0);
		}
		catch(Exception ex){
			rst.put("data", ex.getMessage());
			rst.put("state", 1);
		}
		return rst;
	}

	/**
	 * 添加一个对象
	 * @param params 待添加对象，json串
	 * @return map对象<pre>
	 * 	state: 	状态码，0表示成功，1表示访问库错误,
	 * 	data:	成功时为影响的记录行数，失败为数据库异常信息
	 * </pre>
	 */
	@RequestMapping(value = "", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> add(@RequestParam(value = "params", required = false) String params, HttpSession session, HttpServletRequest req, HttpServletResponse rsp) {
		Map<String, Object> rst = new HashMap<>();
		try{			
			rst.put("data", getService().add(this.addParams(Tools.jsonStrtoMap(params))));
			rst.put("state", 0);
		}
		catch(Exception ex){
			rst.put("data", ex.getMessage());
			rst.put("state", 1);
		}
		return rst;
	}

	/**
	 * 添加一批对象
	 * @param params 待添加对象，json串
	 * @return map对象<pre>
	 * 	state: 	状态码，0表示成功，1表示访问库错误,
	 * 	data:	成功时为影响的记录行数，失败为异常信息
	 * </pre>
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "list", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> addBatch(@RequestParam(value = "params", required = false) String params, HttpSession session, HttpServletRequest req, HttpServletResponse rsp) {
		Map<String, Object> rst = new HashMap<>();
		try{
			rst.put("data", getService().add(this.addParams((List<Map<String,Object>>)Tools.jsonStrtoList(params))));
			rst.put("state", 0);
		}
		catch(Exception ex){
			rst.put("data", ex.getMessage());
			rst.put("state", 1);
		}
		return rst;
	}

	/**
	 * 删除对象
	 * @param params 待删除对象，json串
	 * @return map对象<pre>
	 * 	state: 	状态码，0表示成功，1表示访问库错误,
	 * 	data:	成功时为影响的记录行数，失败为数据库异常信息
	 * </pre>
	 */
	@RequestMapping(value = "", method = RequestMethod.DELETE)
	public @ResponseBody Map<String, Object> delete(@RequestParam(value = "params", required = false) String params, HttpSession session, HttpServletRequest req, HttpServletResponse rsp) {
		Map<String, Object> rst = new HashMap<>();
		try{			
			rst.put("data", getService().delete(this.deleteParams(Tools.jsonStrtoMap(params))));
			rst.put("state", 0);
		}
		catch(Exception ex){
			rst.put("data", ex.getMessage());
			rst.put("state", 1);
		}
		return rst;
	}

	/**
	 * 更新对象
	 * @param params 待更新参数，json串，包含一个大map&lt;obj:{}, tag:{}&gt;；obj为新对象属性；tag为更新目标
	 * @return map对象<pre>
	 * 	state: 	状态码，0表示成功，1表示访问库错误,
	 * 	data:	成功时为影响的记录行数，失败为数据库异常信息
	 * </pre>
	 */
	@RequestMapping(value = "", method = RequestMethod.PUT)
	public @ResponseBody Map<String, Object> update(@RequestParam(value = "params", required = false) String params, HttpSession session, HttpServletRequest req, HttpServletResponse rsp) {
		Map<String, Object> rst = new HashMap<>();
		try{			
			rst.put("data", getService().update(this.updateParams(Tools.jsonStrtoMap(params))));
			rst.put("state", 0);
		}
		catch(Exception ex){
			rst.put("data", ex.getMessage());
			rst.put("state", 1);
		}
		return rst;
	}

	/**
	 * 批量更新对象
	 * @param params 待更新参数，json串，包含一个大map&lt;obj:{}, tag:{}&gt;的集合；obj为新对象属性；tag为更新目标
	 * @return map对象<pre>
	 * 	state: 	状态码，0表示成功，1表示访问库错误,
	 * 	data:	成功时为影响的记录行数，失败为数据库异常信息
	 * </pre>
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "list", method = RequestMethod.PUT)
	public @ResponseBody Map<String, Object> updateList(@RequestParam(value = "params", required = false) String params, HttpSession session, HttpServletRequest req, HttpServletResponse rsp) {
		Map<String, Object> rst = new HashMap<>();
		try{			
			rst.put("data", getService().updateList(this.updateParams((List<Map<String,Object>>)Tools.jsonStrtoList(params))));
			rst.put("state", 0);
		}
		catch(Exception ex){
			rst.put("data", ex.getMessage());
			rst.put("state", 1);
		}
		return rst;
	}


	public IAbstractService getService() {
		return null;
	}
}
