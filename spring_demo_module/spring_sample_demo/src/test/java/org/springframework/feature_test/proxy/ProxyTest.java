package org.springframework.feature_test.proxy;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.aop.framework.Advised;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.proxy.Factory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.ClassUtils;

import cjava.walker.common.repository.TestRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class ProxyTest {

	/*@Autowired
	private TestRepositoryImpl testRepository;*/
	
	@Autowired
	private TestRepository testRepository;
	
	@Test
	public void testTargetType() throws Exception{
		
		System.out.println("Class of Injection :" + testRepository.getClass());
		
		Class<?>[] allInterfacesForClass = ClassUtils.getAllInterfacesForClass(testRepository.getClass(), 
																			getClass().getClassLoader());
		
		if(allInterfacesForClass.length > 0){
			System.out.println();
			System.out.println("//--------Proxy's Interfaces start--------");
			for(Class<?> interf : allInterfacesForClass){
				System.out.println(interf);
			}
			System.out.println("//--------Proxy's Interfaces end--------");
		}

		if(testRepository instanceof Advised){
			System.out.println();
			System.out.println("\\\\---------Advised checked start ----------");
			Advised advisedBean = (Advised) testRepository;
			System.out.println("TargetClass : " + advisedBean.getTargetClass());
			System.out.println("TargetSource :" + advisedBean.getTargetSource().getTarget());
			System.out.println("ProxiedInterfaces : " + Arrays.toString(advisedBean.getProxiedInterfaces()));
			System.out.println("\\\\---------Advised checked end ----------");
		}
		if(testRepository instanceof Factory){
			System.out.println();
			Factory factory = (Factory) testRepository;
			System.out.println("//---------Callbacks start ----------");
			System.out.println("Callbacks : " + Arrays.toString(factory.getCallbacks()));
			System.out.println("//---------Callbacks end----------");
		}
		
		System.out.println();
		testRepository.getEntityById(1L);
	}
}
