package cjava.walker.testcase;


import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

/**
 * 读取hibernate配置的类 约定是:你需要将key的值和hibernate配置的名称一致!
 * 现在只支持classpath下的配置! todo extend this!
 */
public class HibernatePropertiesReader {
	
	public static Properties read(String path) throws IOException {
		
		
		//ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		//ClassLoader.getSystemClassLoader();
		Resource resource = new ClassPathResource(path);
//		Properties properties = new Properties();
//		properties.load(resource.getInputStream());
		
		//using the utils provided by spring
		Properties properties = PropertiesLoaderUtils.loadProperties(resource);

		Enumeration<Object> propertyNames = properties.keys();
		while (propertyNames.hasMoreElements()) {
			String name = (String) propertyNames.nextElement();
			if (!name.startsWith("hibernate")) {
				properties.remove(name);
			}
		}
		return properties;
	}
}
