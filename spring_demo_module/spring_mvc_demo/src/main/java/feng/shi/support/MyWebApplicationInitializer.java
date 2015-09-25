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
		//active profile Ҫ��register/����xml�ļ�֮ǰ!
//		root.getEnvironment().setActiveProfiles("test");
		root.getEnvironment().setActiveProfiles("product");//FIXME ����ΪӲ���� Ѱ����õ�����ʱ��
		
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
		
		//���ʹ�õ�JavaConfig�ķ�ʽ
		/*AnnotationConfigWebApplicationContext rootContext =  
	        new AnnotationConfigWebApplicationContext();  
	        rootContext.register(AppConfig.class);  
	        container.addListener(new ContextLoaderListener(rootContext));*/
		
		return appContext;
	}

	protected void registerDispatcher(ServletContext container,WebApplicationContext appContext){
		//��� DispatcherServlet(��̬ע��|���Servlet)
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
