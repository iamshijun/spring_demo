package cjava.walker.support;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

public class InjectBeanSelfProcessor implements BeanPostProcessor {
	public Object postProcessBeforeInitialization(Object bean, String beanName)
			throws BeansException {
		return bean;
	}

	public Object postProcessAfterInitialization(Object bean, String beanName)
			throws BeansException {
		if (bean instanceof BeanSelfAware) {// ���Beanʵ����BeanSelfAware��ʶ�ӿڣ��ͽ��������ע��
			((BeanSelfAware) bean).setSelf(bean); // ��ʹ��prototype BeanҲ����ʹ�ô��ַ�ʽ
		}
		return bean;
	}
}
