package com.defu.fcp;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 全局静态常量配置类，通过java反射机制将配置文件中的静态值写入到Const类，供全局使用
 * @author likeajin
 *
 */
public class CustomizedPropertyPlaceholderConfigurer extends
		PropertyPlaceholderConfigurer {
	@SuppressWarnings("unchecked")
	protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props)
			throws BeansException {
		super.processProperties(beanFactoryToProcess, props);

		String key, clazz, field, value;
		int dot, mod, index;
		Class<?> c, ft;
		Field f, mf;
		Object k, v;
		boolean a, ma;
		Map<Object, Object> map, atr;
		ParameterizedType pz;
		Type[] at;
		try {
			mf = Field.class.getDeclaredField("modifiers");
			ma = mf.isAccessible();
			mf.setAccessible(true);

			c = Const.class;
			f = c.getDeclaredField("webRoot");
			a = f.isAccessible();
			f.setAccessible(true);
			mod = f.getModifiers();
			mf.setInt(f, mod & ~Modifier.FINAL);
			String path = this.getClass().getClassLoader().getResource("../../").getFile().substring(1);
			path = URLDecoder.decode(path, "utf-8");
			f.set(null, path);
			mf.setInt(f, mod);
			f.setAccessible(a);

			for (Enumeration<?> p = props.propertyNames(); p.hasMoreElements();) {
				key = (String) p.nextElement();
				value = props.getProperty(key);
				dot = key.lastIndexOf(".");
				if (dot > -1) {
					clazz = props.getProperty(key.substring(0, dot));
					field = key.substring(dot + 1);
					try {
						c = Class.forName(clazz);
						dot = field.indexOf("[");
						if (dot > -1) {
							field = field.substring(0, dot);
						}
						f = c.getDeclaredField(field);
						a = f.isAccessible();
						f.setAccessible(true);
						mod = f.getModifiers();

						mf.setInt(f, mod & ~Modifier.FINAL);

						try {
							ft = f.getType();
							if (dot < 0) {
								if (ft == Map.class) {
									atr = (Map<Object, Object>) f.get(null);

									pz = (ParameterizedType) f.getGenericType();
									at = pz.getActualTypeArguments();

									map = new ObjectMapper().readValue(value, Map.class);
									for (Map.Entry<Object, Object> e : map.entrySet()) {
										k = e.getKey();
										if (at[0] == Integer.class) {
											k = Integer.parseInt(k.toString());
										}

										v = e.getValue();
										if (at[1] == Integer.class) {
											v = Integer.parseInt(v.toString());
										}
										else if (at[1] == Long.class) {
											v = Long.parseLong(v.toString());
										}
										else if (at[1] == Boolean.class) {
											v = Boolean.valueOf(v.toString());
										}
										atr.put(k, v);
									}
								}
								else if (ft == Integer.class) {
									f.set(null, Integer.parseInt(value));
								}
								else if (ft == Long.class) {
									f.set(null, Long.parseLong(value));
								}
								else if (ft == Boolean.class) {
									f.set(null, Boolean.valueOf(value));
								}
								else
									f.set(null, props.get(key));
							}
							else {
								index = Integer.valueOf(key.substring(key.indexOf("[") + 1, key.lastIndexOf("]")));
								v = f.get(null);
								
								pz = (ParameterizedType) f.getGenericType();
								at = pz.getActualTypeArguments();
								
								if (v == null) {
									List<?> obj;
									if(at[0] == Integer.class) {
										obj = new ArrayList<Integer>();
										for(int i = 0; i <= index; i++) obj.add(null);
										((List<Integer>) obj).set(index, Integer.parseInt(value));
									}
									else if(at[0] == Long.class) {
										obj = new ArrayList<Long>();
										for(int i = 0; i <= index; i++) obj.add(null);
										((List<Long>) obj).set(index, Long.parseLong(value));										
									}
									else if(at[0] == Boolean.class) {
										obj = new ArrayList<Boolean>();
										for(int i = 0; i <= index; i++) obj.add(null);
										((List<Boolean>) obj).set(index, Boolean.valueOf(value));										
									}
									else {
										obj = new ArrayList<String>();
										for(int i = 0; i <= index; i++) obj.add(null);
										((List<String>) obj).set(index, value);										
									}
									f.set(null, obj);
								}
								if (at[0] == Integer.class) {
									List<Integer> tmp = (List<Integer>) v;
									for(int i = tmp.size(); i <= index; i++) tmp.add(null);
									tmp.set(index, Integer.parseInt(value));
								}
								else if (at[0] == Long.class) {
									List<Long> tmp = (List<Long>) v;
									for(int i = tmp.size(); i <= index; i++) tmp.add(null);
									tmp.set(index, Long.parseLong(value));
								}
								else if (at[0] == Boolean.class) {
									List<Boolean> tmp = (List<Boolean>) v;
									for(int i = tmp.size(); i <= index; i++) tmp.add(null);
									tmp.set(index, Boolean.valueOf(value));
								}
								else {
									((List<String>) v).add(value);
								}
							}
						} catch (Exception ex) {
							ex.printStackTrace();
							break;
						}

						mf.setInt(f, mod);
						f.setAccessible(a);
					} catch (Exception ex) {
						ex.printStackTrace();
						break;

					}
				}
			}
			mf.setAccessible(ma);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
