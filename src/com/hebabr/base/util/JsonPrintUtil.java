package com.hebabr.base.util;


import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.sf.json.JSONObject;



public class JsonPrintUtil {
	
	private static Logger logger = Logger.getLogger(JsonPrintUtil.class);
	

	
	//JacksonMapper输出
	public static void printJsonObj(HttpServletResponse response, Object obj) {
		response.setContentType("text/json;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		
		ObjectMapper mapper = JacksonMapper.getInstance();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		mapper.setDateFormat(df);
		try {
			mapper.writeValue(response.getWriter(), obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void printObjData(HttpServletResponse response, Object obj) {
		response.setContentType("text/json;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			JSONObject json = new JSONObject();
			json.put("data", obj);
			out.print(json.toString());
			logger.debug(json.toString());
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
