package org.springframework.feature_test;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.UnsupportedEncodingException;

import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;

public class SpringModuleExpectionTest {
	
	@Test
	public void testLineNumberReader(){
		try {
			Resource resource=  new ClassPathResource("config_test.properties");
			EncodedResource encodedResource = new EncodedResource(resource, "UF-8");
			
			LineNumberReader reader = new LineNumberReader(encodedResource.getReader());
			
			String msg = null;
			while((msg = reader.readLine())!=null){
				System.out.println(msg);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
