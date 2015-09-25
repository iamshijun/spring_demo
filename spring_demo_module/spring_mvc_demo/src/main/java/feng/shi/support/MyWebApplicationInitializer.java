package feng.shi.support;

import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import feng.shi.java_config.AppContext;
import feng.shi.java_config.DevContext;

public class MyWebApplicationInitializer /*implements WebApplicationInitializer */{

	ApplicationContext createRootApplicationContext(){
		//GenericApplicationContext root = new GenericXmlApplicationContext("/spring/common.xml");//resourceLocations
		
/*		GenericXmlApplicationContext root = new GenericXmlApplicationContext();//resourceLocations
		root.load(new ClassPathResource("/spring/common.xml"));
		root.getEnvironment().setActiveProfiles("product");
		
		root.refresh();//if we set the resource in constructor we should not invoke refresh again!
		root.registerShutdownHook();
*/
		
		AnnotationConfigApplicationContext root = new AnnotationConfigApplicationContext();
		//active profile 要在register/加载xml文件之前!
//		root.getEnvironment().setActiveProfiles("test");
		root.getEnvironment().setActiveProfiles("product");//FIXME 这里为硬编码 寻求更好的设置时机
		
		root.register(DevContext.class,AppContext.class);
		root.refresh();

		return root;
	}
	
	protected WebApplicationContext createRootWebApplicationContext(){
		return null;
	}
	
	public void onStartup(ServletContext container) {
		ApplicationContext root = createRootApplicationContext();
		
		WebApplicationContext appContext = createContext(root);
		
		registerDispatcher(container, appContext);
		registerFilter(container);
	}
	
	protected WebApplicationContext createContext(ApplicationContext parent){
		XmlWebApplicationContext appContext = new XmlWebApplicationContext();
		appContext.setConfigLocation("/WEB-INF/classes/mvc/dispatcher-servlet.xml");
		appContext.setParent(parent);
		
		//如果使用到JavaConfig的方式
		/*AnnotationConfigWebApplicationContext rootContext =  
	        new AnnotationConfigWebApplicationContext();  
	        rootContext.register(AppConfig.class);  
	        container.addListener(new ContextLoaderListener(rootContext));*/
		
		return appContext;
	}

	protected void registerDispatcher(ServletContext container,WebApplicationContext appContext){
		//添加 DispatcherServlet(动态注册|添加Servlet)
		/* add self-defined servlet */
		DispatcherServlet dispatcherServlet = new DispatcherServlet(appContext);
//		dispatcherServlet.setContextInitializerClasses(contextInitializerClasses)
		
		ServletRegistration.Dynamic servletRegistration = 
				container.addServlet("dispatcher", dispatcherServlet);
		
		servletRegistration.setLoadOnStartup(1);
		servletRegistration.addMapping("/");
	}
	
	private void registerFilter(ServletContext container){
//		FilterRegistration.Dynamic filterRegistration = container.addFilter(filterName, filterClass)
	}
}
