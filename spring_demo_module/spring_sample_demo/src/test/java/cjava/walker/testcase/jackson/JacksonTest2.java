package cjava.walker.testcase.jackson;
import java.io.IOException;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;

import cjava.walker.entity.FixMenu;


public class JacksonTest2 {
	
	@Test
	public void test01() throws IOException{
		ObjectMapper objectMapper = new ObjectMapper();
		JsonFactory jsonFactory = objectMapper.getJsonFactory();
		
		JsonGenerator jsonGenerator = jsonFactory.createJsonGenerator(System.out);
		
		jsonGenerator.writeObject(FixMenu.ZSLL);
	}
}
