package org.springframework.feature_test.schedule;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.lessThanOrEqualTo;

import java.net.URISyntaxException;
import java.net.URL;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class TaskExecutorTest {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private AppService appService;
	
//	TaskExecutorFactoryBean
	@Autowired
	private ThreadPoolTaskExecutor taskExecutor;
	
	@Autowired
	private ThreadPoolTaskScheduler taskScheduler;
	
	@Before
	public void setUp(){
		Assert.assertNotNull(taskExecutor);
	}
	
	
	@Test
	public void testDummy() {
		
	}
	
	@Test
	public void checkTaskExecutorSetting(){
		//internal j.u.c ThreadPoolExecutor
		ThreadPoolExecutor threadPoolExecutor = taskExecutor.getThreadPoolExecutor();
		System.out.println(threadPoolExecutor.getQueue());
		/*
		taskExecutor.getCorePoolSize();
		taskExecutor.getKeepAliveSeconds();
		taskExecutor.getPoolSize();
		*/
	}
	
	@Test
	public void testProgrammatically() throws InterruptedException {

		CronTrigger cronTrigger = new CronTrigger("*/3 * * * * ?");
		long start = System.currentTimeMillis();
		final int iteration = 5;
		final CountDownLatch countDownLatch = new CountDownLatch(iteration);
		taskScheduler.schedule(new Runnable() {
			@Override
			public void run() {
				logger.info("count down : {}",countDownLatch.getCount());
				countDownLatch.countDown();
			}
		}, cronTrigger);

		countDownLatch.await();
		long end = System.currentTimeMillis();
		
		//assertThat((int) (end - start) / 1000, both(greaterThanOrEqualTo(30)).and(lessThan(40)));
		assertThat((int) (end - start) / 1000, lessThanOrEqualTo(3*iteration));
	}	
	
	
	@After
	public void tearDown(){
		
	}
	
	
	public void init() throws URISyntaxException{
		String context = getClass().getSimpleName() + "-context.xml";
		URL url = getClass().getResource(context);
		String file = url.toURI().getSchemeSpecificPart();
		@SuppressWarnings("resource")
		AbstractApplicationContext ctx = new FileSystemXmlApplicationContext(file);
		appService = ctx.getBean(AppService.class);

		ctx.registerShutdownHook();
	}
	
	public void testTaskExecute(){
		long start = System.currentTimeMillis();
		appService.doAsync();
		long elipsed = System.currentTimeMillis() - start;
		logger.info("Invoke AppService.doAsyn and time used:",elipsed);
	}
	
	public static void main(String[] args) throws Exception {
		//因为对于线程相关的测试 Junit不会等线程的执行完毕 就会立即返回的,并且因为任务的状态不清楚所以就不能使用一般的测试(CountDonwnLatch,CyclicBarrier..来等待任务的执行完毕) 
		//所以这里使用main线程来进行测试
		TaskExecutorTest test = new TaskExecutorTest();
		test.init();
		test.testTaskExecute();
	}
	
}
