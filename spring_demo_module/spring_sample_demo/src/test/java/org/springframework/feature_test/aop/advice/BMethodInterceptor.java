package org.springframework.feature_test.aop.advice;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class BMethodInterceptor implements MethodInterceptor {
	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		System.out.println("BMethodInterceptor.invoke()");
		return invocation.proceed();
	}
}
