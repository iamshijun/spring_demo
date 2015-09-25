package feng.shi.support;

import org.springframework.util.ObjectUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import feng.shi.java_config.AppContext;
import feng.shi.java_config.WebAppContext;

public class MyDispatcherInitializer /*extends AbstractAnnotationConfigDispatcherServletInitializer */{

	protected WebApplicationContext createRootApplicationContext() {
		Class<?>[] rootConfigClasses = this.getRootConfigClasses();
		if (!ObjectUtils.isEmpty(rootConfigClasses)) {
			AnnotationConfigWebApplicationContext rootAppContext = new AnnotationConfigWebApplicationContext();
			rootAppContext.getEnvironment().setActiveProfiles("qa");
			
			rootAppContext.register(rootConfigClasses);
			return rootAppContext;
		} else {
			return null;
		}
	}
	
	protected Class<?>[] getRootConfigClasses() {
		return new Class[]{AppContext.class};
		//1. 这里如果不重写createRootApplicationContext的话 不能设置active的profile.remain求设置方式.重写后如上!
		//2. 不重寫createRootApplicationContext 就需要把AppContext上的@Profile注解注释掉!
	}

	protected Class<?>[] getServletConfigClasses() {
		return new Class[]{WebAppContext.class};
	}

	protected String[] getServletMappings() {
		return null;
	}

}
