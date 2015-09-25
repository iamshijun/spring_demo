package org.springframework.feature_test;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cjava.walker.entity.Bar;
import cjava.walker.entity.Foo;

@ContextConfiguration(classes = {
		SpringAnnotatedConfigTest.TestContext0.class,
		SpringAnnotatedConfigTest.TestContext1.class
	})
@RunWith(SpringJUnit4ClassRunner.class)
public class SpringAnnotatedConfigTest {

	@Inject
	private Foo foo;

	@Before
	public void setUp() {
		Assert.assertNotNull(foo);
	}
	
	@Test
	public void testDummy(){
	}
	
	
	@Configuration
	static class TestContext0{
		@Bean /*@Lazy*/
		public Bar bar(){
			return new Bar();
		}
	}
	
	@Configuration
	static class TestContext1 extends BaseContext{
		private Bar bar;
		@Autowired
		public void setBar(Bar bar){
			this.bar = bar;
		}
		
		public Bar bar() {
			return bar;
		}
	}
	
	static abstract class BaseContext{
		@Bean
		public Foo foo(){
			return new Foo(bar());
		}
		
		abstract Bar bar();
	}
}
