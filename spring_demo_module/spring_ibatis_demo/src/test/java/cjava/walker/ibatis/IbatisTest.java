package cjava.walker.ibatis;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cjava.walker.ibatis.dao.UserDao;
import cjava.walker.ibatis.entity.Sex;
import cjava.walker.ibatis.entity.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/applicationContext.xml")
public class IbatisTest {

	@Resource
	private UserDao userRepository;

	@Test
	public void testDummy() {

	}
	
	@Test
	public void testGetAllUsers(){
		List<User> users = userRepository.getAllUsers();
		for(User user:users){
			System.out.println(user.getSex().getDescription());
		}
	}

	@Test
	public void testInsert() {
		try {
			User user = new User();
			user.setGroupId(1L);
			user.setName("wasioreina");
			user.setAge(20);
			user.setSex(Sex.FEMALE);
			user.setAddTime(new Date());

			userRepository.insertUser(user);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

	}
	
	@Test
	public void testDelete(){
		userRepository.deleteUsersByIds(Arrays.asList(11,12));
	}

}
