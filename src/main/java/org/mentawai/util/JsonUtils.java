package org.mentawai.util;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {
	
	@SuppressWarnings("unchecked")
    public static Map<String, Object> toMap(String json) {
		try {
			return (Map<String, Object>) new ObjectMapper().readValue(json, HashMap.class);
		} catch(Throwable e) {
			throw new RuntimeException(e);
		}
	}
	
	@SuppressWarnings("unchecked")
    public static void main(String[] args) {
		
		String json = "{ \"data\" : { \"field1\" : \"value1\", \"field2\" : \"value2\"}}";
		
		Map<String, Object> map = toMap(json);
		
		Map<String, Object> m = (Map<String, Object>) map.get("data");
		
		System.out.println(m.get("field1"));
		System.out.println(m.get("field2"));
	}
}