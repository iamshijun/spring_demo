package cjava.walker.testcase;

import java.net.URL;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.junit.Test;

public class ApacheConfigurationTest {
	
	@Test
	public void testRead() throws ConfigurationException{
		URL url = getClass().getClassLoader().getResource("test.properties");
		PropertiesConfiguration configuration = new PropertiesConfiguration(url);
		
		//���� include�����ܹ���������properties�ļ�
		System.out.println(configuration.getProperty("mail.host"));
	}
}
