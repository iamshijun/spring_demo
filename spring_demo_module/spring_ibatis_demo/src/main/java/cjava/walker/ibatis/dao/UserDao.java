package cjava.walker.ibatis.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import cjava.walker.ibatis.entity.User;

@SuppressWarnings({"deprecation","unchecked"})
public class UserDao extends SqlMapClientDaoSupport {

	public List<User> getAllUsers() {
		return getSqlMapClientTemplate().queryForList("getAllUsers");
	}
	
	public List<User> findByExample(User user){
		return getSqlMapClientTemplate().queryForList("findByExample",user);
	}
	
	public int deleteUsersByIds(List<Integer> ids) {
        
        List<User> users=new ArrayList<User>();
        for(Integer id : ids){
        	 User p1=new User(id);
        	 users.add(p1);
        }
        
        Map<String, Object> map = new HashMap<>();
        map.put("users", users);
//        map.put("sex","man");
        return getSqlMapClientTemplate().delete(
                                "deleteUsersByIds", map); // "user.deleteUsersByIds"  //if you have the namespace set
    }
	
	public int insertUser(User user){
		return ((Number) getSqlMapClientTemplate().insert("insertUsers", user)).intValue();
	}
}
