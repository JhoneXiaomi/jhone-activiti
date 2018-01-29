package com.hebabr.base.util;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractRefreshableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringFactory {
    private static SpringFactory instance;
    private static BeanFactory factory;

    private static ApplicationContext context=null;

    private SpringFactory() {
        context = new ClassPathXmlApplicationContext(new String[]
                {"applicationContext.xml","classpath*:applicationContext-*.xml"});
        factory = (BeanFactory) context;

    }
    
    public static void refresh(){
    	if (context==null) getInstance();
    	((AbstractRefreshableApplicationContext )context).refresh();
    }

    public ApplicationContext getContext(){
        return context;
    }

    public static synchronized BeanFactory getInstance() {
        if (instance == null) {
            instance = new SpringFactory();
        }
        return factory;
    }
}
