package feng.shi.test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.aop.framework.autoproxy.BeanNameAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseFactory;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseFactoryBean;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.interceptor.MatchAlwaysTransactionAttributeSource;
import org.springframework.transaction.interceptor.TransactionAttributeSource;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import feng.shi.service.BService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class SpringFeatureTest {
	
	@Inject
	private BService bService;

	@Test
	public void dummy(){
		
	}
	
	@Test
	public void testAsync() throws InterruptedException, ExecutionException{
		Future<String> doAsync = bService.doAsync();
		
		String response = doAsync.get();
		
		System.out.println(response);
	}
	
	@Configuration
	@ComponentScan(basePackages = {"feng.shi.service.impl"})
	@EnableAsync
	static class TestContext{
		@Autowired
		private Environment environment;
		
/*		@Bean //@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
		public DefaultAdvisorAutoProxyCreator autoProxyCreator(){
			return new DefaultAdvisorAutoProxyCreator();
		}
		@Bean
		public TransactionAttributeSourceAdvisor transactionAttributeSourceAdvisor(){
			return new TransactionAttributeSourceAdvisor(transactionInterceptor());
		}
*/		
		
		@Bean public BeanNameAutoProxyCreator beanNameAutoProxyCreator(){
			BeanNameAutoProxyCreator beanNameAutoProxyCreator = new BeanNameAutoProxyCreator();
			beanNameAutoProxyCreator.setBeanNames(new String[]{"*Service"});
			beanNameAutoProxyCreator.setInterceptorNames(new String[]{"transactionInterceptor"});
			return beanNameAutoProxyCreator;
		}
		
		
		@Bean public TransactionInterceptor transactionInterceptor(){
			return new TransactionInterceptor(platformTransactionManager(), transactionAttributeSource());
		}
		@Bean public TransactionAttributeSource transactionAttributeSource(){
			return new MatchAlwaysTransactionAttributeSource();
		}
		
		@Bean public DataSource dataSource(){
			EmbeddedDatabaseFactory embeddedDatabaseFactory = new EmbeddedDatabaseFactoryBean();
			embeddedDatabaseFactory.setDatabaseName("test");
			embeddedDatabaseFactory.setDatabaseType(EmbeddedDatabaseType.H2);
			
			return embeddedDatabaseFactory.getDatabase();
			
			/*EmbeddedDatabase db = new EmbeddedDatabaseBuilder()
			    .generateUniqueName(true)
			    .setType(H2)
			    .setScriptEncoding("UTF-8")
			    .ignoreFailedDrops(true)
			    .addScript("schema.sql")
			    .addScripts("user_data.sql", "country_data.sql")
			 .build();
	
			// perform actions against the db (EmbeddedDatabase extends javax.sql.DataSource)
	
			db.shutdown()*/
		}
		
		@Bean public PlatformTransactionManager platformTransactionManager(){
			return new DataSourceTransactionManager(dataSource());
		}
	}
}
