package com.hebabr.base.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 获取spring ApplicationContext
 * 
 * @author 温强
 * 
 */
public class ApplicationContextUtil implements ApplicationContextAware {

	public static ApplicationContext context;

	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		context = applicationContext;
	}

	/**
	 * 获取context
	 * 
	 * @return
	 */
	public static ApplicationContext getApplicationContext() {
		return context;
	}

	
	public static void main(String[] args){
		 System.out.println(System.getProperty("java.endorsed.dirs"));

	}
}
