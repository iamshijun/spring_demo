package cjava.walker.main;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Bootstrap {
	@SuppressWarnings("resource")
	public static void main(String[] args) {
		//new ClassPathXmlApplicationContext("classpath:*.xml");//为什么linux上不行?只能用下面的形式!
		//new ClassPathXmlApplicationContext(new String[] { "applicationContext.xml", "applicationContext-mail.xml","quartz.xml" });

		new ClassPathXmlApplicationContext(new String[] { "classpath:spring/*.xml" });
	}
}
