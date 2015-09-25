package feng.shi.component;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

@Component
public class LifeCycleTest implements ApplicationContextAware ,  ServletContextAware{

	private Logger logger  = org.slf4j.LoggerFactory.getLogger(getClass());
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		logger.debug("#####----------LifeCycleTest.setApplicationContext()-------------");
	}

	
	@Override
	public void setServletContext(ServletContext servletContext) {
		logger.debug("@@@@@----------LifeCycleTest.setServletContext()----------");
	}
} 
