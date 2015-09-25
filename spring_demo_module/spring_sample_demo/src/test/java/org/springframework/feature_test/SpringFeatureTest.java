package org.springframework.feature_test;


import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.springframework.test.util.ReflectionTestUtils.getField;

import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.sql.DataSource;

import org.hamcrest.Matchers;
import org.hibernate.SessionFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.HibernateTransactionManager;
import org.springframework.orm.hibernate3.annotation.AnnotationSessionFactoryBean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import cjava.walker.common.repository.GroupRepository;
import cjava.walker.common.service.BookingService;
import cjava.walker.common.service.FooService;
import cjava.walker.common.service.JobService;
import cjava.walker.support.JobFinishEvent;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes ={
		SpringFeatureTest.TestConfiguration.class,
		SpringFeatureTest.ModelLayerContext.class
		})
@TransactionConfiguration

public class SpringFeatureTest {

	@Autowired/*(required=false)*/ 
	private BookingService bookingService;
	
	@Autowired
	private cjava.walker.common.Configuration config;
	
	@Resource 
	private Environment environment;
	
	@Autowired
	private JobService jobService;
	
	@Autowired
	private FooService fooService;
	
	@Inject
	private ApplicationContext applicationContext;
	
	@Inject // Java Config Bean
	private TestConfiguration testConfiguration;
	
	@Resource 
	private GroupRepository groupRepository;
	
	@Autowired
	private ApplicationEventMulticaster eventMulticaster;
	
	@Value("#{dataSource}") //直接拿到  beanFactory中的 指定名称的  beanName
	private DataSource dataSource;
	
	
	@Before
	public void setUp(){
		assertThat(config,notNullValue());
		assertThat(environment,notNullValue());
		assertThat(applicationContext,notNullValue());
		assertThat(testConfiguration,notNullValue());
		
		assertThat(eventMulticaster,notNullValue());
		assertThat(dataSource,notNullValue());
	}
	
	@Test
	public void testDummy(){
	}
	
	@Test
	public void shouldReturnAllDependenciesForBean(){
		AutowireCapableBeanFactory capableBeanFactory = applicationContext.getAutowireCapableBeanFactory();
		if(capableBeanFactory instanceof ConfigurableBeanFactory){
			ConfigurableBeanFactory singleBeanRegistry = (ConfigurableBeanFactory) capableBeanFactory;
			String[] dependenciesForBean = singleBeanRegistry.getDependenciesForBean("bookingService");
			Assert.assertThat(dependenciesForBean, Matchers.hasItemInArray("jdbcTemplate"));
			
			System.out.format("Bean bookingService : dependent on : %s",
					Arrays.toString(dependenciesForBean));
		}
	}
	
	@Test
	public void testScheduleJob() throws InterruptedException, ExecutionException{
		final CountDownLatch latch = new CountDownLatch(1); 
		eventMulticaster.addApplicationListener(new ApplicationListener<JobFinishEvent>() {
			@Override
			public void onApplicationEvent(JobFinishEvent event) {
				latch.countDown();
			}
		});

		jobService.doLongTermJob();
		
		Future<Integer> future = jobService.doLongTermJobAndReturn(41);
		System.out.println(future.get());
		
		latch.await();
		//TimeUnit.MINUTES.sleep(1);
	}
	
	//private ExpectedException exception  = ExpectedException.none();
	
	@Test
	public void testValueAnnotation(){
		assertThat(config.getRandonNum(),notNullValue());
		assertThat((String)getField(config, "serviceBroke"), equalTo("tcp://192.168.1.188"));
		
		assertThat(environment.getProperty("mail.none"),nullValue());
		assertThat(environment.getRequiredProperty("mail.from"),equalTo("aimysaber@sina.com"));
	}

	@Test
	public void testAspectJAutoProxy() throws Exception{
		fooService.foo();
		
		Future<Integer> future = jobService.doLongTermJobAndReturn(41);
		System.out.println(future.get());	
	}
	
	@Test
	public void testSimpleTransaction(){
		groupRepository.doSomeProcess(null);
		
		bookingService.book("Alice", "Bob", "Carol");
        Assert.assertEquals("First booking should work with no problem",
                3, bookingService.findAllBookings().size());

        try {
            bookingService.book("Chris", "Samuel");
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }

        Assert.assertEquals("'Samuel' should have triggered a rollback", //cause "Smauel" length is greater than varchar(5)
                3, bookingService.findAllBookings().size());

        try {
            bookingService.book("Buddy", null);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }

        Assert.assertEquals("'null' should have triggered a rollback",
                3, bookingService.findAllBookings().size());

	}
	
	
	@Configuration
	@EnableAsync
	@EnableScheduling
	@EnableAspectJAutoProxy
	@ComponentScan(basePackages={"cjava.walker.common"})
	@PropertySource(value={"email.properties","config.properties"})
	//@ImportResource(value="springTest/import.xml") //可以引入其他的xml/properties的bean配置文件
	public static class TestConfiguration{
      
		@Bean
		PropertyPlaceholderConfigurer configurer(){
			PropertyPlaceholderConfigurer configurer = new PropertyPlaceholderConfigurer(); 
			configurer.setFileEncoding("utf-8");
			configurer.setSystemPropertiesMode(PropertyPlaceholderConfigurer.SYSTEM_PROPERTIES_MODE_OVERRIDE);
			configurer.setLocations(new org.springframework.core.io.Resource[]{
						new ClassPathResource("config_test.properties",SpringFeatureTest.class),
						new ClassPathResource("config.properties")
			});
			return configurer;
		}
		
		
	}
	
	@Configuration
	@EnableTransactionManagement(proxyTargetClass=true)
	public static class ModelLayerContext{
	  /* not work well		
		@Value(value = "${jdbc.driverClass}") 
		String driverClass;
	     ....*/
		
		@Bean/*(autowire=Autowire.BY_TYPE)*/
		BookingService bookingService() {
			return new BookingService();
		}

		@Bean
		JdbcTemplate jdbcTemplate(DataSource dataSource) {// 这里的参数名称必须和方法名称一样.!
			//类似 <constructor-arg ref="" />其实就是说这里必须和@Bean方法名称对应上! 気がします
			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
			System.out.println("Creating tables");
			jdbcTemplate.execute("drop table BOOKINGS if exists");
			jdbcTemplate.execute("create table BOOKINGS("
					+ "ID serial, FIRST_NAME varchar(5) NOT NULL)");
			return jdbcTemplate;
		}
		
		@Bean
		HibernateTemplate hibernateTemplate(SessionFactory  sessionFactory){
			return new HibernateTemplate(sessionFactory);
		}

		@Bean //factoryBean 通过getObjectType得到创建的Bean的类型 这里就能知道使用哪一个FactoryBean来创建SessionFactory
		HibernateTransactionManager transactionManager(SessionFactory sessionFactory){//using sessionFactoryBean 
			return new HibernateTransactionManager(sessionFactory);
		}
		
		/*@Bean
		DataSource c3p0DataSource(){ //c3p0
			ComboPooledDataSource dataSource = new ComboPooledDataSource();
			try {
				dataSource.setDriverClass(Driver.class.getName());
			} catch (PropertyVetoException e) {
				//it supposed to throw(translate) the spring exception
			}
			
			dataSource.setUser("root");
			dataSource.setPassword("eucita");
			dataSource.setJdbcUrl("jdbc:mysql:///test?zeroDateTimeBehavior=convertToNull&characterEncoding=UTF8");
			
			dataSource.setInitialPoolSize(5);
	        dataSource.setMinPoolSize(5);
	        dataSource.setMaxPoolSize(10);
	        dataSource.setAcquireIncrement(10);
	        dataSource.setMaxIdleTime(10);        
			
			return dataSource;
		}*/
		
		@Bean
		DataSource dataSource(){//h2database
			 return new SimpleDriverDataSource() {{
		            setDriverClass(org.h2.Driver.class);
		            setUsername("sa");
		            setUrl("jdbc:h2:mem");
		            setPassword("");
		        }};
		}
		
		/*@Bean
		EmbeddedDatabaseFactoryBean embededDataSource(){
			EmbeddedDatabaseFactoryBean databaseFactoryBean = new EmbeddedDatabaseFactoryBean();
			//注意需要在类路径中加入 h2数据库的 jar包哦
			databaseFactoryBean.setDatabaseType(EmbeddedDatabaseType.H2);
			
			return databaseFactoryBean;
		}
*/
		@Bean
		AnnotationSessionFactoryBean sessionFactoryBean(/*DataSource dataSource*/) {
			AnnotationSessionFactoryBean factoryBean = new AnnotationSessionFactoryBean();
			
			/*Properties hibernateProperties = new Properties();
			ClassPathResource resource = new ClassPathResource("hibernate.properties");
			try {
				hibernateProperties.load(resource.getInputStream());
			} catch (IOException e) {
				throw new IllegalStateException("Read File hibernate.properties failed");
			}*/
			
			//using the utility class(PropertiesLoaderUtils) in spring!!!
			Properties hibernateProperties = new Properties();
			try {
				PropertiesLoaderUtils.loadProperties(new ClassPathResource("hibernate.properties"));
			} catch (IOException e) {
				throw new IllegalStateException("Read File hibernate.properties failed",e);
			}
			
//			factoryBean.setDataSource(dataSource);
//			factoryBean.setDataSource(c3p0DataSource());
			factoryBean.setDataSource(dataSource());
			
			factoryBean.setPackagesToScan(new String[]{"cjava.walker.entity"});
			factoryBean.setHibernateProperties(hibernateProperties);
			
			return factoryBean;
		}
	}
}
