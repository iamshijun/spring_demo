package feng.shi.mvctest.c4;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import feng.shi.controller.MyAbstractCommandController;
import feng.shi.mvctest.SpringMvcWebTest;
 
@ContextConfiguration("c4.xml")
public class HandlerMappingAndAdpaterTest4 extends SpringMvcWebTest{

	
	private MockMvc mockMvc;
	
	@Before
	public void setUp(){
		mockMvc = webAppContextSetup(this.getWebApplicationContext()).build();
	}
	
	@Test
	public void testRequest() throws Exception{
		mockMvc.perform(
				get("/hello").accept(MediaType.ALL))
//			.andDo(MockMvcResultHandlers.print())
			.andExpect(status().isOk())
			.andExpect(view().name("hello"))
			.andExpect(model().attributeExists("message"))
			.andExpect(model().attribute("message","Hello World!"))
		;
	}
	@Test
	public void testRequestWithoutModel() throws Exception{
		try{
			mockMvc.perform(
					get("/helloWithoutReturnModelAndView").accept(MediaType.ALL))
					.andDo(MockMvcResultHandlers.print())
					.andExpect(status().isOk())
					.andExpect(content().string("Hello World!!"))
//					.andExpect(view().name("hello"))
//					.andExpect(model().attributeExists(names))
					// model view��Ϊnull ��Ҫ���Ҳ²� view="", model.size(0)���Ǵ���� 
					;
		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}
	}
	
	
	/**
	 * ע��: �����Ҫ��web.xml������ servlet ������ע�� ��Ϊ�ڲ����оͲ���ʹ��
	 * tomcat(�ȷ�����)������ɨ��ע���˵�servlet,filter��������
	 */
	@Test
	@Ignore
	public void testServletForwardingController() throws Exception{
		try{
			mockMvc.perform( 
					get("/forwardToServlet").accept(MediaType.ALL))
					.andDo(MockMvcResultHandlers.print())
					.andExpect(status().isOk())
					.andExpect(content().string("Controller forward to Servlet"))
					;
		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}
	}
	
	@Test
	public void testAbstractCommand() throws Exception{
		try{
			MvcResult result = mockMvc.perform( 
				get("/abstractCommand?username=shijun&password=123")
					.accept(MediaType.ALL))
					.andDo(MockMvcResultHandlers.print())
					.andExpect(handler().handlerType(MyAbstractCommandController.class))
					.andExpect(status().isOk())
					.andExpect(view().name("abstractCommand"))
					.andExpect(model().attributeExists("user"))
					.andExpect(model().attribute("user", Matchers.allOf(
								Matchers.hasProperty("username",Matchers.equalTo("shijun")),
								Matchers.hasProperty("password",Matchers.equalTo("123"))
							)))
//					.andExpect(content().string("shijun-123"))//el ��Ҫ����ִ��?????
					.andReturn()
				;
			
			String response = result.getResponse().getContentAsString();
			Object user = result.getRequest().getAttribute("user");
			if(user!=null){
				System.out.println(user.getClass());
			}
			System.out.println("Response : " + response);
		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}
	}
	
	
}

	