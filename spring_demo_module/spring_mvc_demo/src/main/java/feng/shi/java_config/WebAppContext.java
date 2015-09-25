package feng.shi.java_config;
import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

@Configuration 
@EnableWebMvc  //Add this annotation to an @Configuration class to have the Spring MVC configuration defined in
//@EnableWebMVC需要和@Configuration 结合使用   又正如和@WebAppConfiguration需要和@Configuration结合使用一样 具体的流程remain  XXX
@ComponentScan(basePackages = { "feng.shi.controller" })
public class WebAppContext {

	@Bean
	MyWebConfigurer myWebConfigurer(){
		return new MyWebConfigurer();
	}
	
	static class MyWebConfigurer extends WebMvcConfigurerAdapter{
		//最开始网友是将extends WebMvcConfigurerAdapter加到WebAppContext上的.这里只是为了更好的了解这个过程多出来一个类
		
		public void addResourceHandlers(ResourceHandlerRegistry registry) {
			//相当于<mvc:resources location="/static/" mapping="/static/**"/>
			registry.addResourceHandler("/static/**").addResourceLocations("/static/");
		}
		
		public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
			configurer.enable();//<mvc:default-servlet-handler />
		}
	}

	@Bean
	public SimpleMappingExceptionResolver exceptionResolver() {
		SimpleMappingExceptionResolver exceptionResolver = new SimpleMappingExceptionResolver();

		Properties exceptionMappings = new Properties();

		//key:fully qulaified exception,value : error view(just the view name-arbitrary)
		exceptionMappings.put("feng.shi.exception.TodoNotFoundException", "error/404");
		exceptionMappings.put("java.lang.Exception", "error/error");
		exceptionMappings.put("java.lang.RuntimeException", "error/error");

		exceptionResolver.setExceptionMappings(exceptionMappings);

		Properties statusCodes = new Properties();
		// key : viewname , value : statue code response to client-side users!
		statusCodes.put("error/404", "404");
		statusCodes.put("error/error", "500");

		exceptionResolver.setStatusCodes(statusCodes);

		return exceptionResolver;
	}

	@Bean
	public ViewResolver viewResolver() {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();

		viewResolver.setViewClass(JstlView.class);
		viewResolver.setPrefix("/WEB-INF/jsp/");
		viewResolver.setSuffix(".jsp");

		return viewResolver;
	}
}
