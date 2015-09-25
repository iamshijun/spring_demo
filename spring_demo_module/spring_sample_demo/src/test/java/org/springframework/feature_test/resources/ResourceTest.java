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
		// 验证返回的是ClassPathResource
		Assert.assertEquals(ClassPathResource.class, resource.getClass());
		
		Resource resource2 = loader
				.getResource("file:cn/javass/spring/chapter4/test1.txt");
		// 验证返回的是ClassPathResource
		Assert.assertEquals(UrlResource.class, resource2.getClass());
		
		Resource resource3 = loader
				.getResource("http://cjava.walker.spring/demo/test1.txt");
		// 验证返回的是ClassPathResource
		Assert.assertEquals(UrlResource.class, resource3.getClass());
		//上述(除了classpath:前缀的) 只要路径path给定的能够构建出 一个 正确的URL 即不会抛出MalformedURLException的 都会返回URLREsource 
		//   不然的话 就会使用下面的ClassPathContextResource(->ClassPathResource的子类) 下面的resource4
		
		Resource resource4 = loader
				.getResource("org/springframework/feature_test/");
		
		// 验证返默认可以加载ClasspathResource
		Assert.assertTrue(resource4 instanceof ClassPathResource); //ClassPathContextResource
		Assert.assertTrue(resource4 instanceof ContextResource); //ClassPathContextResource的父接口

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
		System.out.println(resources.length); //仅仅找到的是 第一个找到的 类路径下的    META-INF/*.MF 这里为我的classpath下的 两个 MF后缀文件
		printResourcesURL(resources);
		
		resources = resolver.getResources("classpath:META-INF/MANIFEST.MF");
		Assert.assertEquals(1, resources.length);
		
		resources = resolver.getResources("classpath*:META-INF/MANIFEST.MF");
		Assert.assertTrue(resources.length > 1);
	}
	
	@Test  
	public void testClasspathAsteriskPrefix () throws IOException {  
	     ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();        
	     //将加载多个绝对匹配的所有Resource  
	    //将首先通过ClassLoader.getResources("META-INF")加载非模式路径部分  
	    //然后进行遍历模式匹配  
	    Resource[] resources=resolver.getResources("classpath*:META-INF/LICENSE.txt");  
	    Assert.assertTrue(resources.length > 1);
	    
	    //将加载多个模式匹配的Resource  
	    resources = resolver.getResources("classpath*:META-INF/*.txt");  
	    Assert.assertTrue(resources.length > 1);    
	}
	
	@Test  
	public void testClasspathAsteriskPrefixLimit() throws IOException {  
	    ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();      //将首先通过ClassLoader.getResources("")加载目录，  
	    //将只返回文件系统的类路径不返回jar的跟路径  
	    //然后进行遍历模式匹配  
	    Resource[] resources = resolver.getResources("classpath*:asm-*.txt");  
	    Assert.assertTrue(resources.length == 0);  
	    //将通过ClassLoader.getResources("asm-license.txt")加载  
	    //asm-license.txt存在于com.springsource.net.sf.cglib-2.2.0.jar  
	    resources = resolver.getResources("classpath*:asm-license.txt");  
	    Assert.assertTrue(resources.length > 0);       
	    //将只加载文件系统类路径匹配的Resource 
	    
	    /*	PathMatchingResourcePatternResolver .getResources
	     * 	1. findPathMatchingResources
	     * 		1) rootDir :(classpath*)
	     *        在找rootDir的时候 经过一系列的截取 返回的是 classpath* -> 再次调用getResources -> findAllClassPathResources
	     *        	这个时候的 location 为去掉classpath*的路径 -> path : "" 空字符串 -> getClassLoader().getResources(path); 返回的当前文件系统(项目路径)!
	     *         
	     *      2) subPattern (LICENS)
	     *          -> だから　只找到项目路径classpath下的所有 LICENS前缀的文件
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
