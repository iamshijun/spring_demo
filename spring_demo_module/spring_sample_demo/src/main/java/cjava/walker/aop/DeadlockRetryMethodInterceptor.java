package cjava.walker.aop;

import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.dao.DeadlockLoserDataAccessException;

import cjava.walker.annotation.DeadlockRetry;

public class DeadlockRetryMethodInterceptor implements MethodInterceptor {

	private static final Logger LOGGER = LoggerFactory.getLogger(DeadlockRetryMethodInterceptor.class);

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		Object obj = invocation.getThis();
		Method method = invocation.getMethod();//这里如果使用的是JdkDynamicAopProxy,返回的是被调用的接口的抽象方法
		//System.out.println(method.getDeclaringClass());
		//这个才是真正的obj(target)上的目标方法(具体接口方法的实现)
		Method targetMethod = obj.getClass().getMethod(method.getName(), method.getParameterTypes());
		
		/*//////////////我加的////////////////////////
		if(targetMethod.isBridge()){//因为上面的方法得到的很有可能是一个Bridge桥方法
			List<Class<?>> realparameterType = new ArrayList<Class<?>>(invocation.getArguments().length);
			for(Object argument : invocation.getArguments()){
				realparameterType.add(argument.getClass());
			}
			targetMethod = obj.getClass().getMethod(method.getName(), realparameterType.toArray(new Class[0]));
		}*/
		//spring 有自己的找bridged的方法-还是用大神们写的方法ba!!!
		targetMethod = BridgeMethodResolver.findBridgedMethod(targetMethod);//XXX
		
		////////////////////////////////////////////
		
		DeadlockRetry dlRetryAnnotation = targetMethod.getAnnotation(DeadlockRetry.class);
		if(dlRetryAnnotation== null){
			return invocation.proceed();
		}
		
		int maxTries = dlRetryAnnotation.maxTries();
		int tryIntervalMillis = dlRetryAnnotation.tryIntervalMillis();
		LOGGER.info(String.format("maxTries : %s ,tryIntervalMillis : %s", maxTries, tryIntervalMillis));
		//return invocation.proceed();
		for (int i = 0; i < maxTries; i++) {
			try {
				LOGGER.info("Attempting to invoke " + invocation.getMethod().getName());
				Object result = invocation.proceed(); // retry
				LOGGER.info("Completed invocation of " + invocation.getMethod().getName());
				return result;
			} catch (Throwable e) {
				Throwable cause = e;
				//... put the logic to identify DeadlockLoserDataAccessException or LockAcquisitionException
				//...in the cause. If the execption is not due to deadlock, throw an exception 
				if (tryIntervalMillis > 0) {
					try {
						Thread.sleep(tryIntervalMillis);
					} catch (InterruptedException ie) {
						LOGGER.warn("Deadlock retry thread interrupted", ie);
					}
				}
				if(cause instanceof DeadlockLoserDataAccessException){
					//如果是死锁异常 重试 | 超过最大可重试次数 抛出异常
					if(i == maxTries - 1){
						//gets here only when all attempts have failed
						throw new RuntimeException("DeadlockRetryMethodInterceptor failed to successfully execute target "
								+ " due to deadlock in all retry attempts", new DeadlockLoserDataAccessException(
								"Created by DeadlockRetryMethodInterceptor", null));
					}else{
						continue;
					}
				}else{
					throw cause;
				}
			}

		}
		return null;
	}
}