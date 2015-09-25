package org.mybatis.spring.sample.dao;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import cjava.walker.mybatis.entity.User;

@Repository
public class UserDaoImpl implements UserDao {

	@Resource
	private SqlSession sqlSession;

	public void setSqlSession(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}

	public User getUser(Integer userId) {
		return (User) sqlSession.selectOne(
				"org.mybatis.spring.sample.mapper.UserMapper.getUserById",
				userId);
	}
}