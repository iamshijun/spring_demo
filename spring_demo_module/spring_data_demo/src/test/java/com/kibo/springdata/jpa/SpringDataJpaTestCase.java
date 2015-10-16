package com.kibo.springdata.jpa;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.kibo.springdata.jpa.domain.AccountInfo;
import com.kibo.springdata.jpa.service.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring/applicationContext-data-jpa.xml"})
@ActiveProfiles("dev")
public class SpringDataJpaTestCase {
	
	@Autowired
	UserService userService;
	
	@Autowired
	DataSource dataSource;
	
	@Test
	public void testGetConnection() throws SQLException{
		Connection connection = dataSource.getConnection("sa","");
		System.out.println(connection);
	}
	
	@Test
	public void testFindByBalanceGreaterThan()	{
		AccountInfo createNewAccount = userService.createNewAccount("g", "ggg", 700);
		System.out.println(createNewAccount);
		System.out.println(userService.findByBalanceGreaterThan(100, new PageRequest(1, 2)));
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
