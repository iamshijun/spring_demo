package cjava.walker.common.adivce;

import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TestPointcutAdvice implements org.aopalliance.intercept.MethodInterceptor{

	private static Logger logger = LoggerFactory.getLogger(TestPointcutAdvice.class);
	
	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		logger.info("Begin invoke .. " + (invocation.getMethod().getDeclaringClass() + "." + invocation.getMethod().getName()));
		Object retVal = invocation.proceed();
		logger.info("End invoke .. " + (invocation.getMethod().getDeclaringClass() + "." + invocation.getMethod().getName()));
		
		return retVal;
	}

}
