package org.rabbitmq.tutorial1;

import java.io.IOException;

import org.junit.Test;
import org.rabbitmq.RabbitmqTestBase;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;

public class RabbitmqTest1 extends RabbitmqTestBase {
	/*
	 * RabbitMQ ��AMQP(Advanced Message Queue Protocol)Э���ʵ��
	 */
	public final static String QUEUE_NAME = "hello";

	@Test
	public void testSend() {
		Connection connection = null;
		Channel channel = null;
		try {
			//1. ConnectionFactory
			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost(HOST);
			//factory.setPort(PORT);
			
			// 2.Connection using connectionFactory
			connection = factory.newConnection();

			// 3 Channel ?like JMS Session ?
			channel = connection.createChannel();

			/*4. Create queue if not exist and set the queue properties
			 * String queue/queue name, 
			 * boolean durable/message will be persist in server,
			 * boolean exclusive, 
			 * boolean autoDelete,
			 * Map<String, Object> arguments
			 */
			channel.queueDeclare(QUEUE_NAME, false, false, false, null);
			/*
			 * ���� �����������֮ǰ�Ѿ�������QUEUE_NAME���ƵĶ��еĻ�
			 * ��Ҫע���������queueDecalre����Ҫ�͵�һ�δ������е�ʱ��Ķ��е�����Ҫһ�²�Ȼ�Ļ��ᱨ���
			 */
			/*
			 * Declaring a queue is idempotent - it will only be created if it
			 * doesn't exist already. The message content is a byte array, so
			 * you can encode whatever you like there
			 */
			
			//5 Send message
			String message = "Hello World!";
			channel.basicPublish("", QUEUE_NAME, null/*routing header*/, message.getBytes());
			
			System.out.println(" [x] Sent '" + message + "'");
			
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				// 6 close resources ./
				channel.close();
				connection.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Test
	public void testReceive() {
		Connection connection = null;
		Channel channel = null;
		try {
			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost(HOST);
			factory.setPort(PORT);

			connection = factory.newConnection();

			channel = connection.createChannel();
			// queueDecalre������Ҫ��send��һ��(/��һ�δ�����ʱ�������һ��)
			channel.queueDeclare(QUEUE_NAME, false, false, false, null);

			System.out
					.println(" [*] Waiting for messages. To exit press CTRL+C");
			
			QueueingConsumer consumer = new QueueingConsumer(channel);
			channel.basicConsume(QUEUE_NAME, true, consumer);

			while (true) {
				/*
				 * Main application-side API: wait for the next message delivery and return it.
				 */
				QueueingConsumer.Delivery delivery = consumer.nextDelivery();
				String message = new String(delivery.getBody());
				System.out.println(" [x] Received '" + message + "'");
				
				//break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ShutdownSignalException e) {
			e.printStackTrace();
		} catch (ConsumerCancelledException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
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
