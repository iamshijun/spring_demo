package feng.shi.test;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import feng.shi.java_config.InfrastructureContextConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
		InfrastructureContextConfiguration.class
})
@ActiveProfiles("test")
public class SpringWebMvcFeatureTest {

	@Resource
	private DataSource dataSource;
	
	@Test
	public void dummy(){
		
	}
}
