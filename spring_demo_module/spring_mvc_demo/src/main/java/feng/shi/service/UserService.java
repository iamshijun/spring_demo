package feng.shi.service;

import java.util.List;

import feng.shi.model.UserModel;

public interface UserService {
	
	void create(UserModel userModel);

	void update(UserModel userModel);

	void delete(UserModel userModel);

	UserModel get(String username);

	List<UserModel> list();
}
