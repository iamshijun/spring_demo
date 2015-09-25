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
 * IMP �˽�Rabbit����Ϣ�ַ�
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
	 * <ul>ǰ��premise : �����ǰqueue�д�������.
	 * <li>1. prefetchCount=0 ����autoAck=true/false
	 * �������,���۵�ǰ�ж��ٸ��߳�(������)��ȡ--���Ǻ���������Consumer! 
	 * ��ִ��testSend��ִ��testReceive �����Ļ�
	 * ֻ����һ���߳�(������)���յ�����������Ϣ.
	 *  ���ں�����Ϣ(���������ߵ�)�ͻ���Roud-bin����ʽ���ַ�!
	 * </li>
	 * <li>2. prefetchCount = 1,autoAck=trueͬ��</li>
	 * <li>3. prefetchCount = 1,autoAck=false--prefetch������!</li>
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
		//prefetch ����������˼����ʵ�Ľ������ 
		// -- �����ǰclient�������Ѿ����,�ͻ��serverȡ����
		// ����server������Ӷ��ı���ԭ��Ĭ�ϵ�Round-robin�ķַ���ʽ
		
		//perfetchCount = 1 => 17's -- ����1�Ǹ���õ�ѡ��
		//perfetchCount = 0 => 25's 0-unlimited�ƺ���Ĭ�ϵ�Round-robinһ��(ʱ��᳤һ��)
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
