package org.springframework.feature_test.schedule;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class AppService {
	
	private Logger logger = LoggerFactory.getLogger(getClass());

	long lastCronInvokeTime = Long.MAX_VALUE;
	long lastSimpleInvokeTime = Long.MAX_VALUE;

	@Scheduled(cron = "*/5 * * * * ?")
	public void doScheduleTaskWithCron() {
		long t = System.currentTimeMillis() - lastCronInvokeTime;
		logger.info("AppService.doScheduleTaskWithCron() : {}",(t < 0 ? "Begin" : t));
		//可以从log信息中看到执行的线程的名称 是task:scheduler的名称(假设没有设置threadPrefix的话)
		lastCronInvokeTime = System.currentTimeMillis();
	}

	@Scheduled(fixedRate = 10 * 1000)
	// run every 10 seconds
	public void doScheduleTaskWithSimple() {
		long t = System.currentTimeMillis() - lastSimpleInvokeTime;
		logger.info("AppService.doScheduleTaskWithSimple() : {}" ,(t < 0 ? "Begin" : t));
		lastSimpleInvokeTime = System.currentTimeMillis();
	}
	
	public void doScheduleTaskWithXmlDriven(){
		long t = System.currentTimeMillis() - lastSimpleInvokeTime;
		logger.info("AppService.doScheduleTaskWithXmlDriven() : {}" ,(t < 0 ? "Begin" : t));
		lastSimpleInvokeTime = System.currentTimeMillis();
	}

	@Async
	public void doAsync() {
		// mock!
		try {
			long start = System.currentTimeMillis();
			Thread.sleep(5000);
			long end = System.currentTimeMillis();
			logger.info("End Async operation!,Time used : {}", TimeUnit.SECONDS.convert(end - start, TimeUnit.MILLISECONDS));
			//可以从log信息中看到执行的线程的名称 是task:executor的名称(假设没有设置threadPrefix的话)
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Async
	public Future<String> doAsyncWithRet() {
		String ret = "";
		try {
			long start = System.currentTimeMillis();
			Thread.sleep(3500);
			long end = System.currentTimeMillis();
			ret = String.format("End Async operation!,Time used : %s", TimeUnit.SECONDS.convert(end - start, TimeUnit.MILLISECONDS));
		} catch (InterruptedException e) {
			ret = e.getMessage();
		}
		// mock!
		return new AsyncResult<String>(ret);
	}
}
