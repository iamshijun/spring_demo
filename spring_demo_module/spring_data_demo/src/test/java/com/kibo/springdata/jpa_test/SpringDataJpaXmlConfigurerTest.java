package com.kibo.springdata.jpa_test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.kibo.springdata.jpa.domain.AccountInfo;
import com.kibo.springdata.jpa.service.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring/applicationContext-data-jpa.xml"})
@ActiveProfiles("product")
public class SpringDataJpaXmlConfigurerTest implements BeanNameAware{
	
	@Override
	public void setBeanName(String name) {
		System.err.println(name);
	}
	
	@Autowired
	private Environment env;
	@Autowired
	private UserService userService;
	@Autowired
	private DataSource dataSource;
	@Value("#{environment.JAVA_HOME}")
	private String javaHome;
	
	@Test
	public void test01(){
		System.out.println(javaHome);
	}
	
	@Test
	public void testGetConnection() throws SQLException{
		Connection connection;
		if(env.acceptsProfiles("dev")){
			connection = dataSource.getConnection("sa","");//„e¤ËdataSource.getConnection()¤â¤¤¤¤
		}else{
			connection = dataSource.getConnection();
		}
		System.out.println((env.acceptsProfiles("dev") ? "dev" : "product") + " connection : " + connection);
	}
	
	@Test
	public void testFindByBalanceGreaterThan()	{
//		AccountInfo createNewAccount = userService.createNewAccount("g", "ggg", 700);
//		System.out.println(createNewAccount);
		Page<AccountInfo> page = userService.findByBalanceGreaterThan(100, new PageRequest(0, 5));
		String formatString = String.format("TotalElements=%d , TotalPages=%d",page.getTotalElements(),page.getTotalPages());
		System.out.println(formatString);
		List<AccountInfo> accountInfos = page.getContent();
		if(!accountInfos.isEmpty()){
			for(AccountInfo ai : accountInfos){
				System.out.println(ai);
			}
		}
	}
	
	
	
	/*public static void main(String[] args) {
    	GenericXmlApplicationContext xmlApplicationContext = new GenericXmlApplicationContext();
    	xmlApplicationContext.registerShutdownHook();
    	xmlApplicationContext.load("spring/applicationContext-jpa.xml");
    	xmlApplicationContext.refresh();
    	
        UserService userService = xmlApplicationContext.getBean("userService", UserService.class);
        AccountInfo createNewAccount = userService.createNewAccount("g", "ggg", 700);
        System.out.println(createNewAccount);
        System.out.println(userService.findByBalanceGreaterThan(100, new PageRequest(1, 2)));
        xmlApplicationContext.close();
    }*/
}
