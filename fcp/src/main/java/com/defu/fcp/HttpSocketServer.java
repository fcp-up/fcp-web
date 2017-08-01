package com.defu.fcp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import com.defu.util.Tools;

@ServerEndpoint(value = "/ws/socket/{sessionId}")
public class HttpSocketServer {
	/**
	 * 所有客户端，一个servlet session一个客户端
	 */
	private static final Map<String, Session> connections = new HashMap<>();

	/**
	 * 打开连接
	 * 
	 * @param session
	 * @param sessionId
	 * @throws Exception 
	 */
	@OnOpen
	public void onOpen(Session session, @PathParam(value = "sessionId") String sessionId) throws Exception {

		Session c = connections.get(sessionId);
		if (c != null) {
			try {
				c.close();
			} catch (IOException e1) {
			}
		}

		connections.put(sessionId, session);
		
		broadcast(groupMsg(sessionId, c == null ? "连接到服务器。" : "重新连接到服务器。"));
	}

	/**
	 * 关闭连接
	 * @throws Exception 
	 */
	@OnClose
	public void onClose(Session session, @PathParam(value = "sessionId") String sessionId) throws Exception {
		connections.remove(sessionId);
		broadcast(groupMsg(sessionId, "断开服务器连接。"));
	}

	/**
	 * 接收客户端信息
	 * 
	 * @param message
	 * @throws Exception 
	 */
	@OnMessage
	public void onMessage(Session session, String message, @PathParam(value = "sessionId") String sessionId) throws Exception {
		try {

			Map<String, Object> msg = Tools.jsonStrtoMap(message);
			int type 	= Integer.valueOf(msg.get("type").toString());
			
			if(type == Const.WSP.MT.txt) {
				this.onTxtMsg(session, msg.get("msg").toString(), sessionId);
			}
			else if(type == Const.WSP.MT.arch) {
				
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void onTxtMsg(Session session, String msg, String sessionId) throws Exception {
		broadcast(groupMsg(sessionId, msg));
	}
	
	/**
	 * 错误信息响应
	 * 
	 * @param throwable
	 */
	@OnError
	public void onError(Throwable throwable) {
	}

	
	
	/**
	 * 根据servlet session关闭对应客户端
	 * @param sessionId
	 */
	public static void closeClient(String sessionId) {
		try{
			connections.get(sessionId).close();
		}
		catch(Exception ex){}
		connections.remove(sessionId);
	}
	
	/**
	 * 向所有客户端广播资料变更
	 * @param archId 资料ID
	 */
	public static void notifyArchiveAlter(int archId) {
		notifySpecifyClient(Const.WSP.ST.wsvr, null, Const.WSP.MT.arch, archId + "");
	}
	
	/**
	 * 向所有客户端广播终端上下线
	 * @param terminal 终端信息
	 * @param record 上下线记录信息
	 */
	public static void notifyTermOnline(Map<String, Object> terminal, Map<String, Object> record) {
		Map<String, Object> chg = new HashMap<>();
		chg.put("terminal", terminal);
		chg.put("record", record);
		
		List<Map<String, Object>> list = new ArrayList<>();
		list.add(chg);
		
		try {
			notifySpecifyClient(Const.WSP.ST.wsvr, null, Const.WSP.MT.termOnline, Tools.listToJsonStr(list));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 向所有客户端广播设备报警
	 * @param device 设备信息
	 * @param alarm 报警信息
	 */
	public static void notifyDeviceAlarm(Map<String, Object> device, Map<String, Object> alarm) {
		Map<String, Object> chg = new HashMap<>();
		chg.put("device", device);
		chg.put("alarm", alarm);
		
		List<Map<String, Object>> list = new ArrayList<>();
		list.add(chg);
		
		try {
			notifySpecifyClient(Const.WSP.ST.wsvr, null, Const.WSP.MT.deviceAlarm, Tools.listToJsonStr(list));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 通知指定客户端
	 * @param sourceType 消息来源类型
	 * @param sourceAdr 消息来源
	 * @param msgType 消息类型
	 * @param msg 消息
	 * @param sessionIds 客户端sessionId集合
	 */
	public static void notifySpecifyClient(int sourceType, String sourceAdr, int msgType, String msg, String... sessionIds) {
		Map<String, Object> msgWrap = new HashMap<>(), src = new HashMap<>();
		src.put("type", sourceType);
		src.put("addr", sourceAdr);
		msgWrap.put("source", src);
		msgWrap.put("msg", msg);
		msgWrap.put("type", msgType);
		List<String> tags = new ArrayList<>();
		for(String id: sessionIds) {
			tags.add(id);
		}
		msgWrap.put("target", tags);
		broadcast(msgWrap);
	}
	
	/**
	 * 通过一个来自客户端的消息组装一个广播消息体
	 * @param sessionId 客户端sessionId
	 * @param message 客户端消息
	 * @return 广播消息体
	 * @throws Exception
	 */
	private static String groupMsg(String sessionId, String message) throws Exception {
		Map<String, Object> msgWrap = new HashMap<>(), src = new HashMap<>();
		src.put("type", Const.WSP.ST.client);
		src.put("addr", sessionId);
		msgWrap.put("type", Const.WSP.MT.txt);
		msgWrap.put("source", src);
		msgWrap.put("msg", message);
		
		return groupMsg(msgWrap);
	}

	public HttpSocketServer() {
	}
	
	private static String groupMsg(Map<String, Object> msgWrap) throws Exception {
		return Tools.mapToJsonStr(msgWrap);
	}

	/**
	 * 广播消息体
	 * @param msgWrap
	 */
	private static void broadcast(Map<String, Object> msgWrap) {
		try {
			broadcast(groupMsg(msgWrap));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 发送或广播信息
	 * 
	 * @param message
	 */
	private static void broadcast(String message) {
		for (Session c : connections.values()) {
			try {
				synchronized (c) {
					c.getBasicRemote().sendText(message);
				}
			} catch (IOException e) {
				connections.remove(c);
				try {
					c.close();
				} catch (IOException e1) {
				}
			}
		}
	}

}
