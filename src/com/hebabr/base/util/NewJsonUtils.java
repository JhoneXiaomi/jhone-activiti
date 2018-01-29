package com.hebabr.base.util;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * 本工具类是对照JsonUtils的新json工具类，因为缓存的加入，经过测试，最后选择jackson2.6作为json新处理类。
 * jackson提供了直接转换list，map等一系列方法，并极大提高了json转换效率，大家可对比感受一下。
 * @author dyh
 *
 */
public class NewJsonUtils {
	
	private static JsonGenerator jsonGenerator = null;
    private static ObjectMapper objectMapper = null;
    
    /**
     * 将对象(bean，list, map等)转换成json字符串
     * @param obj
     * @return
     * @throws Exception
     */
    public static String bean2JsonStr(Object obj){
    	objectMapper = new ObjectMapper();
    	StringWriter writer =new StringWriter();
    	try {
			jsonGenerator = objectMapper.getFactory().createGenerator(writer);
			jsonGenerator.writeObject(obj);
			return writer.toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}  	
    }
    
    /**
     * 讲json字符串转换成单个的java对象，需要传入java类
     * @param jsonStr
     * @param clazz
     * @return
     */
    public static Object jsonStr2Bean(String jsonStr, Class<?> clazz){
    	objectMapper = new ObjectMapper();
    	objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    	objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
    	try {
			return objectMapper.readValue(jsonStr, clazz);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
    }
    
    /**
     * 将json字符串转换成java集合(list.map等等)
     * @param jsonStr
     * @param beanClazz
     * @param typeClazz
     * @return
     */
	public static Collection<?> jsonStr2List(String jsonStr, Class<?> beanClazz){
    	objectMapper = new ObjectMapper();
    	try {
    		return objectMapper.readValue(jsonStr, objectMapper.getTypeFactory().constructCollectionType(List.class, beanClazz));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
    }
	
	/**
	 * 把json字符串转化成为map对象
	 * @param jsonStr
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Map json2Map(String jsonStr){
		objectMapper = new ObjectMapper();
		try {
			return objectMapper.readValue(jsonStr, HashMap.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} 
		
		   
	}
	
	public static String List2JsonStr(List<?> list){
		objectMapper = new ObjectMapper();

		try {
			return objectMapper.writeValueAsString(list);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
    
//    @SuppressWarnings("unchecked")
//	public static void main(String[] args) throws Exception{
//    	List<TbDict> list = new ArrayList<TbDict>();
//    	TbDict dict = new TbDict();
//    	dict.setName("测试");
//    	dict.setCode("test");
//    	list.add(dict);
//    	String jsonStr = bean2JsonStr(list);
//    	List<TbDict> list1 = (List<TbDict>) jsonStr2List(jsonStr, TbDict.class, List.class);
//    	for(TbDict dict1 : list1){
//    		System.out.println(dict1.getCode());
//    	}
//    	
//    }

}
