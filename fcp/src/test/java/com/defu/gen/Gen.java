package com.defu.gen;

import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map;
import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;

public class Gen {
	/**
	 * 生成基本的mybatis xml代码段
	 * @param table 待生成代码的表
	 * @param fn 各个方法映射的ID
	 * @param cols 列映射&lt;属性, 库字段&gt;
	 * @param cfg 其他配置，当前可用配置<pre>
	 * {
	 * 	increase: 自增列对应的属性
	 * 	interface: dao的接口全路径
	 * 	key: 主键列对应的属性
	 * }
	 * </pre>
	 * @param vm 引用的模板，根据数据库不同用不同的模板
	 * @throws Exception
	 */
	public static void auto(String table, Map<String, String> fn, Map<String, String> cols, Map<String, String> cfg, String vm) throws Exception {
		VelocityContext ctx;

		ctx = new VelocityContext();
		ctx.put("fn", fn);
		ctx.put("table", table);
		ctx.put("cols", cols);
		ctx.put("cfg", cfg);

		Writer writer;
		VelocityEngine ve = getVelocityEngine();
		Template template = ve.getTemplate(vm);
		template.merge(ctx, writer = new OutputStreamWriter(System.out));
		
		writer.flush();
		writer.close();
	}
	
	private static VelocityEngine getVelocityEngine() throws Exception {
		VelocityEngine ve = new VelocityEngine();
		Properties properties = new Properties();
		String fileDir = Thread.currentThread().getContextClassLoader()
				.getResource("com/defu/gen").getPath();
		// ve.setProperty(Velocity.RESOURCE_LOADER, "class");
		// ve.setProperty("class.resource.loader.class",
		// "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		properties.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, fileDir);
		properties.setProperty(Velocity.ENCODING_DEFAULT, "utf-8");
		properties.setProperty(Velocity.INPUT_ENCODING, "utf-8");
		properties.setProperty(Velocity.OUTPUT_ENCODING, "utf-8");
		ve.init(properties);
		return ve;

	}
	

}
