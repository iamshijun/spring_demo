package org.rabbitmq;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownSignalException;

public class RabbitmqTest7 extends RabbitmqTestBase {
	
	final String queueName = "distinct-consumer-1-channel";
	
	@Before
	public void setUp(){
		super.purgeQueues(queueName);
	}
	
	@Test
	public void testMultiConsumersWithOneChannel(){//channel thread-safty test
		
		final int messageCount = 100;
		final CountDownLatch countDownLatch = new CountDownLatch(messageCount);
		
		
		final ExecutorService executor = Executors.newFixedThreadPool(5,new ThreadFactory() {
			@Override
			public Thread newThread(Runnable r) {
				Thread t = new Thread(r);
				t.setDaemon(true);
				return t;
			}
		});
		
		class Task{
			Consumer consumer;
			Channel channel;
			Envelope envelope;
			BasicProperties properties;
			byte[] body;
			public Task(Consumer consumer,Channel channel,Envelope envelope,
					BasicProperties properties, byte[] body) {
				super();
				this.consumer = consumer;
				this.channel = channel;
				this.envelope = envelope;
				this.properties = properties;
				this.body = body;
			}
			
		}
		
		try {
			final Connection connection =  createConnection();
			final Channel channel = connection.createChannel();
			
			final BlockingQueue<Task> tasks = new LinkedBlockingQueue<Task>();
			
			boolean autoAck = false;
			int consumerCount = 4;
			while(consumerCount-- > 0){
				executor.execute(new Runnable(){
					@Override
					public void run() {
						while(true && !Thread.interrupted()){
							try {
								Task task= tasks.take();
								Consumer consumer = task.consumer;
								consumer.handleDelivery(Thread.currentThread().getName(),
															task.envelope,
															task.properties,
															task.body);
								
								task.channel.basicAck(task.envelope.getDeliveryTag(), false);
								
								countDownLatch.countDown();
							} catch (InterruptedException e) {
								Thread.currentThread().interrupt();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				});
			}
			
			doProcess(new Process() {
				@Override
				public void doProcess(Connection connection, Channel channel)
						throws IOException {
					
					channel.queueDeclare(queueName, false, false, false, null);
					
					for(int i = 0 ; i < messageCount;++i){
						channel.basicPublish("", queueName, null, ("message-" + i).getBytes());
					}
				}
			});
			
			while(true){
				if (countDownLatch.getCount() <= 0)
					break;
				channel.basicConsume(queueName, autoAck,
						new DefaultConsumer(channel) {//queue
							@Override
							public void handleDelivery(String consumerTag,
														Envelope envelope,
														BasicProperties properties,
														byte[] body) throws IOException {
			
								tasks.add(new Task(new DefaultConsumer(channel){
										@Override
										public void handleDelivery(String consumerTag,
																	Envelope envelope,
																	BasicProperties properties,
																	byte[] body) throws IOException {
											
											System.out.format("[%s] : consumer %s \n",Thread.currentThread().getName(),new String(body));
											
										}
								}, channel,envelope, properties, body));
							}
				});
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		executor.shutdown();
		try {
			executor.awaitTermination(3, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			//ignore
		}
	}
	
	
	@Test //check the thread name in consumer's handleDelivery method
	public void testConsumerBoundToMultiQueue(){
		final String queue1 = "q1", queue2 = "q2", queue3 = "q3";
		
		final CountDownLatch latch = new CountDownLatch(3);
		
		doProcess(new Process() {
			
			@Override
			public void doProcess(Connection connection, Channel channel)
					throws IOException {
				channel.queueDeclare(queue1,false,false,false,null);
				channel.queueDeclare(queue2,false,false,false,null);
				channel.queueDeclare(queue3,false,false,false,null);
				
				channel.basicPublish("", queue1, null, "i'm from queue1".getBytes());
				channel.basicPublish("", queue2, null, "i'm from queue2".getBytes());
				channel.basicPublish("", queue3, null, "i'm from queue3".getBytes());
			}
		});
		
		//receive
		try {
			Connection connection = createConnection();
			Channel channel = connection.createChannel();
			
			Consumer consumer = new DefaultConsumer(channel){
				@Override
				public void handleDelivery(String consumerTag,
										Envelope envelope,
										BasicProperties properties,
										byte[] body) throws IOException {
					
					System.out.format("[%s] : consumer %s \n",Thread.currentThread().getName(),new String(body));
					
					latch.countDown();
				}
				
				@Override
				public void handleShutdownSignal(String consumerTag,
						ShutdownSignalException sig) {
					super.handleShutdownSignal(consumerTag, sig);
				}
			};
			channel.basicConsume(queue1, true, consumer);
			channel.basicConsume(queue2, true, consumer);
			channel.basicConsume(queue3, true, consumer);
			
			latch.await();
			
			channel.close();
			connection.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			//ignore
		}
		
	}
	
}
