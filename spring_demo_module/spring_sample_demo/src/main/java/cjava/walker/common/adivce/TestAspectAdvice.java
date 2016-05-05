package cjava.walker.common.adivce;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/*
 * 如果使用了ComponentScan的话 这里加上@Component ,就可以不用在@Configuration注解的类中添加这个Bean的创建
 * @Bean TestAspectAdvice testAspectAdvice(){....}
 */
@Aspect
@Component
public class TestAspectAdvice {
	/*--combining pointcut expressions--*/
	@Pointcut("execution(public * *(..))")
	private void anyPublicOperation() {}

	@Pointcut("within(com.xyz.someapp.trading..*)")
	private void inTrading() {}

	@Pointcut("anyPublicOperation() && inTrading()") //<- combie with the point declared above
	private void tradingOperation() {}
	/*
	 *  It is a best practice to build more complex pointcut expressions out of smaller named components as shown above.
	 *  When referring to pointcuts by name, normal Java visibility rules apply
	 *  (you can see private pointcuts in the same type, protected pointcuts in the hierarchy, public pointcuts anywhere and so on). 
	 *  Visibility does not affect pointcut matching.*/
	
	/*--end--*/
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Before("execution(* cjava.walker..*.Job*.do*Job*(..))")
	//invoke before the JobService method match the pattern "do*Job*"
	public void doBefore(JoinPoint joinPoint) {//这里的joinPoint不是ProceedingJoinPoint,为MethodInvocationProceedingJoinPoint
		logger.debug("joinPoint class : {}",joinPoint.getClass());
		logger.info(String.format("Ready to do Job Name : [%s]", joinPoint.getSignature()));
	}
	
	@Around("execution(* cjava.walker.common.service..*Service.*(..))")
	public void doRound(JoinPoint joinPoint){//ProceedingJoinPoint
		logger.debug("joinPoint class : {}",joinPoint.getClass());
		logger.info("TestAspectAdvice.doRound() " + joinPoint);
	}

}
