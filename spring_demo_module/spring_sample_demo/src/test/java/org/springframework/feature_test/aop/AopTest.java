package org.springframework.feature_test.aop;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class AopTest {

	@Autowired
	private HelloWorldService helloWorldService;
	
	@Test
	public void testAopWithAspect(){
		helloWorldService.sayHello("shijun");
	}
}
