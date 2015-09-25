package feng.shi.mvctest;


import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.internal.util.collections.IdentitySet;
import org.mockito.internal.util.collections.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.IfProfileValue;
import org.springframework.test.annotation.ProfileValueSource;
import org.springframework.test.annotation.ProfileValueSourceConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import feng.shi.entity.Todo;
import feng.shi.entity.Todo.TodoBuilder;
import feng.shi.java_config.AppContext;
import feng.shi.java_config.TestContext;
import feng.shi.java_config.WebAppContext;
import feng.shi.service.TodoService;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
		AppContext.class,
		TestContext.class, 
		WebAppContext.class,
	})
//@ContextConfiguration(locations = {"classpath:testContext.xml", "classpath:exampleApplicationContext-web.xml"})
@WebAppConfiguration
@ActiveProfiles("dev") //选择使用哪一个 profile (即 environment.setActiveProfile(String...))
//在applicationContext还没有被完全初始化 ,没有开始读入BeanDefinition的时候 ((ConfigurableEnvironment)webApplicationContext.getEnvironment()).setActiveProfiles("dev");
//BeanDefinitionReader在读取xml , (java_config) 会对配置中指定的profile进行 判断 Reader在建立的时候会设置Environment对象 所以可以使用  其中的 acceptProfiles(profile):boolean 方法 判断当前的profile是否可以接受(即当前的指定的profile是否actived)
//如果哦是可接受(actived)的话 就会进行 读取 将 <beans>标签里面的bean都会读入并注册到BeanFactory中 

@ProfileValueSourceConfiguration(value=
	WebApplicationContextTodoControllerTest.CustomizeProfileValConfiguration.class
) //默认是使用 Systemproperties 作为 PropertyValue的提供 即在运行的时候 可以通过   java XXXX  -Dtest_with_dev=true 来完成对下面测试的实现 
public class WebApplicationContextTodoControllerTest {
	
	public static class CustomizeProfileValConfiguration implements
			ProfileValueSource {

		@Override
		public String get(String key) {
			if ("test_with_dev".equals(key))
				return "YES";
			else
				return "FALSE";
		}

	}

    private MockMvc mockMvc;


    @Autowired
    private TodoService todoServiceMock;
    
    //quick hand to create mock object!
//    @Mock
//    private TodoService todoServiceMock;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setUp() {
        //We have to reset our mock between tests because the mock objects
        //are managed by the Spring container. If we would not reset them,
        //stubbing and verified behavior would "leak" from one test to another.
    	//IMPL
    	IdentitySet set = Sets.newIdentitySet();
    	set.add(
    			((ConfigurableEnvironment)webApplicationContext.getEnvironment()).getActiveProfiles()
    	);
    	if(set.contains("dev")){
    		System.err.println("mock reset mockobject: TodoServiceMock");
    		Mockito.reset(todoServiceMock);
    	}
        

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }
    
    @Test
    public void testDummy(){
    	
    }
    
	@Test
	public void testStaticResourceRequest() throws Exception{
		try {
			mockMvc.perform(
					get("/static/css/main.css"))
				.andDo(print())
				.andExpect(status().is(200))
				.andExpect(content().contentType(MediaType.valueOf("text/css"))) 
				.andExpect(header().string("Last-Modified", "1403338511211"))
			;
			
			Assert.assertTrue(new MediaType("text", "css").equals(MediaType.valueOf("text/css")));
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

    
    @Test
    @IfProfileValue(name = "test_with_dev",value="YES") //根据特定的Profile name/value 判断是否执行测试方法 
    public void testFindAllShouldAddTodoEntriesAndRenderTodoListView() throws Exception{
    	Todo first =   new TodoBuilder()
				    	.id(1L)
				        .description("Lorem ipsum")
				        .title("Foo")
				        .build();
    	
    	Todo second = new TodoBuilder()
    					.id(2L)
				        .description("Lorem ipsum")
				        .title("Bar")
				        .build();
    	
    	when(todoServiceMock.findAll()).thenReturn(Arrays.asList(first,second));
    	
    	mockMvc.perform(get("/findAll"))
    		.andExpect(status().isOk()) //StatusResultMatchers 
    		.andExpect(view().name("todo/list"))
    		.andExpect(forwardedUrl("/WEB-INF/jsp/todo/list.jsp"))
    		.andExpect(model().attribute("todos", hasSize(2)))
    		.andExpect(model().attribute("todos", hasItem(
    				allOf(
    					hasProperty("id",is(1L)),
    					hasProperty("title",is("Foo")),
    					hasProperty("description",is("Lorem ipsum"))
    				)
    			)))
  			.andExpect(model().attribute("todos", hasItem(
    					allOf(
    							hasProperty("id",is(2L)),
    							hasProperty("title",is("Bar")),
    							hasProperty("description",is("Lorem ipsum"))
    					)
    			)))
    			.andDo(print())
    	;
    	
    	verify(todoServiceMock,times(1)).findAll();//findAll method invoke only 1 time!
    	verifyNoMoreInteractions(todoServiceMock);
    }
    
    
}