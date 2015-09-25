package feng.shi.mvctest;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.WebApplicationContext;

/**
 * Spring WebMvc Test used Base class
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public abstract class SpringMvcWebTest {

	private WebApplicationContext wac;
	
	@Autowired
	public void setWebApplicationContext(WebApplicationContext wac) {
		this.wac = wac;
	}
	
	protected WebApplicationContext getWebApplicationContext() {
		return wac;
	}
}
