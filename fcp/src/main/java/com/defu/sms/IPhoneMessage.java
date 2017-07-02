package com.defu.sms;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

public interface IPhoneMessage {
	/**
	 * 发送文本短信
	 * @param msg 短信内容
	 * @param phoneNo 电话号码
	 * @param args 扩展参数，备用
	 * @return 返回一个map，扩展
	 */
	Map<String, Object> sendTextMsg(String msg, String phoneNo, Object... args) throws IOException;
	/**
	 * 发送文本短信
	 * @param msg 短信内容
	 * @param phoneNo 电话号码
	 * @param args 扩展参数，备用
	 * @return 返回一个map，扩展
	 */
	Map<String, Object> sendTextMsg(String msg, List<String> phoneNo, Object... args) throws IOException, URISyntaxException ;
	/**
	 * 发送语音短信
	 * @param msg 短信内容
	 * @param phoneNo 电话号码
	 * @param args 扩展参数，备用
	 * @return 返回一个map，扩展
	 */
	Map<String, Object> sendPhonicMsg(String msg, List<String> phoneNo, Object... args) throws IOException, URISyntaxException ;

	/**
	 * 发送语音短信
	 * @param msg 短信内容
	 * @param phoneNo 电话号码
	 * @param args 扩展参数，备用
	 * @return 返回一个map，扩展
	 */
	Map<String, Object> sendPhonicMsg(String msg, String phoneNo, Object... args) throws IOException, URISyntaxException ;
}
