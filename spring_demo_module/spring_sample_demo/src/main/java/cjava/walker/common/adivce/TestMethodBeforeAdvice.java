package cjava.walker.common.adivce;

import java.lang.reflect.Method;

import org.springframework.aop.MethodBeforeAdvice;

public class TestMethodBeforeAdvice implements MethodBeforeAdvice{

	@Override
	public void before(Method method, Object[] args, Object target)
			throws Throwable {
		System.out.println("TestMethodBeforeAdvice.before()");
	}

}
