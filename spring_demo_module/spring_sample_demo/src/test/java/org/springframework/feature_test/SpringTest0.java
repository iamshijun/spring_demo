package org.springframework.feature_test;


import java.util.Arrays;

import javax.annotation.Resource;
import javax.inject.Inject;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cjava.walker.entity.DefaultUserPreferences;
import cjava.walker.entity.UserManager;

@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration
@ContextConfiguration(classes={SpringTest0.TestContext.class})
public class SpringTest0 {

	@Resource
	private ApplicationContext applicationContext;
	
	@Autowired
	@Qualifier("foo")
	private DefaultUserPreferences preferences;
	
	@Inject
	private UserManager userManager;
	
	@Configuration
	@ImportResource(value={"classpath:org/springframework/feature_test/SpringTest0-context.xml"})
	static class TestContext{
		/*@Bean //���ä��⤦�ޤ��Ǥ���
		public UserManager userManager(@Qualifier("default") DefaultUserPreferences preferences){
			UserManager userManager = new UserManager();
			userManager.setUserPreferences(preferences);
			return userManager;
		}*/
		
		@Bean
		@Qualifier("pref333") 
		public DefaultUserPreferences preferences3(){
			DefaultUserPreferences pref = new DefaultUserPreferences();
			pref.setName("pref");
			return pref;
		}
	}
	
	@Before
	public void setUp(){
		
	}
	
	@Test
	public void testPropertyPlaceHolderWorkInAlias(){
		String[] aliasNames = applicationContext.getAliases("foo");
		System.out.println(Arrays.toString(aliasNames));
	}
	
	@Test
	public void testPropertyOverride(){
		Assert.assertThat(preferences.getName(),  CoreMatchers.equalTo("mypreference"));
//		Assert.assertThat(preferences.getName(),  CoreMatchers.equalTo("bar"));	//without override
	}
	
	@Test
	public void testQualifier(){
		Assert.assertThat(userManager.getUserPreferences().getName(), CoreMatchers.equalTo("defaultPreference"));
		/*
		 * ���￴��spring�е�Qualifierʹ�õĵط� 
		 * 1.��Bean�Ķ����� ��������@Configuration��@Bean�л�����xml meta������benaʱ�������<qualifer>
		 * ������BeanDefition�����һ��Qualifier(AutowireCandidateQualifier)
		 * ���Կ���AbstractBeanDefinition��һ��Map���洢��
		 * 
		 * 2.��ע��ʵ�е�ʱ��(@Autowired,@Resource�����Ի����Ƿ�����ʹ��@Qualifier��ʱ��,
		 * ���ʱ��beanFactory�ͻ�������candidate��ѡ��bean��Qualifier���ҵ������ע��ƥ���bean������ע��
		 * ,����Ĭ�������bean�����ʱ��û�и����κε�Qualifier��ʱ��
		 * ,�������bean�����ƺ�ע���@Qualifier��ָ��ֵ����ƥ��Ƚ�)
		 */
	}
	
	@Test
	public void testBeanWrapperAndPropertyValues(){
		BeanWrapper beanWrapper = new BeanWrapperImpl(UserManager.class);
		
		Assert.assertThat(preferences.getName(),  CoreMatchers.equalTo("mypreference"));
				
		MutablePropertyValues propertyValues = new MutablePropertyValues();
		propertyValues.add("userPreferences", preferences);//˳�����ҪŶ!û�������е�����!
		propertyValues.add("userPreferences.name", "hello_");
		
		beanWrapper.setPropertyValues(propertyValues);
		
		UserManager userManager = (UserManager) beanWrapper.getWrappedInstance();
		
		Assert.assertThat(preferences.getName(), 
					CoreMatchers.equalTo(userManager.getUserPreferences().getName()));
		System.out.println(userManager.getUserPreferences().getName());
	}
	
	@Test
	public void testDummy(){
	}
	
}
