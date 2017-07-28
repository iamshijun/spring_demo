package org.springframework.feature_test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cjava.walker.config.PatchMakerConfig;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:springTest2/application-config.xml")
public class SpringProcessSequenceFeatureTest {
	//try runwith -Dpatchmarker.properties.config.location=file:///etc/patchmaker/patch.properties 
	//注意回顾ResourceEditor的规则 如果上述不指定schema file://的话 会在classpath中找指定的配置文件
	
	@Autowired
	private PatchMakerConfig patchMakerConfig;
	
	@Test
	public void test01(){
		System.out.println(patchMakerConfig.getVersion());
	}
}
