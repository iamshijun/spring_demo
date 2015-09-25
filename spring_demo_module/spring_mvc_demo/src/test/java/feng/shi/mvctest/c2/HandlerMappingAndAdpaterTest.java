package feng.shi.mvctest.c2;


import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.util.Arrays;
import java.util.Map;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import feng.shi.mvctest.SpringMvcWebTest;
import feng.shi.support.DelegateViewResolver;
 
@ContextConfiguration("c2.xml")
public class HandlerMappingAndAdpaterTest extends SpringMvcWebTest{

	
	private MockMvc mockMvc;
	
	@Before
	public void setUp(){
		mockMvc = webAppContextSetup(this.getWebApplicationContext()).build();
		
		System.out.println("ActiveProfiles : " + Arrays.toString(this.getWebApplicationContext().getEnvironment().getActiveProfiles()));
	}
	
	@Test
	public void testRequest() throws Exception{
		mockMvc.perform(
				get("/hello").accept(MediaType.ALL))
			.andDo(MockMvcResultHandlers.print())
			.andExpect(status().isOk())
			.andExpect(view().name("hello"))
			.andExpect(model().attributeExists("message"))
			.andExpect(model().attribute("message","Hello World!"))
//			.andExpect(content().contentType(MediaType.valueOf("application/json")))
		;
	}
	
	@Test
	public void testViewResovler(){
		DelegateViewResolver delegateViewResolver = getWebApplicationContext().getBean(DelegateViewResolver.class);
		Map<String, ViewResolver> resolversMap = delegateViewResolver.getResolvers();
		Assert.assertThat(resolversMap,
				Matchers.allOf(
						hasEntry(equalTo("json"), any(ViewResolver.class)),
						hasEntry(equalTo("jsp"),Matchers.instanceOf(InternalResourceViewResolver.class)),
						hasEntry(equalTo("ftlViewResolver"), Matchers.instanceOf(FreeMarkerViewResolver.class))
			));
		for(String key : resolversMap.keySet()){
			System.out.println(key);
			System.out.println("\t|--" + resolversMap.get(key).getClass().getSimpleName());
		}
	}
	
	@Test
	public void testDummy(){
		
	}
}
