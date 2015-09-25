package feng.shi.test;

import java.lang.reflect.Proxy;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.aop.framework.autoproxy.BeanNameAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.ClassUtils;

import feng.shi.repository.TodoRepository;
import feng.shi.repository.impl.TodoRepositoryImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class SpringProxyTest {
	
//	private static boolean proxyTargetClass = false;
	private static boolean proxyTargetClass = true;

	@Autowired
	private TodoRepository todoRepository;
//	@Autowired
//	private TodoRepositoryImpl todoRepositoryImpl;
	
	@Autowired
	private ApplicationContext applicationContext;
	
	@Before
	public void setUp(){
		Assert.assertNotNull("applicationContext cannot be null", applicationContext);
		Assert.assertNotNull("todoRepository cannot be null", todoRepository);
	}
	
	@Test
	public void testDummy(){
		
	}
	
	@Test
	public void testProxyCreator(){
		
		TodoRepository todoRepository = applicationContext.getBean(TodoRepository.class);
		
		if(proxyTargetClass){
			Assert.assertTrue(ClassUtils.isCglibProxy(todoRepository));
			//为TodoRepositoryImpl的子类
			Assert.assertTrue(TodoRepositoryImpl.class.isAssignableFrom(todoRepository.getClass()));
		}else{
			//在proxyTargeClass为false的时候即当前使用者 jdk的动态代理  所有上面的 注入 
			//@Autowired private TodoRepository todoRepository;
			//不能写成具体的类  
			//@Autowired private TodoRepositoryImpl todoRepositoryImpl
			// 这样是会报错的 因为当前repository的类型根本就不是 这个实现类了 而是Proxy实例 正如第二个assert所断定的
			Assert.assertTrue(Proxy.isProxyClass(todoRepository.getClass()));
			Assert.assertThat(todoRepository, CoreMatchers.instanceOf(Proxy.class));
			
		}
		System.out.println(todoRepository.getClass());
	}
	
	///////////////////////////////////////////////////////////////////////////////////
	
	
	@Configuration
	@ComponentScan(basePackages = "feng.shi.repository")
	static class TestContext {

		@Bean
		public BeanNameAutoProxyCreator beanNameAutoProxyCreator() {
			BeanNameAutoProxyCreator proxyCreator = new BeanNameAutoProxyCreator();
			proxyCreator.setInterceptorNames(new String[]{"advice"});
			proxyCreator.setBeanNames(new String[]{"todRepository"});
			proxyCreator.setProxyTargetClass(proxyTargetClass);//默认为alse
			return proxyCreator;
		}

		@Bean
		public MethodInterceptor advice() {
			return new MethodInterceptor() {

				@Override
				public Object invoke(MethodInvocation invocation)
						throws Throwable {
					System.out.println("------------before invocation------------");
					Object ret = invocation.proceed();
					System.out.println("------------after invocation ------------");
					return ret;
				}
			};
		}
	}
}
