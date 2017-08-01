package com.defu.fcp;


/**
 * 全局静态常量
 * @author likeajin
 *
 */
public final class Const {
	/**
	 * 工程根目录路径
	 */
	public static final String webRoot = null;
	
	/**
	 * 系统用户初始密码
	 */
	public static final String dftPwd = null;
	
	public static final String yunpiankey = null;
	
	public static final class SmsCatCfg {
		public static final String comId = null;
		public static final String com = null;
		public static final Integer baudRate = null;
		public static final String manufacturer = null;
		public static final String model = null;
		public static final String simPin = null;
	}
	
	/**
	 * 相对路径，统一管理文件路径
	 * @author likeajin
	 *
	 */
	public static final class RelativePath {
		public static final String usrTmpDir = null;
	}


	public static final class SessionKey {
		public static final String curUser = null;
		public static final String vcode = null;
	}


	/**
	 * 相，用于对某些编码的描述
	 * @author likeajin
	 *
	 */
	public static final class Paraphase {}

	/**
	 * 编码，Paraphase的逆向类
	 * @author likeajin
	 *
	 */
	public static final class Code {}

	/**
	 * web socket protocol webSocket消息业务协议
	 * @author likeajin
	 *
	 */
	public static final class WSP {
		/**
		 * source type 消息源类型
		 * @author likeajin
		 *
		 */
		public static final class ST {
			/**
			 * web server 
			 */
			public static final Integer wsvr = 1;
			/**
			 * client 浏览器客户端
			 */
			public static final Integer client = 2;
		}
		/**
		 * 消息类型
		 * @author likeajin
		 *
		 */
		public static final class MT {
			/**
			 * 普通文本消息
			 */
			public static final Integer txt = 1;
			/**
			 * 资料变更
			 */
			public static final Integer arch = 2;
			/**
			 * 终端上下线通知
			 */
			public static final Integer termOnline = 3;
			/**
			 * 设备告警
			 */
			public static final Integer deviceAlarm = 4;
			
		}
	}

	/**
	 * 资料标识
	 * @author likeajin
	 *
	 */
	public static final class ArchID {
		/**
		 * 未知
		 */
		public static final Integer undefined = -1;
		/**
		 * 终端
		 */
		public static final Integer terminal = 1;
		/**
		 * 设备
		 */
		public static final Integer device = 2;
	}
	
	public static final class ArchAlterType {
		/**
		 * 新增
		 */
		public static final Integer add = 1;
		/**
		 * 删除
		 */
		public static final Integer del = 2;
		/**
		 * 修改
		 */
		public static final Integer mod = 3;
	}

}
