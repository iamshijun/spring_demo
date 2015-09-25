package feng.shi.java_config;

import java.util.Locale;

import org.mockito.Mockito;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import feng.shi.service.TodoService;

@Profile({"test","dev"})
@Configuration
public class TestContext {

    @Bean
    public MessageSource messageSource() {
    	MessageSource messageSource = Mockito.mock(MessageSource.class);
    	
		Mockito.when(messageSource.getMessage(
				Mockito.anyString(), Mockito.any(Object[].class), Mockito.any(Locale.class))
			).thenReturn("invoke getMessage(String code,Object[] args,Locale)")
		;
		/*
		ResourceBundleMessageSource resourceBundleMessageSource = new ResourceBundleMessageSource();
		messageSource.setBasename("i18n/messages");
        messageSource.setUseCodeAsDefaultMessage(true);
        //.... other settings
		ResourceBundleMessageSource spy = Mockito.spy(resourceBundleMessageSource);
		Mockito.doReturn("hi").when(spy).getMessage(
						Mockito.anyString(), Mockito.any(Object[].class), Mockito.any(Locale.class)
		);*/
		
    	
        return messageSource;
    }

    @Bean
    public TodoService todoService() {
    	System.out.println("create mock TodoService");
        return Mockito.mock(TodoService.class);
    }
}