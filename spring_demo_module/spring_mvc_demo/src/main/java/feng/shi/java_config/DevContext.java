package feng.shi.java_config;

import java.util.List;

import org.slf4j.Logger;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.support.ResourceBundleMessageSource;

import feng.shi.entity.Todo;
import feng.shi.service.TodoService;

@Profile({"dev"})
@Configuration
public class DevContext {

	private Logger logger  = org.slf4j.LoggerFactory.getLogger(getClass());
	
    @Bean
    public MessageSource messageSource() {
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setBasename("i18n/messages");
        messageSource.setUseCodeAsDefaultMessage(true);
        return messageSource;
    }

    @Bean
    public TodoService todoService() {
    	logger.info("using the dev 'todoService' implemention");
    	return new MyService();
    }
    
    class MyService implements TodoService{
		public List<Todo> findAll() {
			throw new UnsupportedOperationException();
		}
    	
    }
}