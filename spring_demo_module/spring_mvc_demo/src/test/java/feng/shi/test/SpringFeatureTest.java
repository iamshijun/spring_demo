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

	//ע : ����ɨ��� ��feng.shi.service.impl �µ�ABService��BServiceʵ�� ʹ����@Asyn ���н����Ƕ����뵽�� ����������
	//���� �ͻ��漰����BeanPostProcessor��: AsyncAnnotationBeanPostProcessor , AbstractAutoProxyCreator(SmartInstantiationAwareBeanPostProcess)
	// ��������Ҫ��һ���� AService��BService֮������� ѭ������. ������˵��SmartInitiationAwareBeanPostProcess
	// ����ͨ���� AServiceImpl �е�@Asyn ע��ȥ�� ����ǰ������ʼ���Ƿ�ɹ��Լ������ʱ����ֵĴ��� ���Կ�����������ĵط� 
	//(ps ������Ϊɨ���˳�������Ǵ�AServiceImpl��ʼ��)
	
	//ͬ����ʹ����û��ʹ�õ� ���ض� getEarlyBeanReference�����SmartInitiationAwareBeanPostProcess (DefaultAdvisorAutoProxyCreator / BeanNameAutoProxyCreator)
	//�ڴ���  circular reference������� �ͻ��� getSingleton(beanName)��ȡ��һ���� �������ɱ����õ�bean , �ڶ����ͻ����  initialize����(�׳�exposedObject/ exposure) �� ���������beanʵ�����бȶ�
	//������ڲ��� ������bean������������õ�bean�ͻ��׳� �쳣 (ps ���������������ɵ�bean�� ������ɵ����ձ�¶��ȥʵ�����ڲ���)
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
