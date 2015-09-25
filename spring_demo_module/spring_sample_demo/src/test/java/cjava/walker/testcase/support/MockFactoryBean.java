package cjava.walker.testcase.support;

import org.mockito.Mockito;
import org.springframework.beans.factory.FactoryBean;

public class MockFactoryBean<T> implements FactoryBean<T> {
	
	private final Class<T> clazz;
	private boolean isSingleton = true;
	
	public MockFactoryBean(Class<T> clazz){
		this.clazz = clazz; 
	}

	public void setSingleton(boolean isSingleton) {
		this.isSingleton = isSingleton;
	}
	
	@Override
	public T getObject() throws Exception {
		return Mockito.mock(clazz);
	}

	@Override
	public Class<T> getObjectType() {
		return clazz;
	}

	@Override
	public boolean isSingleton() {
		return isSingleton;
	}

}
