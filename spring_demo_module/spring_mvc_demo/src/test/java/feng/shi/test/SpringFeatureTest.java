package feng.shi.test;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.CustomEditorConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import feng.shi.model.PhoneNumberModel;
import feng.shi.model.PhoneNumberModelEditor;

@RunWith(SpringJUnit4ClassRunner.class)
@Configuration
@ContextConfiguration(classes=SpringFeatureTest.class)
public class SpringFeatureTest {

	@Autowired
	private ConfigurableBeanFactory configurableBeanFactory;
	
	@Test
	public void testBeanFactoryGetBeanAndConverter(){
		PhoneNumberModel myPhoneNumber = configurableBeanFactory.getBean("myPhoneNumber",PhoneNumberModel.class);
		System.out.println(myPhoneNumber);
	}
	
	@Bean
	public String myPhoneNumber(){
		return new String("0757-82711768");
	}
	
	@Bean
	//假设 PhoneNumberModelEditor和PhoneNumberModel不在同一个目录下或者editor名称前缀和bean的名称不一样
	public CustomEditorConfigurer customEditorConfigurer(){
		CustomEditorConfigurer cec = new CustomEditorConfigurer();
		Map<String,Object> customEditors = new HashMap<>();
		customEditors.put(PhoneNumberModel.class.getName(), PhoneNumberModelEditor.class);
		cec.setCustomEditors(customEditors);
		return cec;
	}
}

