package cjava.walker.testcase;


import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

/**
 * ��ȡhibernate���õ��� Լ����:����Ҫ��key��ֵ��hibernate���õ�����һ��!
 * ����ֻ֧��classpath�µ�����! todo extend this!
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
