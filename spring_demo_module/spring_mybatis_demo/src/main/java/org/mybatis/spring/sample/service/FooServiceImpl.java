package org.mybatis.spring.sample.service;

import org.mybatis.spring.sample.mapper.UserMapper;

import cjava.walker.mybatis.entity.User;

public class FooServiceImpl implements FooService {

	private UserMapper userMapper;

	public void setUserMapper(UserMapper userMapper) {
		this.userMapper = userMapper;
	}

	public User doSomeBusinessStuff(Integer userId) {
		return this.userMapper.getUser(userId);
	}
}