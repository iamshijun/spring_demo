package feng.shi.test;

import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
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
import org.springframework.transaction.interceptor.TransactionAttributeSourceAdvisor;
import org.springframework.transaction.interceptor.TransactionInterceptor;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class SpringFeatureTest {
	
//	@Inject
//	private AService aService;
//	@Inject
//	private BService bService;

	//注 : 这里扫描的 包feng.shi.service.impl 下的ABService和BService实现 使用了@Asyn 还有将他们都加入到了 事务拦截中
	//这里 就会涉及到的BeanPostProcessor　: AsyncAnnotationBeanPostProcessor , AbstractAutoProxyCreator(SmartInstantiationAwareBeanPostProcess)
	// 另外最主要的一点是 AService和BService之间存在着 循环引用. 跟上面说的SmartInitiationAwareBeanPostProcess
	// 可以通过将 AServiceImpl 中的@Asyn 注释去掉 看当前容器初始化是否成功以及错误的时候出现的错误 可以看到出现问题的地方 
	//(ps 另外因为扫描的顺序所以是从AServiceImpl开始的)
	
	//同样即使下面没有使用到 有特定 getEarlyBeanReference处理的SmartInitiationAwareBeanPostProcess (DefaultAdvisorAutoProxyCreator / BeanNameAutoProxyCreator)
	//在存在  circular reference的情况下 就会在 getSingleton(beanName)获取到一个被 迫切生成被引用的bean , 第二步就会对其  initialize完后的(俗称exposedObject/ exposure) 和 反射出来的bean实例进行比对
	//如果存在差异 并且有bean依赖这个被引用的bean就会抛出 异常 (ps 即不允许迫切生成的bean后 最后生成的最终暴露出去实例存在差异)
	/* Caused by: org.springframework.beans.factory.BeanCurrentlyInCreationException: Error creating bean with name 'aService': Bean with name 'aService' has been injected into other beans [bService] in its raw version as part of a circular reference, but has eventually been wrapped. This means that said other beans do not use the final version of the bean. This is often the result of over-eager type matching - consider using 'getBeanNamesOfType' with the 'allowEagerInit' flag turned off, for example.
			at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean(AbstractAutowireCapableBeanFactory.java:548)
			at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBean(AbstractAutowireCapableBeanFactory.java:458)
			at org.springframework.beans.factory.support.AbstractBeanFactory$1.getObject(AbstractBeanFactory.java:296)
			at org.springframework.beans.factory.support.DefaultSingletonBeanRegistry.getSingleton(DefaultSingletonBeanRegistry.java:223)
			at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:293)
	*/
	@Test
	public void dummy(){
		
	}
	
	/*@Test
	public void testAsync() throws InterruptedException, ExecutionException{
		Future<String> doAsync = bService.doWithOtherBeanWithAsync();
		String response = doAsync.get();
		System.out.println(response);
	}*/
	
	@Configuration
	@ComponentScan(basePackages = {"feng.shi.service.impl"})
	@EnableAsync
	static class TestContext{
		@Autowired
		private Environment environment;
		
		@Bean @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
		public DefaultAdvisorAutoProxyCreator autoProxyCreator(){
			return new DefaultAdvisorAutoProxyCreator();
		}
		@Bean @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
		public TransactionAttributeSourceAdvisor transactionAttributeSourceAdvisor(){
			return new TransactionAttributeSourceAdvisor(transactionInterceptor());
		}
		
		/*
		@Bean public BeanNameAutoProxyCreator beanNameAutoProxyCreator(){
			BeanNameAutoProxyCreator beanNameAutoProxyCreator = new BeanNameAutoProxyCreator();
			beanNameAutoProxyCreator.setBeanNames(new String[]{"*Service"});
			beanNameAutoProxyCreator.setInterceptorNames(new String[]{"transactionInterceptor"});
			return beanNameAutoProxyCreator;
		}*/
		
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
