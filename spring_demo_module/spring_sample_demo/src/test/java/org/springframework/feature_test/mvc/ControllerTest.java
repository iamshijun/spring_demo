package org.springframework.feature_test.mvc;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.web.context.WebApplicationContext;
 
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration //ע����˿������� webapp��λ��(Ĭ��Ϊsrc/main/webapp)��
//��ǰ������ApplicationContext��Ϊ WebApplicationContext������,��Ȼ�Ļ������ǲ��ܵõ�WebApplicationContext��
@ContextConfiguration("dispatcher-servlet.xml")
public class ControllerTest {

	@Autowired
	private WebApplicationContext wac;
	
	private MockMvc mockMvc;
	
	@Before
	public void setUp(){
		mockMvc = webAppContextSetup(this.wac).build();
	}
	
	@Test
	public void testRequest() throws Exception{
		mockMvc.perform(
				get("/testJackson?id=1&name=shijun").accept(MediaType.APPLICATION_JSON))
			.andDo(MockMvcResultHandlers.print())
			.andExpect(status().isOk())
//			.andExpect(content().contentType(MediaType.valueOf("application/json;charset=UTF-8")))
			.andExpect(content().contentType(MediaType.valueOf("application/json")))
			.andExpect(jsonPath("$.id").value(1))
			.andExpect(jsonPath("$.username").value("shijun"))
		;
	}
}
