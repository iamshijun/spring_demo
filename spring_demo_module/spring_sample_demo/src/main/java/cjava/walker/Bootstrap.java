package cjava.walker;

import java.util.Map;

import org.springframework.aop.Advisor;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import cjava.walker.common.service.IBookingService;

public class Bootstrap {
	@SuppressWarnings("resource")
	public static void main(String[] args) {
		//new ClassPathXmlApplicationContext("classpath:*.xml");//为什么linux上不行?只能用下面的形式!
		//new ClassPathXmlApplicationContext(new String[] { "applicationContext.xml", "applicationContext-mail.xml","quartz.xml" });

		ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext(new String[] { "classpath:spring/*.xml" });
		
		/* TransactionInterceptor beans*/
		Map<String, TransactionInterceptor> beansOfTransactionInterceptor = applicationContext.getBeansOfType(TransactionInterceptor.class);
		System.out.println(beansOfTransactionInterceptor);
		System.out.println();
		
		Map<String, Advisor> beansOfAdvisor = applicationContext.getBeansOfType(Advisor.class);
		System.out.println(beansOfAdvisor);
		
		
//		BookingService bookingService = applicationContext.getBean(BookingService.class); //see 没有接口定义的情况下
//		bookingService.findAllBookings();
		
		IBookingService bookingService = applicationContext.getBean(IBookingService.class);
		bookingService.findAllBookings();
		
//		Object bookingService = applicationContext.getBean("bookingService");
//		System.out.println(Arrays.toString(bookingService.getClass().getInterfaces()));
		
	}
}
