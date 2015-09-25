package org.rabbitmq.tutorial2;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;

import org.junit.Test;
import org.rabbitmq.RabbitmqTestBase;

import com.rabbitmq.client.Address;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

public class RabbitmqTest2 extends RabbitmqTestBase {

	public final static String QUEUE_NAME = "hello";
	
	private final int ITERATION = 10;
	
	private final CountDownLatch latch = new CountDownLatch(10);
	
	private String dots[] = {".","..","...","....","....."};
	
	@Test
	public void testDummy(){
		
	}
	
	@Test
	public void purgeQueue(){
		super.purgeQueues(QUEUE_NAME);
	}
	
	public Connection createConnection() throws IOException {
		ConnectionFactory factory = new ConnectionFactory();
		//using Address
		return factory.newConnection(new Address[] { 
				new Address(HOST, PORT), 
				new Address("127.0.0.1", 5379) //if the first connection fail use this! 
		});
	}
	
	@Test
	public void testSend() {
		try {
//			String[] messageToSend = {"1.","2..","3..","4....","5....."};
			Connection connection  = createConnection();
			
			Channel channel = connection.createChannel();
			channel.queueDeclare(QUEUE_NAME, false, false, false, null);
			
			for(int i = 0; i < ITERATION ;++i){
				String msg = i + dots[i % dots.length];
				String message = getMessage(msg);
				//publish -> ""(default exchange) -> send with routeKey QUEUE_NAME
				channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
				
				System.out.println(" [x] Sent '" + message + "'");
			}

			channel.close();
			connection.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	@Test
	public void testReceive() throws InterruptedException, BrokenBarrierException {
		Worker worker1 = new Worker();
		Worker worker2 = new Worker();
		
		Thread t1 = new Thread(worker1);
		Thread t2 = new Thread(worker2);
		//rabbitmq的Round-robin机制
		//1.注意这里的执行方式是 先执行testReceive,然后一个一个的执行testSend方法!
		
		//2.!! 如果反转过来,先执行testSend让队列存在多个消息,然后再执行testReceive方法的话 .
		// Rabbitmq会将队列中所有的消息都dispath到其中一个client上.. 
		// Rabbitmq的消息Round-robin分发是对于但有一条消息进来 就会以Round-robin的方式来分配task将要被分发到的client端
		
		//按照1的执行方式(如果没有设置会含有错误的标志的话  -failOnce=true)可以看到
		//0,2,4,6,8 - thread-0或者是thread-1的client端
		//1,3,5,7,9 - thread-1或者是thread-0的client端
		//这种分配不是即时,但消息进来的时候就立即分配要分发的client端.
		//首先因为任务的执行时间的不同--ack的时间不同,但是上面的分发顺序还是没有改变..
		//另外.当一个client端在一开始就失败的话e.g 0,那么另外一个client对先收到1,3,5,7,9然后再收到2,4,6,8的 
		
		//不过这种方式可以被修改.. 见RabbitmqTest4
		
		t1.start();		t2.start();
		
		latch.await();
		System.out.println("FINISHED RECEIVE TEST");
	}
	
	
	private volatile boolean failOnce = false;
	private boolean autoAck = false;//autoAcKnowledge
	
	class Worker implements Runnable {
		public void run() {
			Connection connection = null;
			Channel channel = null;
			try {
				
				String threadName = Thread.currentThread().getName();
				
				connection = createConnection();
				channel = connection.createChannel();
				
				channel.queueDeclare(QUEUE_NAME, false, false, false, null);

				System.out
						.println(threadName + " [*] Waiting for messages. To exit press CTRL+C");
				
				QueueingConsumer consumer = new QueueingConsumer(channel);
				
				//channel.exchangeBind(QUEUE_NAME,"", "black");
				channel.basicConsume(QUEUE_NAME, autoAck , consumer);
				//?可以用一个consumer消费多个队列的消息吗?たぶん　行ける！
				
				Random random = new Random();
				while (true) {
					QueueingConsumer.Delivery delivery = consumer.nextDelivery();
					
					String message = new String(delivery.getBody());
					doWork(message);
					
					if(random.nextInt(10) % 2 ==0 && !failOnce){//单纯的产生一次错误
						System.out.format("%s [x] Received ' %s  , Failed \n",threadName,message);
						failOnce = true;
						return;//返回--断开连接 让server知道 当前message的接受无效 交由别的consumer执行
					}else{
						System.out.format("%s [x] Received ' %s  , Scuessed \n",threadName,message);
						latch.countDown();
					}

					if (!autoAck){
						channel.basicAck(
								delivery.getEnvelope().getDeliveryTag(),
								false);
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
