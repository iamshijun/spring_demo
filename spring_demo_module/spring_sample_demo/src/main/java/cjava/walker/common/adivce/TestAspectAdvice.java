package cjava.walker.common.adivce;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/*
 * ���ʹ����ComponentScan�Ļ� �������@Component ,�Ϳ��Բ�����@Configurationע�������������Bean�Ĵ���
 * @Bean TestAspectAdvice testAspectAdvice(){....}
 */
@Aspect
@Component
public class TestAspectAdvice {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Before("execution(* cjava.walker..*.Job*.do*Job*(..))")
	//invoke before the JobService method match the pattern "do*Job*"
	public void doBefore(JoinPoint joinPoint) {//�����joinPoint����ProceedingJoinPoint,ΪMethodInvocationProceedingJoinPoint
		logger.debug("joinPoint class : {}",joinPoint.getClass());
		System.out.format("Ready to do Job Name : [%s] \n", joinPoint.getSignature());
	}
	
	@Around("execution(* cjava.walker.common.service.FooService.*(..))")
	public void doRound(JoinPoint joinPoint){//ProceedingJoinPoint
		logger.debug("joinPoint class : {}",joinPoint.getClass());
		System.out.println("TestAspectAdvice.doRound() " + joinPoint);
	}

}
