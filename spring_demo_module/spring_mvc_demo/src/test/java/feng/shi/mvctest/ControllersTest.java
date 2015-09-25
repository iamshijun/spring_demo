package feng.shi.mvctest;

import static feng.shi.support.ContextVariablesHolder.getValOfType;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.isA;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import feng.shi.mvctest.controllers.MyMultiActionHandler;
import feng.shi.mvctest.controllers.TestController;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {
		ControllersTest.RootContext.class,
		ControllersTest.MyWebMvcConfigurer.class
	}
)
public class ControllersTest {

	@Autowired
	private WebApplicationContext wac;
	
	@Autowired
	private Map<String,ApplicationContext> wacMap;
	
	@Autowired(required=false)
	private TestController testController;

	private MockMvc mockMvc;

	@Before
	public void setUp() {
		Assert.assertNotNull("wac should not be null", wac);
		mockMvc = webAppContextSetup(wac).build();
		
		System.out.println("==============" + wacMap.size() + "==============");
		
		//������ճ���Ļ� TestController����null ,��Ϊ��ǰ�� wacͬʱ��ΪDispatcherServlet��wac (����) ���ָ��ӹ�ϵ
//		Assert.assertNull("controller cannot got any chance to wired in", testController);
	}

	@Test
	public void testDummy() {

	}
	
	@Test
	public void testPrerequisite(){
		Map<String, HandlerMapping> beansOfHandlerMapping = wac.getBeansOfType(HandlerMapping.class);
		System.out.println(beansOfHandlerMapping);
		Map<String, HandlerAdapter> beansOfHandlerType = wac.getBeansOfType(HandlerAdapter.class);
		System.out.println(beansOfHandlerType);
	}
	
	@Test
	public void testGetWacBindOnDispatcherServlet(){
		try {
			mockMvc.perform(get("/test_get_ser_wac"))/*.andDo(print())*/;
			WebApplicationContext swac =feng.shi.support.ContextVariablesHolder.getValOfType(
					DispatcherServlet.WEB_APPLICATION_CONTEXT_ATTRIBUTE,WebApplicationContext.class
				);
			System.out.println(swac);
			
			//������Ҫע���һ���� spring-test���Կ�� TestDispatcherServletʹ�õ��� ��ǰ��root wac��Ϊ�����wac(ע��DispatcherServlet��һ��wacΪ�����Ĺ��캯��o)
			Assert.assertNull(swac.getParent());
			Assert.assertTrue(swac== wac);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testModlAttribute() throws Exception{
		try {
			mockMvc.perform(get("/testModelAttribute_inm"))
				//.andDo(print())
				.andExpect(model().attribute("now", 
						allOf(
							isA(Date.class),
							equalTo(getValOfType("now", Date.class))
						)
					)
				)
		;
							
			mockMvc.perform(get("/testModelAttribute_inmp?message=hello world"))
				//.andDo(print())
				.andExpect(model().attribute("now",
						allOf(
							isA(Date.class),
							equalTo(getValOfType("now", Date.class))
						)
					)
			    )
			    .andExpect(model().attribute("message",equalTo("hello world"))); //����messageû�����Ļ�   valueΪ""�մ� --���ǵÿ�������resolver!
			;
		
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	@Test
	public void testStaticResourceRequest() throws Exception{
		try {
			mockMvc.perform(
					get("/static/css/main.css"))
				.andDo(print())
				.andExpect(status().is(200))
				.andExpect(content().contentType(MediaType.valueOf("text/css"))) 
				.andExpect(header().string("Last-Modified", "1403334912000"))
			;
			
			Assert.assertTrue(new MediaType("text", "css").equals(MediaType.valueOf("text/css")));
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Test
	public void testMultActionRequest() throws Exception {
		try {
			mockMvc.perform(
					get("/foo/bar/handle")) //����MultiController�е�Ĭ��ParameterResolver�����·������Ϣ �Ҵ��Ӧ�ķ��� ����Ϊhandle
				.andDo(print())
				.andExpect(handler().handlerType(MyMultiActionHandler.class))
				.andExpect(status().is(200))
				.andExpect(view().name("hello"))
				.andExpect(forwardedUrl("/WEB-INF/pages/jsp/hello.jsp"))
			;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Configuration
	@ComponentScan("feng.shi.component")
	static class RootContext {
	}

	@EnableWebMvc
	@Configuration
	@ComponentScan("feng.shi.mvctest.controllers")
	static class MyWebMvcConfigurer extends WebMvcConfigurerAdapter {
		@Override
		public void configureDefaultServletHandling(
				DefaultServletHandlerConfigurer configurer) {
			//configurer.enable();
		}
		
		@Override
		public void addResourceHandlers(ResourceHandlerRegistry registry) {
			//<mvc:resources location="/static/" mapping="/static/**"/>
			registry.addResourceHandler("/static/**").addResourceLocations("/static/");
		}
		

		@Override
		public void addReturnValueHandlers(
				List<HandlerMethodReturnValueHandler> returnValueHandlers) {
			super.addReturnValueHandlers(returnValueHandlers);
		}

		@Bean
		public ViewResolver viewResolver() {
			InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
			viewResolver.setViewClass(JstlView.class);

			viewResolver.setPrefix("/WEB-INF/pages/jsp/");
			viewResolver.setSuffix(".jsp");

			return viewResolver;
		}

//		@Bean(name = "mah")
		public MyMultiActionHandler multiActionHandler() {
			return new MyMultiActionHandler();
		}
		
		@Bean
		public SimpleUrlHandlerMapping simpleUrlHandlerMapping() {
			SimpleUrlHandlerMapping simpleUrlHandlerMapping = new SimpleUrlHandlerMapping();
			Map<String, Object> urlMap = new HashMap<>();
			urlMap.put("/foo/bar/*", "mah"); 
			//�������������  default-servlet-handle�Ļ� �Ͳ���ƥ����� ��ֱ��ƥ�� /* ��default-servlet ��Ϊdefault-servlet-handlerͬ���½���һ��SimpleHandlerMapping,
			//������Ϊû�����ö�Ӧ��order �ͻ������ȱ���� ��ʹ��default servlet. ���������Ҿ�Ϊ��ǰ��handlerMapping������order
			simpleUrlHandlerMapping.setUrlMap(urlMap);
			simpleUrlHandlerMapping.setOrder(1);
			return simpleUrlHandlerMapping;
		}
	}
}
