package org.rabbitmq.tutorial3;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

public class ReceiveLogs {

	private static final String EXCHANGE_NAME = "logs";

	public static void main(String[] argv) throws java.io.IOException,
			java.lang.InterruptedException {

		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();

		//exchange的类型总共有4中这里使用fanout 使用广播broadcast,注意这里不是topic(JMS才是topic)
		channel.exchangeDeclare(EXCHANGE_NAME, "fanout"); //type : fanout
		
		//使用无参数的queueDeclare得到一个随机名称的Queue(getQueue返回队列的名称_)
		String queueName = channel.queueDeclare().getQueue();
		//然后将Queue和exchange 进行绑定bind 可以使用rabbitmqctl list_bindings靠到绑定
		//另外tutorial2中的hello,task_queue也是进行了绑定的
		
		/*
		 * In previous parts of the tutorial we knew nothing about exchanges,
		 * but still were able to send messages to queues. That was possible
		 * because we were using a default exchange, which we identify by the
		 * empty string (""). 
		 * 
		 * 之前在basicPublish中更定的exchange为""空字符串,用到的是一个default_exchange
		 */
		
		/*
		 *
		 * 
		 Listing exchanges ...
		                        direct
		amq.direct              direct
		amq.fanout              fanout
		amq.headers             headers
		amq.match               headers
		amq.rabbitmq.log        topic
		amq.rabbitmq.trace      topic
		amq.topic               topic
		logs                    fanout
		...done.
		(exchange_name          exchange type)
		可以看到的是 这里默认的exchange是一个direct类型的exchange
		 * 
		 * 
		    Listing bindings ...
		1	        exchange        amq.gen-4qqZfLdYNYC9p9ZUES_c5g  queue   amq.gen-4qqZfLdYNYC9p9ZUES_c5g  []
		2	        exchange        amq.gen-HbEW898djJtx5_ARG_KcAg  queue   amq.gen-HbEW898djJtx5_ARG_KcAg  []
		3	        exchange        hello                           queue   hello                           []
		4	        exchange        task_queue                      queue   task_queue                      []
		5	logs    exchange        amq.gen-4qqZfLdYNYC9p9ZUES_c5g  queue                                   []
		6	logs    exchange        amq.gen-HbEW898djJtx5_ARG_KcAg  queue                                   []
			...done.
			
		  (exchange_name            queue_name                              route_key?      )   
		                         
		 可以看到之前的hello,queue的exchange名称是空字符串	,上面也知道了这个exchange是一个类型为direct的exchange
		 另外对于fanout的exchange这里的exchange log,在绑定exchange和queue的同时 会将这个queue绑定到默认的exchange中
		 当消费者关闭连接后 这个绑定会被自动哦删除,临时创建的队列也会被删除!
		 
		 另外多出来的1,2是和之前一样 如果你知道queue的名称 同样可以直接将消息放到上面,就是说fanout其实存在两种形式 
		 * 
		 */
		channel.queueBind(queueName, EXCHANGE_NAME, "");

		System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

		QueueingConsumer consumer = new QueueingConsumer(channel);
		//consumer最后还是和queue来交互的.不像JMS,有Queue和Destination!
		channel.basicConsume(queueName, true, consumer);

		while (true) {
			QueueingConsumer.Delivery delivery = consumer.nextDelivery();
			String message = new String(delivery.getBody());

			System.out.println(" [x] Received '" + message + "'");
		}
	}
}