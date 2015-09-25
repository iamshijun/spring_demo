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
			//ΪTodoRepositoryImpl������
			Assert.assertTrue(TodoRepositoryImpl.class.isAssignableFrom(todoRepository.getClass()));
		}else{
			//��proxyTargeClassΪfalse��ʱ�򼴵�ǰʹ���� jdk�Ķ�̬����  ��������� ע�� 
			//@Autowired private TodoRepository todoRepository;
			//����д�ɾ������  
			//@Autowired private TodoRepositoryImpl todoRepositoryImpl
			// �����ǻᱨ��� ��Ϊ��ǰrepository�����͸����Ͳ��� ���ʵ������ ����Proxyʵ�� ����ڶ���assert���϶���
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
			proxyCreator.setProxyTargetClass(proxyTargetClass);//Ĭ��Ϊalse
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
