package footmark.springdata.jpa.test;

import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.data.domain.PageRequest;

import footmark.springdata.jpa.service.UserService;

public class SimpleSpringJpaDemo {
    @SuppressWarnings("resource")
	public static void main(String[] args) {
    	GenericXmlApplicationContext xmlApplicationContext = new GenericXmlApplicationContext();
    	xmlApplicationContext.registerShutdownHook();
    	xmlApplicationContext.load("spring-demo-cfg.xml");
    	xmlApplicationContext.refresh();
    	
        UserService userService = xmlApplicationContext.getBean("userService", UserService.class);
//        AccountInfo createNewAccount = userService.createNewAccount("g", "ggg", 700);
//        System.out.println(createNewAccount);
        System.out.println(userService.findByBalanceGreaterThan(100, new PageRequest(1, 2)));
        
//        xmlApplicationContext.close();
    }
}
