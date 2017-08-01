package com.defu.util;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Tools {
	public static ObjectMapper mapper = new ObjectMapper();
	
	public static String mapToJsonStr(Map<String, Object> map) throws JsonProcessingException {
		if(map == null) return null;
		return mapper.writeValueAsString(map);
	}

	public static String listToJsonStr(List<?> list) throws JsonProcessingException {
		if(list == null) return null;
		return mapper.writeValueAsString(list);
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Object> jsonStrtoMap(String jsonStr) throws JsonParseException, JsonMappingException, IOException {
		if(jsonStr == null) return null;
		return mapper.readValue(jsonStr, Map.class);
	}

	public static List<?> jsonStrtoList(String jsonStr) throws JsonParseException, JsonMappingException, IOException {
		if(jsonStr == null) return null;
		return mapper.readValue(jsonStr, List.class);
	}
	
	@SuppressWarnings("unchecked")
	public static void setFinalField(Map<String, String> props) {
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

			for (Map.Entry<String, String> x: props.entrySet()) {
				key = x.getKey();
				value = x.getValue();
				dot = key.lastIndexOf(".");
				if (dot > -1) {
					clazz = props.get(key.substring(0, dot));
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
										if (at[1] == Boolean.class) {
											v = Boolean.valueOf(v.toString());
										}
										atr.put(k, v);
									}
								}
								else if (ft == Integer.class) {
									f.set(null, Integer.parseInt(value));
								}
								else if (ft == Boolean.class) {
									f.set(null, Boolean.valueOf(value));
								}
								else {
									f.set(null, value);
								}
							}
							else {
								index = Integer.valueOf(key.substring(key.indexOf("[") + 1, key.lastIndexOf("]")));
								v = f.get(null);
								
								pz = (ParameterizedType) f.getGenericType();
								at = pz.getActualTypeArguments();

								if (v == null) {
									List<?> obj;
									if (at[0] == Integer.class) {
										obj = new ArrayList<Integer>();
										for(int i = 0; i <= index; i++) obj.add(null);
										((List<Integer>) obj).set(index, Integer.parseInt(value));
									}
									if (at[0] == Boolean.class) {
										obj = new ArrayList<Boolean>();
										for(int i = obj.size(); i <= index; i++) obj.add(null);
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
									List<Integer> tmp = ((List<Integer>) v);
									for(int i = tmp.size(); i <= index; i++) tmp.add(null);
									tmp.set(index, Integer.parseInt(value));
								}
								if (at[0] == Boolean.class) {
									List<Boolean> tmp = ((List<Boolean>) v);
									for(int i = tmp.size(); i <= index; i++) tmp.add(null);
									tmp.set(index, Boolean.valueOf(value));
								}
								else {
									List<String> tmp = ((List<String>) v);
									for(int i = tmp.size(); i <= index; i++) tmp.add(null);
									tmp.set(index, value);
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
