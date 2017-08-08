package com.defu.fcp;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URLDecoder;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.defu.util.impl.FileApi;
import com.mchange.v2.c3p0.ComboPooledDataSource;

public class SystemListener extends ContextLoaderListener implements
		HttpSessionListener {
	protected static final Log log = LogFactory.getLog(SystemListener.class);

	private void initDbConfig(ComboPooledDataSource dbs,
			WebApplicationContext ctx, String dbType, Properties prop) {
		String user = prop.getProperty("db.username"), pwd = prop
				.getProperty("db.password"), ip = prop.getProperty("db.ip"), port = prop
				.getProperty("db.port"), name = prop.getProperty("db.name"), url = null;

		dbs.setUser(user);
		dbs.setPassword(pwd);

		if ("mysql".equals(dbType)) {
			try {
				dbs.setDriverClass("org.gjt.mm.mysql.Driver");
			} catch (Exception ex) {
			}
			url = "jdbc:mysql://"
					+ ip
					+ ":"
					+ port
					+ "/"
					+ name
					+ "?characterEncoding=utf8&characterSetResults=UTF-8&zeroDateTimeBehavior=convertToNull";

		} else if ("sqlserver".equals(dbType)) {
			try {
				dbs.setDriverClass("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			} catch (Exception ex) {
			}

			url = "jdbc:sqlserver://" + ip + ":" + port + ";DatabaseName="
					+ name;

		} else if ("firebird".equals(dbType)) {
			try {
				dbs.setDriverClass("org.firebirdsql.jdbc.FBDriver");
			} catch (Exception ex) {
			}

			//jdbc:firebirdsql:www.dataprovider.cn/6050:pim2.5_archives
			url = "jdbc:firebirdsql:" + ip + "/" + port + ":" + name;

		}

		dbs.setJdbcUrl(url);

		if (log.isDebugEnabled()) {
			log.debug("ip\t:" + ip);
			log.debug("port\t:" + port);
			log.debug("name\t:" + name);
			log.debug("url\t:" + url);
			log.debug("user\t:" + user);
			log.debug("pwd\t:" + pwd);
		}

	}

	@Override
	public void sessionCreated(HttpSessionEvent arg0) {}

	@Override
	public void sessionDestroyed(HttpSessionEvent arg0) {
		String sessionId = arg0.getSession().getId();
		
		new FileApi().deleteFile(Const.webRoot + Const.RelativePath.usrTmpDir + "/" + sessionId);
		HttpSocketServer.closeClient(sessionId);
	}

	@Override
	public void contextInitialized(final ServletContextEvent e) {
		ServletContext context = e.getServletContext();
		String dbType = null;

		Properties prop = new Properties();
		try {
			String cfgPath = URLDecoder.decode(this.getClass().getClassLoader()
					.getResource("../../").getFile().substring(1), "utf-8")
					+ context.getInitParameter("dbConfigLocation");

			File cfg = new File(cfgPath);
			Reader fr = new InputStreamReader(new FileInputStream(cfg));
			StringBuilder sb = new StringBuilder();
			int c;
			while ((c = fr.read()) != -1) {
				if(((char)c) != '\r'){
					sb.append((char)c);
				}
			}
			fr.close();
			String fileInfo = sb.toString();
			String[] infoItems = fileInfo.split("[\n]");
//			ICrypto rc4 = new RC4();
			for (int i = 0; i < infoItems.length; i++) {
				String infoItem = infoItems[i];
				if(infoItem == null || infoItem.isEmpty()){
					continue;
				}
//				infoItem = rc4.decrypt(infoItem);
				String[] infoItemKeyValue = infoItem.split("=");
				if(infoItemKeyValue == null || infoItemKeyValue.length != 2){
					continue;
				}
				prop.put(infoItemKeyValue[0], infoItemKeyValue[1]);
			}
			dbType = prop.getProperty("db.type");
			context.log("数据库类型：" + dbType);
		} catch (Exception ex) {
		}

		if (dbType == null) {
			context.log("工程启动出错，加载配置文件database.properties失败，将无法正常运行，请联系供应商。");
			return;
		}

		context.setInitParameter("contextConfigLocation",
				"classpath:config/spring-*.xml,classpath:config/adaptor/db-"
						+ dbType + ".xml");
		
		super.contextInitialized(e);
		WebApplicationContext ctx = WebApplicationContextUtils
				.getWebApplicationContext(e.getServletContext());
		
		ComboPooledDataSource dbs = ctx.getBean(ComboPooledDataSource.class);
		initDbConfig(dbs, ctx, dbType, prop);
		
		if (log.isDebugEnabled())
			log.debug("web服务启动");

	}
	

}
