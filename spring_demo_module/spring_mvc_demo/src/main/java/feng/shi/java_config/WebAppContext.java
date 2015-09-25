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
//@EnableWebMVC��Ҫ��@Configuration ���ʹ��   �������@WebAppConfiguration��Ҫ��@Configuration���ʹ��һ�� ���������remain  XXX
@ComponentScan(basePackages = { "feng.shi.controller" })
public class WebAppContext {

	@Bean
	MyWebConfigurer myWebConfigurer(){
		return new MyWebConfigurer();
	}
	
	static class MyWebConfigurer extends WebMvcConfigurerAdapter{
		//�ʼ�����ǽ�extends WebMvcConfigurerAdapter�ӵ�WebAppContext�ϵ�.����ֻ��Ϊ�˸��õ��˽�������̶����һ����
		
		public void addResourceHandlers(ResourceHandlerRegistry registry) {
			//�൱��<mvc:resources location="/static/" mapping="/static/**"/>
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
