package org.mybatis.spring.sample.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import cjava.walker.mybatis.entity.User;

public interface UserMapper {
  @Select("SELECT * FROM users WHERE id = #{userId}")
//  @ResultMap(value="org.mybatis.spring.sample.mapper.UserMapper.userMap")
  @ResultMap(value="userMap")
  User getUser(@Param("userId") Integer userId);
  
  List<User> getAllUsers();

  Integer insertUsers(User user);
} 