package cjava.walker.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;


public class UserManager {

	private DefaultUserPreferences userPreferences;
	
/*	
    @Autowired
    @Qualifier("default")
    private DefaultUserPreferences userPreferences;
*/	
	
	@Autowired
	public void setUserPreferences(@Qualifier(value="default") DefaultUserPreferences userPreferences) {
		this.userPreferences = userPreferences;
	}
	
	public DefaultUserPreferences getUserPreferences() {
		return userPreferences;
	}
}
