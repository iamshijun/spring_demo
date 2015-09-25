package cjava.walker.support;

import java.io.IOException;

import org.aopalliance.aop.Advice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jndi.JndiObjectFactoryBean;
import org.springframework.orm.hibernate3.annotation.AnnotationSessionFactoryBean;

import cjava.walker.annotation.DeadlockRetry;
import cjava.walker.aop.DeadlockRetryMethodInterceptor;
import cjava.walker.testcase.HibernatePropertiesReader;

@Configuration
public class JavaConfig {

	@Bean
	public DefaultAdvisorAutoProxyCreator advisorAutoProxy(){
		return new DefaultAdvisorAutoProxyCreator();
	}
	
	@Bean
	public AnnotationMatchingPointcut deadlockRetryPointcut(){
		return new AnnotationMatchingPointcut(null, DeadlockRetry.class);
	}
	
	@Bean
	public DeadlockRetryMethodInterceptor deadlockRetryMethodInterceptor(){
		return new DeadlockRetryMethodInterceptor();
	}
	
	/*@Bean(name="myAppDeadlockRetryPointcutAdvisor")
	public DefaultPointcutAdvisor myAppDeadlockRetryPointcutAdvisor(){
		return new DefaultPointcutAdvisor(deadlockRetryPointcut(), deadlockRetryMethodInterceptor());
	}*/
	@Bean(name="myAppDeadlockRetryPointcutAdvisor")
	public DefaultPointcutAdvisor myAppDeadlockRetryPointcutAdvisor(Pointcut pointcut,Advice advice){
		return new DefaultPointcutAdvisor(pointcut, advice);
	}
	
	@Bean
	public PropertiesFactoryBean myhibernateProperties() throws IOException{
		PropertiesFactoryBean fb = new PropertiesFactoryBean();
		fb.setProperties(HibernatePropertiesReader.read("config.properties"));//fix this
		return fb;
	}
	
	@Bean
	public JndiObjectFactoryBean dataSource(){
		JndiObjectFactoryBean factoryBean = new JndiObjectFactoryBean();
		factoryBean.setJndiName("java:comp/env/jdbc/datasource/myapp-ds");
		return factoryBean;
	}
	
	@Bean
	public AnnotationSessionFactoryBean sessionFactory() throws IOException{
		AnnotationSessionFactoryBean factoryBean = new AnnotationSessionFactoryBean();
		factoryBean.setPackagesToScan(new String[]{"cjava.walker.entity"});
		factoryBean.setHibernateProperties(myhibernateProperties().getObject());
		return factoryBean;
	}
	
}
