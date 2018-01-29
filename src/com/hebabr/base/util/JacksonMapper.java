package com.hebabr.base.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JacksonMapper {

	private static final ObjectMapper mapper = new ObjectMapper();
	private JacksonMapper() {}
	public static ObjectMapper getInstance() {
		return mapper;
	}

	public static String compressStringList(List<String> objects) {
		String value = "";
		ObjectMapper mapper = JacksonMapper.getInstance();
		try {
			value = mapper.writeValueAsString(objects);
		} catch (JsonGenerationException e) {
			// Do nothing
		} catch (IOException e) {
			// Do nothing
		}
		return value;
	}

	public static List<String> extractList(String jsonString) {
		List<String> list= new ArrayList<String>();

		ObjectMapper mapper = JacksonMapper.getInstance();
		try {
			list = mapper.readValue(jsonString, ArrayList.class);
		} catch (JsonParseException e) {
			// Do nothing
		} catch (IOException e) {
			// Do nothing
		}

		return list;
	}
}
