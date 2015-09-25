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

		//exchange�������ܹ���4������ʹ��fanout ʹ�ù㲥broadcast,ע�����ﲻ��topic(JMS����topic)
		channel.exchangeDeclare(EXCHANGE_NAME, "fanout"); //type : fanout
		
		//ʹ���޲�����queueDeclare�õ�һ��������Ƶ�Queue(getQueue���ض��е�����_)
		String queueName = channel.queueDeclare().getQueue();
		//Ȼ��Queue��exchange ���а�bind ����ʹ��rabbitmqctl list_bindings������
		//����tutorial2�е�hello,task_queueҲ�ǽ����˰󶨵�
		
		/*
		 * In previous parts of the tutorial we knew nothing about exchanges,
		 * but still were able to send messages to queues. That was possible
		 * because we were using a default exchange, which we identify by the
		 * empty string (""). 
		 * 
		 * ֮ǰ��basicPublish�и�����exchangeΪ""���ַ���,�õ�����һ��default_exchange
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
		���Կ������� ����Ĭ�ϵ�exchange��һ��direct���͵�exchange
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
		                         
		 ���Կ���֮ǰ��hello,queue��exchange�����ǿ��ַ���	,����Ҳ֪�������exchange��һ������Ϊdirect��exchange
		 �������fanout��exchange�����exchange log,�ڰ�exchange��queue��ͬʱ �Ὣ���queue�󶨵�Ĭ�ϵ�exchange��
		 �������߹ر����Ӻ� ����󶨻ᱻ�Զ�Ŷɾ��,��ʱ�����Ķ���Ҳ�ᱻɾ��!
		 
		 ����������1,2�Ǻ�֮ǰһ�� �����֪��queue������ ͬ������ֱ�ӽ���Ϣ�ŵ�����,����˵fanout��ʵ����������ʽ 
		 * 
		 */
		channel.queueBind(queueName, EXCHANGE_NAME, "");

		System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

		QueueingConsumer consumer = new QueueingConsumer(channel);
		//consumer����Ǻ�queue��������.����JMS,��Queue��Destination!
		channel.basicConsume(queueName, true, consumer);

		while (true) {
			QueueingConsumer.Delivery delivery = consumer.nextDelivery();
			String message = new String(delivery.getBody());

			System.out.println(" [x] Received '" + message + "'");
		}
	}
}