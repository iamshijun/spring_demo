package feng.shi.persistence.service;

import javax.persistence.EntityManagerFactory;

import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import feng.shi.persistence.model.User;

@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = PersistenceJPAConfig.class)
@ContextConfiguration(classes = UserServiceTest.class)
@Configuration
@ComponentScan(basePackages = {
		"feng.shi.persistence.config.**",
		"feng.shi.persistence.service.**" 
	})
public class UserServiceTest {

	@Autowired
	UserService userService;
	
	@Autowired
	EntityManagerFactory entityManagerFactory;

	@Test
	public void testDummy() {
	}
	
	@Test
	public void testAddUser(){
		User user = new User("test_username","pw***d");
		user.setDigestPassword("digestPwd");
		user.setExpireAt(new DateTime(2017, 8, 2, 0, 0).toDate());
		userService.addUser(user);
	}
}
