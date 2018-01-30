package com.hebabr.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class) // 整合 
@ContextConfiguration(locations="classpath:conf/spring.xml") 
public class jhoneTest {
	
	@Test
	public void test() {
		
	}
}
