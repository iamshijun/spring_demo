package cjava.walker.common.service;

import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import cjava.walker.support.JobFinishEvent;

@Service
public class JobService {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private ApplicationEventMulticaster eventMulticaster;

	@Scheduled(initialDelay = 1000, fixedRate = 5000)
	public void doPerid(){
		logger.info("Hello ^_^ : " + System.currentTimeMillis());
	}
	
	@Async
	public Future<Integer> doLongTermJobAndReturn(int n ){
		//....
		if(n < 0)
			return new AsyncResult<Integer>(-1);
		return new AsyncResult<Integer>(fibnaci(n));
	}
	
	@Async
	public void doLongTermJob(){
		try {
			Thread.sleep(25 * 1000);
		} catch (InterruptedException e) {
			return;//ignore
		}
		System.out.println("JobService.doLongTermJob() finished");
		
		eventMulticaster.multicastEvent(new JobFinishEvent(this));
	}
	
	
	public int fibnaci(int n) {
		return (n == 0 | n == 1) ? n : fibnaci(n - 1) + fibnaci(n - 2);
	}
	
	public static void main(String[] args) {
		int res = new JobService().fibnaci(41);
		System.out.println(res);
	}
}
