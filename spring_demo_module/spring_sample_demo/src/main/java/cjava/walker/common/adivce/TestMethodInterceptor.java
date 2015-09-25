package cjava.walker.common.adivce;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class TestMethodInterceptor implements MethodInterceptor{

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		System.out.println("TestMethodInterceptor.invoke() : Before invoaction proceed");
		Object ret = invocation.proceed();
		System.out.println("TestMethodInterceptor.invoke() : After invoaction proceed");
		return ret;
	}

}
