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
		//1. �����������дcreateRootApplicationContext�Ļ� ��������active��profile.remain�����÷�ʽ.��д������!
		//2. ���،�createRootApplicationContext ����Ҫ��AppContext�ϵ�@Profileע��ע�͵�!
	}

	protected Class<?>[] getServletConfigClasses() {
		return new Class[]{WebAppContext.class};
	}

	protected String[] getServletMappings() {
		return null;
	}

}
