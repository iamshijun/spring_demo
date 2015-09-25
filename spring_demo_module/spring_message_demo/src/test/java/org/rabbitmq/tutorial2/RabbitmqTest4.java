package org.rabbitmq.tutorial2;

import java.io.IOException;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;

import org.junit.Test;
import org.rabbitmq.RabbitmqTestBase;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.QueueingConsumer;

/**
 * IMP 了解Rabbit的消息分发
 */
public class RabbitmqTest4 extends RabbitmqTestBase {

	private final int ITERATION = 10;
	
	public final static String QUEUE_NAME = "hello";
	
	private String dots[] = {".","..","...","....","....."};
	
	@Test
	public void testDummy(){
		
	}
	
	@Test
	public void purgeQueue(){
		super.purgeQueues(QUEUE_NAME);
	}
	
	@Test
	public void testSend() {
		try {
			Connection connection  = createConnection();
			
			Channel channel = connection.createChannel();
			channel.queueDeclare(QUEUE_NAME, false, false, false, null);
			
			for(int i = 0; i < ITERATION ;++i){
				String msg = i + dots[i % 2 == 0 ? 0 : dots.length - 1];
				String message = getMessage(msg);
				channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
				//System.out.println(" [x] Sent '" + message + "'");
			}
			channel.close();
			connection.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	
	private boolean autoAck = false;
	private int prefetchCount = 0;
	
	void choose(int plan){
		switch (plan) {
		case 1:
			prefetchCount = 0;
			autoAck = true;
			break;
		case 2:
			prefetchCount = 0;
			autoAck = false;
			break;
		case 3:
			prefetchCount = 1;
			autoAck = true;
			break;
		case 4:
			prefetchCount = 1;
			autoAck = false;
			break;
		default:
			prefetchCount = 0;
			autoAck = false;
			break;
		}
	}
	
	/**
	 * 
	 * <ul>前提premise : 如果当前queue中存在数据.
	 * <li>1. prefetchCount=0 无论autoAck=true/false
	 * 的情况下,无论当前有多少个线程(消费者)来取--都是后续创建的Consumer! 
	 * 先执行testSend后执行testReceive 这样的话
	 * 只会有一个线程(消费者)接收到服务器的消息.
	 *  对于后续消息(后于消费者的)就会以Roud-bin的形式来分发!
	 * </li>
	 * <li>2. prefetchCount = 1,autoAck=true同上</li>
	 * <li>3. prefetchCount = 1,autoAck=false--prefetch起作用!</li>
	 * </ul>
	 * 
	 * @throws InterruptedException
	 * @throws BrokenBarrierException
	 */
	@Test
	public void testReceiveWithMessageAlreadyExists() throws InterruptedException, BrokenBarrierException {
		choose(0);
		
		testSend();
		
		CountDownLatch latch = new CountDownLatch(10);
		Thread t1 = new Thread( new BoundWorker(latch));
		Thread t2 = new Thread( new BoundWorker(latch));
		
		t1.start();		t2.start();
		
		Thread.sleep(1000);
		
		long start = System.currentTimeMillis();
		latch.await();
		long end = System.currentTimeMillis();
		
		System.out.format("FINISHED RECEIVE TEST,Time used : %d's \n",(end - start)/1000);
	}
	
	@Test
	public void testReceiveWithNoneMessageLeft() throws InterruptedException, BrokenBarrierException {
		CountDownLatch latch = new CountDownLatch(10);
		Thread t1 = new Thread( new BoundWorker(latch));
		Thread t2 = new Thread( new BoundWorker(latch));
		
		t1.start();		t2.start();
		
		Thread.sleep(1000);
		testSend();
		
		long start = System.currentTimeMillis();
		latch.await();
		long end = System.currentTimeMillis();
		
		System.out.format("FINISHED RECEIVE TEST,Time used : %d's \n",(end - start)/1000);
		//prefetch 根据字面意思和现实的结果来看 
		// -- 如果当前client中任务已经完成,就会跟server取任务
		// 不由server来分配从而改变了原来默认的Round-robin的分发形式
		
		//perfetchCount = 1 => 17's -- 估计1是个最好的选择
		//perfetchCount = 0 => 25's 0-unlimited似乎跟默认的Round-robin一样(时间会长一点)
	}
	
	///////////////////////////////////////////////////////////////////////////////////////

	class BoundWorker implements Runnable {
		private final CountDownLatch latch;

		public BoundWorker(CountDownLatch latch) {
			this.latch = latch;
		}
		
		public void run() {
			Connection connection = null;
			Channel channel = null;
			try {
				String threadName = Thread.currentThread().getName();
				
				connection = createConnection();
				channel = connection.createChannel();
				
				//set Quality of Service
				channel.basicQos(prefetchCount); 
				//channel.basicQos(prefetchSize, prefetchCount, global);?? 
				
				channel.queueDeclare(QUEUE_NAME, false, false, false, null);

				System.out
						.println(threadName + " [*] Waiting for messages. To exit press CTRL+C");
				
				QueueingConsumer consumer = new QueueingConsumer(channel);
				channel.basicConsume(QUEUE_NAME, autoAck , consumer);
				
				while (true) {
					QueueingConsumer.Delivery delivery = consumer
							.nextDelivery();

					String message = new String(delivery.getBody());
					doWork(message);

					System.out.format("%s [x] Received ' %s  , Scuessed \n",
							threadName, message);
					latch.countDown();

					if (!autoAck) {
						channel.basicAck(delivery.getEnvelope()
								.getDeliveryTag(), false);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try {
					channel.close();
					connection.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
