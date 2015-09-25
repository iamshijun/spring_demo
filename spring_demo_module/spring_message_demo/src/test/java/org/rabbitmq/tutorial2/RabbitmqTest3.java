package org.rabbitmq.tutorial2;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.junit.Test;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;

/**
 * Persistence test
 * @author Administrator
 *
 */
public class RabbitmqTest3 {
	
	/**
	 * Document :
	 * Note on message persistence
	 * 
	 * Marking messages as persistent doesn't fully guarantee that a message
	 * won't be lost. Although it tells RabbitMQ to save the message to disk,
	 * there is still a short time window when RabbitMQ has accepted a message
	 * and hasn't saved it yet. Also, RabbitMQ doesn't do fsync(2) for every
	 * message -- it may be just saved to cache and not really written to the
	 * disk. The persistence guarantees aren't strong, but it's more than enough
	 * for our simple task queue. If you need a stronger guarantee you can wrap
	 * the publishing code in a transaction.
	 */
	
	public final static String QUEUE_NAME = "task_queue";
	public final static String HOST = ConnectionFactory.DEFAULT_HOST;
	public final static int PORT = ConnectionFactory.DEFAULT_AMQP_PORT; // 5672
	
	private final int messageCount = 1;
	private final CountDownLatch countDownLatch = new CountDownLatch(messageCount);
	
	private boolean durable = true;
	
	@Test
	public void testSend() {
		try {
			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost(HOST);
			
			Connection connection = factory.newConnection();
			Channel channel = connection.createChannel();
			
			channel.queueDeclare(QUEUE_NAME, durable, false, false, null);
			
			String message = "Hi i am a persistent task!";
			/*
			 * If we use durable queue , we should set the properties with the
			 * persistence infomation!
			 */
			AMQP.BasicProperties basicprops = durable ? MessageProperties.PERSISTENT_TEXT_PLAIN : null;
			
			channel.basicPublish("", 
					QUEUE_NAME,
					basicprops,
					message.getBytes());
			
			System.out.println(" [x] Sent '" + message + "'");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testReceive() {
		try {
			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost(HOST);
			factory.setPort(PORT);
			
			Connection connection = factory.newConnection();
			Channel channel = connection.createChannel();
			
			channel.queueDeclare(QUEUE_NAME, durable, false, false, null);

			System.out
					.println(" [*] Waiting for messages. To exit press CTRL+C");
			
			final QueueingConsumer consumer = new QueueingConsumer(channel);
			channel.basicConsume(QUEUE_NAME, true, consumer);
			new Thread() {
				{setDaemon(true);}
				public void run() {
					try {
						while (true) {
							QueueingConsumer.Delivery delivery;
							delivery = consumer.nextDelivery();//channel关闭的时候 这里会抛出ShutdownSignalException异常

							String message = new String(delivery.getBody());
							System.out.format(" [x] Received '%s' \n", message);

							countDownLatch.countDown();
						}
					}catch(ShutdownSignalException sig){
						consumer.handleShutdownSignal(null, sig); //我乱加的,what handleShutdownSignal for?
					} catch (Exception e) {
						e.printStackTrace();
					}
				};
			}.start();
			
			countDownLatch.await();
			
			channel.close();
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
