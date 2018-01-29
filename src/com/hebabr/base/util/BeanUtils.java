package com.hebabr.base.util;

import java.lang.reflect.Field;
import java.util.Map;

public class BeanUtils {
	
	/**
	 * 根据类型向bean里设置属性值（待完善）
	 * @param bean
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Object setValueToBean(Object bean){
		//得到类对象  
	       Class userCla = (Class) bean.getClass();  
	        
	       /* 
	        * 得到类中的所有属性集合 
	        */  
	       Field[] fs = userCla.getDeclaredFields();
	       try{
	       for(int i = 0 ; i < fs.length; i++){  
	           Field f = fs[i];  
	           f.setAccessible(true); //设置些属性是可以访问的  
	           Object val = f.get(bean);//得到此属性的值     
	        
	           System.out.println("name:"+f.getName()+"\t value = "+val);  
	            
	           String type = f.getType().toString();//得到此属性的类型  
	           if (type.endsWith("String")) {  
	              System.out.println(f.getType()+"\t是String");  
	              f.set(bean,"12") ;        //给属性设值  
	           }else if(type.endsWith("int") || type.endsWith("Integer")){  
	              System.out.println(f.getType()+"\t是int");  
	              f.set(bean,12) ;       //给属性设值  
	           }else{  
	              System.out.println(f.getType()+"\t");  
	           }              
	       }
	       return bean;
	       }catch(Exception e){
	    	   return null;
	       }
	}
	
	@SuppressWarnings("rawtypes")
	public static Object setValueByNameToBean(Object bean, String type, String value){
		 Class clazz = (Class) bean.getClass();
		 Field[] fs = clazz.getDeclaredFields();
		 for(int i=0; i<fs.length; i++){
			 Field f = fs[i];
			 if(f.getName().equals(type)){
				try {
					f.setAccessible(true);
					f.set(bean, value);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return null;
				}
			 }
		 }
		 return bean;
	}
	
	public static Object getDataBeanByClassAndMap(Class clazz, Map dataMap) throws Exception{
		 Field[] fs = clazz.getDeclaredFields();
		 for(int i = 0 ; i < fs.length; i++){  
	           Field f = fs[i];
	           f.setAccessible(true);
	           String attrName = f.getName();
	           String attrValue = (String) dataMap.get(attrName);
	           String type = f.getType().toString();
	           if (type.endsWith("String")) { 
	        	   f.set(attrName, attrValue);
	           }else if(type.endsWith("int") || type.endsWith("Integer")){
	        	   f.setInt(attrName, Integer.valueOf(attrValue));
	           }
		 }
		return fs;
	}
}
