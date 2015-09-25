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
		//rabbitmq��Round-robin����
		//1.ע�������ִ�з�ʽ�� ��ִ��testReceive,Ȼ��һ��һ����ִ��testSend����!
		
		//2.!! �����ת����,��ִ��testSend�ö��д��ڶ����Ϣ,Ȼ����ִ��testReceive�����Ļ� .
		// Rabbitmq�Ὣ���������е���Ϣ��dispath������һ��client��.. 
		// Rabbitmq����ϢRound-robin�ַ��Ƕ��ڵ���һ����Ϣ���� �ͻ���Round-robin�ķ�ʽ������task��Ҫ���ַ�����client��
		
		//����1��ִ�з�ʽ(���û�����ûẬ�д���ı�־�Ļ�  -failOnce=true)���Կ���
		//0,2,4,6,8 - thread-0������thread-1��client��
		//1,3,5,7,9 - thread-1������thread-0��client��
		//���ַ��䲻�Ǽ�ʱ,����Ϣ������ʱ�����������Ҫ�ַ���client��.
		//������Ϊ�����ִ��ʱ��Ĳ�ͬ--ack��ʱ�䲻ͬ,��������ķַ�˳����û�иı�..
		//����.��һ��client����һ��ʼ��ʧ�ܵĻ�e.g 0,��ô����һ��client�����յ�1,3,5,7,9Ȼ�����յ�2,4,6,8�� 
		
		//�������ַ�ʽ���Ա��޸�.. ��RabbitmqTest4
		
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
				//?������һ��consumer���Ѷ�����е���Ϣ��?���֤��Ф��룡
				
				Random random = new Random();
				while (true) {
					QueueingConsumer.Delivery delivery = consumer.nextDelivery();
					
					String message = new String(delivery.getBody());
					doWork(message);
					
					if(random.nextInt(10) % 2 ==0 && !failOnce){//�����Ĳ���һ�δ���
						System.out.format("%s [x] Received ' %s  , Failed \n",threadName,message);
						failOnce = true;
						return;//����--�Ͽ����� ��server֪�� ��ǰmessage�Ľ�����Ч ���ɱ��consumerִ��
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
