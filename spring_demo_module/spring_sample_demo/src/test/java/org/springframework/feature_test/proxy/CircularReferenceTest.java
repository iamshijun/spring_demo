package org.springframework.feature_test.proxy;

import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseFactoryBean;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import cjava.walker.common.service.AService;
import cjava.walker.common.service.BService;
import cjava.walker.common.service.impl.AServiceImpl;
import cjava.walker.common.service.impl.BServiceImpl;
import cjava.walker.support.InjectBeanSelfProcessor;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = CircularReferenceTest.Context.class)
public class CircularReferenceTest {
	
	@Autowired
	private AService aService;
	
	@Autowired
	private BService bService;
	
	@Test
	public void testDummy(){
		
	}
	
	

	@Configuration
	@EnableAspectJAutoProxy
	@EnableTransactionManagement
	public static class Context {
		@Bean
		public AService aService(){
			return new AServiceImpl();
		}
		@Bean
		public BService bService(){
			return new BServiceImpl();
		}
		
		@Bean
		public InjectBeanSelfProcessor injectBeanSelfProcessor(){
			return new InjectBeanSelfProcessor();
		}
		
		@Bean
		public PlatformTransactionManager transactionManager(DataSource dataSource){
			return new DataSourceTransactionManager(dataSource);
		}
		
		@Bean
		public EmbeddedDatabaseFactoryBean dataSource(){
			EmbeddedDatabaseFactoryBean embeddedDatabaseFactoryBean = new EmbeddedDatabaseFactoryBean();
			embeddedDatabaseFactoryBean.setDatabaseType(EmbeddedDatabaseType.H2);
			
			return embeddedDatabaseFactoryBean;
		}
	}
}
