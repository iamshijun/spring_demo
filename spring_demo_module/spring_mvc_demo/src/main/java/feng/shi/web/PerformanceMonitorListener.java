package feng.shi.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.web.context.support.ServletRequestHandledEvent;

public class PerformanceMonitorListener implements	ApplicationListener<ServletRequestHandledEvent> {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public void onApplicationEvent(ServletRequestHandledEvent event) {
		String url = event.getRequestUrl();
		logger.info("------ Request : {}  has been handled ------",url );
	}

}
