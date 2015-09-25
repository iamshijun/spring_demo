package org.rabbitmq.tutorial4;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.rabbitmq.RabbitmqTestBase;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.QueueingConsumer.Delivery;
import com.rabbitmq.client.ShutdownSignalException;

/**
 * Test bind with routeKey 
 * @author Administrator
 *
 */
public class RabbitmqTest5 extends RabbitmqTestBase {

	//1 对于使用queueDeclare声明的对立,会默认进行一个绑定
	//即调用了channel的exchangBind方法 其中三个参数值为 :
	//exchang_name("" 为默认的exchange) queue_name(指定的queue的名称) routeKey(和queue名称完全一致)
	
	//对于fanout类型的exchang进行exchangBind的时候通常都会忽略掉routeKey的,fanout类型的exchang只懂得广播..
	//广播到所有绑定在exchang上的queue上不理会routeKey?? test with tutorial3,或者是设置routekey为空字符串!?
	@Before
	public void setUp(){
		//super.purgeQueues(QUEUE_1_NAME,QUEUE_2_NAME,QUEUE_3_NAME);
	}
	
	@Test
	public void testDummy(){
		
	}
	
	private final String QUEUE_1_NAME     = "thread_color_intersted";
	private final String QUEUE_2_NAME     = "grey_interested";
	private final String QUEUE_3_NAME     = "white_interested";
	
	private final String ROUTE_KEY_BLACK  = "black";
	private final String ROUTE_KEY_GREY   = "grey";
	private final String ROUTE_KEY_WHITE  = "white";
	
	private final String EXCHANGE_COLOR   = "color_change";
	
	
	@Test
	public void shouldReceiveMessageFromTheRightRouteKey() throws InterruptedException{
		
		CountDownLatch latch = new CountDownLatch(5 + 3);
		
		new DaemonThread(new Worker("black_worker",QUEUE_1_NAME,latch)).start();
		new DaemonThread(new Worker("grey_worker",QUEUE_2_NAME,latch)).start();
		new DaemonThread(new Worker("while_worker",QUEUE_3_NAME,latch)).start();
		
		Thread.sleep(500);
		
		doProcess(new Process() {
			
			@Override
			public void doProcess(Connection connection, Channel channel)
					throws IOException {
				
				channel.queueDeclare(QUEUE_1_NAME, false, false, false, null);
				channel.queueDeclare(QUEUE_2_NAME, false, false, false, null);
				channel.queueDeclare(QUEUE_3_NAME, false, false, false, null);
				
				channel.basicPublish(EXCHANGE_COLOR, ROUTE_KEY_BLACK, null,
						"i am black".getBytes());
				channel.basicPublish(EXCHANGE_COLOR, ROUTE_KEY_GREY, null,
						"i am grey ".getBytes());
				channel.basicPublish(EXCHANGE_COLOR, ROUTE_KEY_WHITE, null,
						"i am white".getBytes());
				
				//publish using the default_exchange ""..
				channel.basicPublish(DEFAULT_EXCHANGE, QUEUE_1_NAME, null,
						String.format("i am message to queue : [%s] ", QUEUE_1_NAME).getBytes());
				channel.basicPublish(DEFAULT_EXCHANGE, QUEUE_2_NAME, null,
						String.format("i am message to queue : [%s] ", QUEUE_2_NAME).getBytes());
				channel.basicPublish(DEFAULT_EXCHANGE, QUEUE_3_NAME, null,
						String.format("i am message to queue : [%s] ", QUEUE_3_NAME).getBytes());
			}
		});
		
		latch.await();
		
		System.out.println("FINISHED");
	}
	
	class DaemonThread extends Thread{
		{
			setDaemon(true);
		}

		public DaemonThread() {
		}

		public DaemonThread(Runnable runnable) {
			super(runnable);
		}
	}
	
	class Worker implements Runnable {
		
		private final String workerName;
		private final String queue;
		private CountDownLatch latch;

		public Worker(String workerName, String queueName,CountDownLatch latch) {
			Assert.assertNotNull("queueName cannot given null", queueName);
			Assert.assertNotNull("workerName cannot given null", workerName);

			this.workerName = workerName;
			this.queue = queueName;
			this.latch = latch;
		}

		@Override
		public void run() {
			Connection connection = null;
			Channel channel = null;
			
			QueueingConsumer consumer = null;
			String consumerTag = null;
			try {
				connection = createConnection();
				channel = connection.createChannel();

				consumer = new QueueingConsumer(channel);
				consumerTag = channel.basicConsume(queue, true, consumer);

				while (true && !Thread.interrupted()) {
					Delivery delivery = consumer.nextDelivery();
					String message = new String(delivery.getBody());

					System.out.format(
							"[*] consumer : %s received message : %s\n",
							workerName, message);
					
					latch.countDown();
					// if it is not auotAck! do this!
					// channel.basicAck(delivery.getEnvelope().getDeliveryTag(),false);
				}
			} catch (ShutdownSignalException e) {
				e.printStackTrace();
				consumer.handleShutdownSignal(consumerTag, e);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				System.out.println("Return resources");
				try {
					channel.close();
					connection.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	
	
	@Test  //initialize the queues and bindings
	@Ignore  
	public void testQueueBinding(){
		//do send
		
		doProcess(new Process() {
			
			private void declareQueue(Channel channel,String queueName) throws IOException{//default properties
				channel.queueDeclare(queueName, false, false, false, null);
			}
			@Override
			public void doProcess(Connection connection, Channel channel)
					throws IOException {
				
				deleteQueues(QUEUE_1_NAME,QUEUE_2_NAME,QUEUE_3_NAME);
				
				channel.exchangeDeclare(EXCHANGE_COLOR, "direct");
				
				declareQueue(channel,QUEUE_1_NAME);
				declareQueue(channel,QUEUE_2_NAME);
				declareQueue(channel,QUEUE_3_NAME);
				
				//queue_bind不能绑定到DEFAULT_EXCHANGE("")上
				// 不是exchangBind哦 是queueBind ,? what exchangeBind for 
				channel.queueBind(QUEUE_1_NAME, EXCHANGE_COLOR, ROUTE_KEY_BLACK);
				channel.queueBind(QUEUE_1_NAME, EXCHANGE_COLOR, ROUTE_KEY_GREY);
				channel.queueBind(QUEUE_1_NAME, EXCHANGE_COLOR, ROUTE_KEY_WHITE);

				channel.queueBind(QUEUE_2_NAME, EXCHANGE_COLOR, ROUTE_KEY_GREY);

				channel.queueBind(QUEUE_3_NAME, EXCHANGE_COLOR, ROUTE_KEY_WHITE);
				/*
				 * list_bindings ..we can see the screen :
				 * 
				 * Listing bindings ...
					                exchange        grey_interested         queue   grey_interested         []
					                exchange        task_queue              queue   task_queue              []
					                exchange        thread_color_intersted  queue   thread_color_intersted  []
					                exchange        white_interested        queue   white_interested        []
					                
					color_change    exchange        grey_interested         queue   grey    []
					color_change    exchange        thread_color_intersted  queue   black                   []
					color_change    exchange        thread_color_intersted  queue   grey                    []
					color_change    exchange        thread_color_intersted  queue   white                   []
					color_change    exchange        white_interested        queue   white                   []
					
				(   exchange_name                   queue_name                      route_key               ?? )  
				 *
				 *   1. publisher 只和exchane打交道,有必要的话 就加上一个作为标识的routeKey,所以根本不需要知道一个queue的名称
				 *   ..(顶多只使用默认的exchange的时候 routeKey和queueName相等这个时候才需要知道queueName)
				 *   
				 *   2. consumer  只和queue打交道,虽然说这几个例子下来都在consumer的过程中 来绑定queue,exchange,routeKey的,但是不然
				 *   ..consumer消费只管从自己的queue(可以是多个try!)中得到消息    
				 */
				
			}
		},new ExceptionHandler() {//exception callback
			public void handle(Throwable t) {
				
				t.printStackTrace();
				
				deleteQueues(QUEUE_1_NAME,QUEUE_2_NAME,QUEUE_3_NAME);
			}
		});
		
	}
}
