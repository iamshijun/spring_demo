package feng.shi.support;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.web.context.ConfigurableWebApplicationContext;

//得到想过请把implements注释去掉
public class SpringMvcProfilesInitializer /*implements
		ApplicationContextInitializer<ConfigurableWebApplicationContext>*/ {

	public void initialize(ConfigurableWebApplicationContext ctx) {
		
		ConfigurableEnvironment environment = ctx.getEnvironment();
		List<String> profiles = new ArrayList<String>(getProfiles());
		
		if (profiles == null || profiles.isEmpty()) {
			throw new IllegalArgumentException("Profiles have not been configured");
		}
		
		environment.setActiveProfiles(profiles.toArray(new String[0]));
	}

	//TODO add logic
	private Collection<String> getProfiles() {
		//        return Lists.newArrayList("file_based", "test_data");
		return Arrays.asList("test");
	}
}