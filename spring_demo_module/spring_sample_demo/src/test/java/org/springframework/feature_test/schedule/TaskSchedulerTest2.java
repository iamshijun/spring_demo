package org.springframework.feature_test.schedule;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TaskSchedulerTest2.TestContext.class)
@EnableScheduling
public class TaskSchedulerTest2 {
	
/*	@Scheduled(initialDelay=1000,fixedDelay=1000)
	public void doScheduleInConfiguration(){
		System.out.println("TaskSchedulerTest2.doScheduleInConfiguration() " + System.currentTimeMillis());
	}*/

	@Autowired
	private AppService appService;
	
	@Test
	public void testDoAsyncWithRet(){
		Future<String> doAsyncWithRet = appService.doAsyncWithRet();
		try {
			System.out.println(doAsyncWithRet.get());
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}
	

	@Configuration
	static class TestContext {
		@Bean
		public AppService appService(){
			return new AppService();
		}
	}

}
