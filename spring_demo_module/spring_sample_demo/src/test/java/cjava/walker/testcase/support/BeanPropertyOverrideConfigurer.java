package cjava.walker.testcase.support;

import java.util.Map;
import java.util.Properties;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.propertyeditors.ClassEditor;


/**
 * ������ΪPropertyOverrideConfigurerֻ����֪��bean�����Ƶ�ʱ����ܹ��޸����е�����
 * ���Ƕ��ڲ�֪�����Ƶ���֪�������Ƶ�Beanȥ����Ϊ��
 */
@SuppressWarnings("unchecked")
public class BeanPropertyOverrideConfigurer implements BeanFactoryPostProcessor{

	private Map<String,Properties> beanProperties;
	
	public void setBeanProperties(Map<String, Properties> beanProperties) {
		this.beanProperties = beanProperties;
	}
	
	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		ClassEditor classEditor = new ClassEditor(getClass().getClassLoader());
		//System.out.println(Arrays.toString(beanFactory.getBeanDefinitionNames()));
		
		//beanFactory.getBeanDefinition("org.springframework.context.annotation.internalAutowiredAnnotationProcessor");
		
		for(Map.Entry<String, Properties> beanProperty : beanProperties.entrySet()){
			String key = beanProperty.getKey();
			
			try{
				BeanDefinition definition = beanFactory.getBeanDefinition(key);
				//visit definition property value directly!
				MutablePropertyValues propertyValues = definition.getPropertyValues();
				propertyValues.addPropertyValues(beanProperties.get(key));
			}catch (NoSuchBeanDefinitionException e) {
				classEditor.setAsText(key);
				@SuppressWarnings("rawtypes")
				Class type = (Class) classEditor.getValue();
				if(type != null){
					Object bean = beanFactory.getBean(type);
					if(bean != null){
						BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(bean);
						new BeanWrapperImpl(bean);
						MutablePropertyValues mutablePropertyValues = new MutablePropertyValues(beanProperty.getValue());
						// bw.registerCustomEditor(Resource.class, new ResourceEditor());
						bw.setPropertyValues(mutablePropertyValues,true);
					}
				}
			}
		}
	}
}