package org.springframework.feature_test;


import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cjava.walker.common.Configuration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:springTest/*.xml")
public class SpringTest {

	@Resource
	private Configuration configuration;
	
	@Test
	public void testDummy(){
	}
	
	@Test
	public void test(){
		System.out.println(configuration.getRandonNum().intValue());
	}
	
}
