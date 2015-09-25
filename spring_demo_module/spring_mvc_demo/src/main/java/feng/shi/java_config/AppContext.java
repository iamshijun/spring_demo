package feng.shi.java_config;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.support.ResourceBundleMessageSource;

import feng.shi.entity.Todo;
import feng.shi.service.TodoService;

@Configuration
@Profile({"qa","product"})
public class AppContext {
	
	private Logger logger  = LoggerFactory.getLogger(getClass());

	@Bean
	@Qualifier("qa")  
	public TodoService todoService(){
		logger.info("create real TodoService implemention");
		return new TodoService() {
			@Override
			public List<Todo> findAll() {
				return Collections.emptyList();
			}
		};
	}
	
	@Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();

        messageSource.setBasename("i18n/messages");
        messageSource.setUseCodeAsDefaultMessage(true);

        return messageSource;
    }
}
