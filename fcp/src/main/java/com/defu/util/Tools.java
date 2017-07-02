package com.defu.util;

import java.io.IOException;
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

	public static String ListToJsonStr(List<?> list) throws JsonProcessingException {
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
}
