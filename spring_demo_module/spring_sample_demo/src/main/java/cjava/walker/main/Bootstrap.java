package cjava.walker.main;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Bootstrap {
	@SuppressWarnings("resource")
	public static void main(String[] args) {
		//new ClassPathXmlApplicationContext("classpath:*.xml");//Ϊʲôlinux�ϲ���?ֻ�����������ʽ!
		//new ClassPathXmlApplicationContext(new String[] { "applicationContext.xml", "applicationContext-mail.xml","quartz.xml" });

		new ClassPathXmlApplicationContext(new String[] { "classpath:spring/*.xml" });
	}
}
