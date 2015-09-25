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
		/*@Bean //こっちもうまくでいる
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
		 * 宸戦心欺spring嶄議Qualifier聞喘議仇圭 
		 * 1.壓Bean議協吶嶄 涙胎頁壓@Configuration議@Bean嶄珊頁壓xml meta嶄塘崔bena扮昨公協議<qualifer>
		 * 脅氏壓BeanDefition嶄耶紗匯倖Qualifier(AutowireCandidateQualifier)
		 * 辛參心心AbstractBeanDefinition嗤匯倖Map栖贋刈議
		 * 
		 * 2.壓廣秘糞佩議扮昨(@Autowired,@Resource議奉來賜宀頁圭隈貧聞喘@Qualifier議扮昨,
		 * 宸倖扮昨beanFactory祥氏壓侭嗤candidate昨僉議bean嶄Qualifier嶄孀欺才宸倖廣盾謄塘議bean栖序佩廣秘
		 * ,辛參潮範泌惚壓bean協吶議扮昨短嗤公協販採議Qualifier議扮昨
		 * ,恷朔氏功象bean議兆各才廣盾議@Qualifier議峺協峙序佩謄塘曳熟)
		 */
	}
	
	@Test
	public void testBeanWrapperAndPropertyValues(){
		BeanWrapper beanWrapper = new BeanWrapperImpl(UserManager.class);
		
		Assert.assertThat(preferences.getName(),  CoreMatchers.equalTo("mypreference"));
				
		MutablePropertyValues propertyValues = new MutablePropertyValues();
		propertyValues.add("userPreferences", preferences);//乏會載嶷勣填!短嗤�誅黏亠綴把�!
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
