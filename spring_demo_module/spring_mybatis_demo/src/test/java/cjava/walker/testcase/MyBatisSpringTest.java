package cjava.walker.testcase;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.sample.dao.UserDao;
import org.mybatis.spring.sample.mapper.UserMapper;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cjava.walker.mybatis.entity.Sex;
import cjava.walker.mybatis.entity.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/*.xml")
public class MyBatisSpringTest {

	@Resource
	private UserMapper userMapper;

	@Resource
	private UserDao userDao;
	
	@Resource
	private SqlSession sqlSession;
	
	@Before
	public void setUp(){
		Assert.assertNotNull(sqlSession);
		
	}

	@Test
	public void testDummy() {
	}
	
	@Test
	public void testWithSqlSession(){
		User user = sqlSession.selectOne("getUser", 1);
		System.out.println(user.getName());
	}
	
	@Test
	public void testGetUser() {
		try {
			User user = userMapper.getUser(1);
			System.out.println(user.getName());
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Test
	public void testGetUser2() {
		try {
			User user = userDao.getUser(1);
			System.out.println(user.getName());
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Test
	public void testGetAllUser() {
		List<User> allUsers = userMapper.getAllUsers();
		for (User user : allUsers) {
			System.out.println(user.getName());
		}
	}

	@Test
	@Ignore
	public void testInsert() {
		User user = new User();
		user.setAddTime(new Date());
		user.setAge(22);
		user.setGroupId(1);
		user.setName("wahaha");
		user.setPasswd("222");
		user.setSex(Sex.MALE);

		userMapper.insertUsers(user);
	}
}
