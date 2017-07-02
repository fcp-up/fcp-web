package com.p3rd;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.smslib.AGateway;
import org.smslib.IOutboundMessageNotification;
import org.smslib.Message.MessageEncodings;
import org.smslib.OutboundMessage;
import org.smslib.Service;
import org.smslib.modem.SerialModemGateway;

public class SmsCat {
	protected final Log log = LogFactory.getLog(getClass());
	
	private String comId;
	private String comPort;
	private Integer baudRate;
	private String manufacturer;
	private String model;
	private String pin;
	
	/**
	 * 是否开启服务
	 */
	private boolean isStartService = false;
	
	private SerialModemGateway gateway;
	private Service service;
	
	/**
	 * 开启短信服务
	 */
	public void startService() {
		if (log.isDebugEnabled()) log.debug("开始初始化短信猫服务！");
		
		// 初始化网关，参数信息依次为：COMID,COM号,比特率,制造商,Modem模式
		gateway = new SerialModemGateway(comId, comPort, baudRate, manufacturer, model);

		gateway.setInbound(true);
		gateway.setOutbound(true);
		gateway.setSimPin(pin);

		service = Service.getInstance();
		if (service == null) {
			if (log.isWarnEnabled()) log.warn("短信猫服务初始化失败！");
			return;
		}

		service.setOutboundMessageNotification(new IOutboundMessageNotification(){
			public void process(AGateway gateway, OutboundMessage msg) {
				if (log.isWarnEnabled()) log.warn(String.format("短信猫接受到消息：to\t%s\t%s", msg.getRecipient(), msg.getText()));
			}
		});
		
		try {
			service.addGateway(gateway);
			if (log.isDebugEnabled()) log.debug("短信猫服务初始化完毕，开启短信猫服务！");
			service.startService();
			if (log.isDebugEnabled()) log.debug("开启短信猫服务成功！");
			isStartService = true;
		} catch (Exception e) {
			if (log.isWarnEnabled()) log.warn("开启短信猫服务异常！");
			e.printStackTrace();
		} 
	}

	/**
	 * 停止SMS服务
	 */
	public void stopService() {
		if (log.isDebugEnabled()) log.debug("关闭短信猫服务！");
		try{
			service.setOutboundMessageNotification(null);
			service.removeGateway(gateway);
		}
		catch(Exception ex){
		}
		
		try{
			service.stopService();
		}
		catch(Exception ex){
		}
		
		try {
			Service.getInstance().stopService();
		} catch (Exception e) {
			if (log.isWarnEnabled()) log.warn("关闭短信猫服务异常！");
			e.printStackTrace();
		} 
		isStartService = false;
		
		gateway = null;
		service = null;
	}

	/**
	 * 发送短信
	 * 
	 * @param message
	 *            短信内容
	 * @param toNumber
	 *            手机号码
	 */
	public boolean sendTextMsg(String message, String toNumber) {
		if (log.isDebugEnabled()) log.debug("接受到信息：" + message);
		if (!isStartService) {
			if (log.isDebugEnabled()) log.debug("尚未开启短信猫服务，放弃发送！");
			return false;
		}

		// 封装信息
		OutboundMessage msg = new OutboundMessage(toNumber, message);
		msg.setEncoding(MessageEncodings.ENCUCS2);
		try {
			// 发送信息
			if (log.isDebugEnabled()) log.debug("发送信息");
			return Service.getInstance().sendMessage(msg);
		} catch (Exception e) {
			if (log.isDebugEnabled()) log.debug("发送信息异常，重启短信猫服务。");
			stopService();
			startService();
			return false;
		} 
	}
	
	public SmsCat(String comId, String comPort, Integer baudRate,
			String manufacturer, String model, String pin) {
		super();
		this.comId = comId;
		this.comPort = comPort;
		this.baudRate = baudRate;
		this.manufacturer = manufacturer;
		this.model = model;
		this.pin = pin;
	}

	public String getComId() {
		return comId;
	}

	public void setComId(String comId) {
		this.comId = comId;
	}

	public String getComPort() {
		return comPort;
	}

	public void setComPort(String comPort) {
		this.comPort = comPort;
	}

	public Integer getBaudRate() {
		return baudRate;
	}

	public void setBaudRate(Integer baudRate) {
		this.baudRate = baudRate;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}
	
	
}
