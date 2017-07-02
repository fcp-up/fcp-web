package com.p3rd;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class YunpianMessage {
	protected final Log log = LogFactory.getLog(getClass());
	
	// 修改为您的apikey.apikey可在官网（http://www.yuanpian.com)登录后获取
	private String apikey;
	
	// 查账户信息的http地址
	private static String URI_GET_USER_INFO = "https://sms.yunpian.com/v2/user/get.json";

	// 智能匹配模版发送接口的http地址
//	private static String URI_SEND_SMS = "https://sms.yunpian.com/v2/sms/single_send.json";
	
	// 指定模版群体发送接口的http地址
	private static String URI_TPL_BATCH_SEND_SMS = "URL：https://sms.yunpian.com/v2/sms/tpl_batch_send.json";

//	 指定模板发送接口的http地址
	private static String URI_TPL_SEND_SMS = "https://sms.yunpian.com/v2/sms/tpl_single_send.json";

	// 发送语音验证码接口的http地址
//	private static String URI_SEND_CODE_VOICE = "https://voice.yunpian.com/v2/voice/send.json";

//	 指定模板单个发送语音验证码接口的http地址
	private static String URI_SEND_VOICE = "https://voice.yunpian.com/v2/voice/tpl_notify.json";

	// 编码格式。发送编码格式统一用UTF-8
	private static String ENCODING = "UTF-8";

	/**
	 * 发送文本短信
	 * @param msg 短信内容
	 * @param phoneNo 接受电话号码
	 * @param tplId 使用的模板ID
	 * @return 云片的响应
	 * @throws IOException
	 */
	public String sendTextMsg(String msg, String phoneNo, String tplId) throws IOException {
		if (log.isDebugEnabled()) log.debug("发送文本短信：" + msg);
		/**************** 使用指定模版接口发短信(推荐) *****************/
		msg = sendSms(apikey, tplId, msg, phoneNo, URI_TPL_SEND_SMS);
		
		if (log.isDebugEnabled()) log.debug("云片响应：" + msg);
		
		return msg;

	}

	/**
	 * 发送文本短信
	 * @param msg 短信内容
	 * @param phoneNo 接受电话号码
	 * @param tplId 使用的模板ID
	 * @return 云片的响应
	 * @throws IOException
	 */
	public String sendTextMsg(String msg, List<String> phoneNo, String tplId) throws IOException {
		if (log.isDebugEnabled()) log.debug("发送文本短信：" + msg);
		StringBuilder sb = new StringBuilder();
		boolean append = false;
		
		for(String no: phoneNo) {
			if(append) sb.append(",");
			append = true;
			sb.append(no);
		}
		
		/**************** 使用指定模版接口发短信(推荐) *****************/
		msg = sendSms(apikey, tplId, msg, sb.toString(), URI_TPL_BATCH_SEND_SMS);

		if (log.isDebugEnabled()) log.debug("云片响应：" + msg);
		return msg;

	}

	/**
	 * 发送语音短信
	 * @param msg 短信内容
	 * @param phoneNo 接受的电话号码
	 * @param tplid 使用的模板ID
	 * @return 云片的响应
	 * @throws IOException
	 */
	public String sendPhonicMsg(String msg, String phoneNo, String tplid) throws IOException {
		if (log.isDebugEnabled()) log.debug("发送语音短信：" + msg);

		/**************** 使用接口发语音验证码 *****************/
		msg = sendVoice(apikey, tplid, msg, phoneNo, URI_SEND_VOICE);

		if (log.isDebugEnabled()) log.debug("云片响应：" + msg);
		
		return msg;
	}
	

	/**
	 * 取账户信息
	 *
	 * @return json格式字符串
	 * @throws java.io.IOException
	 */
	public static String getUserInfo(String apikey) throws IOException,
			URISyntaxException {
		Map<String, String> params = new HashMap<String, String>();
		params.put("apikey", apikey);
		return post(URI_GET_USER_INFO, params);
	}

	/**
	 * 通过模板批量发送短信(不推荐)
	 *
	 * @param apikey apikey
	 * @param tpl_id 模板id
	 * @param tpl_value 模板变量值
	 * @param mobile 接受的手机号
	 * @param url 使用的发送接口
	 * @return json格式字符串
	 * @throws IOException
	 */
	public static String sendSms(String apikey, String tpl_id, String tpl_value, String mobile, String url) throws IOException {
		Map<String, String> params = new HashMap<String, String>();
		params.put("apikey", apikey);
		params.put("tpl_id", tpl_id);
		params.put("tpl_value", tpl_value);
		params.put("mobile", mobile);
		return post(url, params);
	}

	/**
	 * 通过接口发送语音通知
	 *
	 * @param apikey apikey
	 * @param tplId 模板ID
	 * @param text 短信内容
	 * @param mobile 接受的手机号
	 * @param url 使用的发送接口
	 * @return json格式字符串
	 */
	public static String sendVoice(String apikey, String tplId, String text, String mobile, String url) throws IOException {
		Map<String, String> params = new HashMap<String, String>();
		params.put("apikey", apikey);
		params.put("mobile", mobile);
		params.put("tpl_value", text);
		params.put("tpl_id", tplId);
		return post(url, params);
	}

	/**
	 * 基于HttpClient 4.3的通用POST方法
	 *
	 * @param url 提交的URL
	 * @param paramsMap 提交<参数，值>Map
	 * @return 提交响应
	 * @throws IOException 
	 */
	public static String post(String url, Map<String, String> paramsMap) throws IOException {
		CloseableHttpClient client = HttpClients.createDefault();
		String responseText = null;
		CloseableHttpResponse response = null;

		try {
			HttpPost method = new HttpPost(url);
			if (paramsMap != null) {
				List<NameValuePair> paramList = new ArrayList<NameValuePair>();
				for (Map.Entry<String, String> param : paramsMap.entrySet()) {
					NameValuePair pair = new BasicNameValuePair(param.getKey(),
							param.getValue());
					paramList.add(pair);
				}
				method.setEntity(new UrlEncodedFormEntity(paramList, ENCODING));
			}
			response = client.execute(method);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				responseText = EntityUtils.toString(entity);
			}
		} 
		catch (IOException e) {
			throw e;
		} finally {
			response.close();
		}
		return responseText;
	}

	
	
	public String getApikey() {
		return apikey;
	}
	

	public void setApikey(String apikey) {
		this.apikey = apikey;
	}



}
