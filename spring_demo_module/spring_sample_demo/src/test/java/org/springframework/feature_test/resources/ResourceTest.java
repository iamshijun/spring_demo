package org.springframework.feature_test.resources;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.ContextResource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

public class ResourceTest {

	@Test
	public void testResourceLoader() throws IOException {
		ResourceLoader loader = new DefaultResourceLoader();
		
		Resource resource = loader
				.getResource("classpath:cn/javass/spring/chapter4/test1.txt");
		// ��֤���ص���ClassPathResource
		Assert.assertEquals(ClassPathResource.class, resource.getClass());
		
		Resource resource2 = loader
				.getResource("file:cn/javass/spring/chapter4/test1.txt");
		// ��֤���ص���ClassPathResource
		Assert.assertEquals(UrlResource.class, resource2.getClass());
		
		Resource resource3 = loader
				.getResource("http://cjava.walker.spring/demo/test1.txt");
		// ��֤���ص���ClassPathResource
		Assert.assertEquals(UrlResource.class, resource3.getClass());
		//����(����classpath:ǰ׺��) ֻҪ·��path�������ܹ������� һ�� ��ȷ��URL �������׳�MalformedURLException�� ���᷵��URLREsource 
		//   ��Ȼ�Ļ� �ͻ�ʹ�������ClassPathContextResource(->ClassPathResource������) �����resource4
		
		Resource resource4 = loader
				.getResource("org/springframework/feature_test/");
		
		// ��֤��Ĭ�Ͽ��Լ���ClasspathResource
		Assert.assertTrue(resource4 instanceof ClassPathResource); //ClassPathContextResource
		Assert.assertTrue(resource4 instanceof ContextResource); //ClassPathContextResource�ĸ��ӿ�

		ContextResource cr = (ContextResource) resource4;
		Resource resource5 = cr.createRelative("SpringTest0-context.xml");
		System.out.println(resource5.getFile());
		Assert.assertTrue(resource5 instanceof ClassPathResource);
	}
	
	@Test
	public void testApplicationContextGetResource(){
		ConfigurableApplicationContext ctx = new ClassPathXmlApplicationContext();
		Resource resource = ctx.getResource("org/springframework/feature_test/SpringTest0-context.xml");
		Assert.assertEquals(ClassPathResource.class, resource.getClass());
		ctx.close();
	}
	
	@Test
	public void testResourcePatternResolver() throws IOException{
		ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		Resource[] resources = resolver.getResources("classpath:META-INF/*.MF");
		System.out.println(resources.length); //�����ҵ����� ��һ���ҵ��� ��·���µ�    META-INF/*.MF ����Ϊ�ҵ�classpath�µ� ���� MF��׺�ļ�
		printResourcesURL(resources);
		
		resources = resolver.getResources("classpath:META-INF/MANIFEST.MF");
		Assert.assertEquals(1, resources.length);
		
		resources = resolver.getResources("classpath*:META-INF/MANIFEST.MF");
		Assert.assertTrue(resources.length > 1);
	}
	
	@Test  
	public void testClasspathAsteriskPrefix () throws IOException {  
	     ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();        
	     //�����ض������ƥ�������Resource  
	    //������ͨ��ClassLoader.getResources("META-INF")���ط�ģʽ·������  
	    //Ȼ����б���ģʽƥ��  
	    Resource[] resources=resolver.getResources("classpath*:META-INF/LICENSE.txt");  
	    Assert.assertTrue(resources.length > 1);
	    
	    //�����ض��ģʽƥ���Resource  
	    resources = resolver.getResources("classpath*:META-INF/*.txt");  
	    Assert.assertTrue(resources.length > 1);    
	}
	
	@Test  
	public void testClasspathAsteriskPrefixLimit() throws IOException {  
	    ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();      //������ͨ��ClassLoader.getResources("")����Ŀ¼��  
	    //��ֻ�����ļ�ϵͳ����·��������jar�ĸ�·��  
	    //Ȼ����б���ģʽƥ��  
	    Resource[] resources = resolver.getResources("classpath*:asm-*.txt");  
	    Assert.assertTrue(resources.length == 0);  
	    //��ͨ��ClassLoader.getResources("asm-license.txt")����  
	    //asm-license.txt������com.springsource.net.sf.cglib-2.2.0.jar  
	    resources = resolver.getResources("classpath*:asm-license.txt");  
	    Assert.assertTrue(resources.length > 0);       
	    //��ֻ�����ļ�ϵͳ��·��ƥ���Resource 
	    
	    /*	PathMatchingResourcePatternResolver .getResources
	     * 	1. findPathMatchingResources
	     * 		1) rootDir :(classpath*)
	     *        ����rootDir��ʱ�� ����һϵ�еĽ�ȡ ���ص��� classpath* -> �ٴε���getResources -> findAllClassPathResources
	     *        	���ʱ��� location Ϊȥ��classpath*��·�� -> path : "" ���ַ��� -> getClassLoader().getResources(path); ���صĵ�ǰ�ļ�ϵͳ(��Ŀ·��)!
	     *         
	     *      2) subPattern (LICENS)
	     *          -> �����顡ֻ�ҵ���Ŀ·��classpath�µ����� LICENSǰ׺���ļ�
	     */
	    resources = resolver.getResources("classpath*:LICENS*");  
	    Assert.assertTrue(resources.length == 1);  
	}
	
	private void printResourcesURL(Resource... resources){
		if(resources != null && resources.length > 0){
			for(Resource resource : resources){
				try {
					System.out.println(resource.getURL());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
