package org.activemq;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Test;

public class ActiveMQTest extends ActiveMQTestBase {

	//totally use JMS api
//	private final String brokerURL = ActiveMQConnection.DEFAULT_BROKER_URL;
	private final String topicName = "joke";
	
	
	
	
	
	
	///////////////////////////////////////////////////////////////////////////////////////////////////
	@Test
	public void testSendMesageToTopic() throws JMSException{
		boolean transacted = true;
		doInTopicQuiet(new Process() {
			@Override
			public void doInTopicSession(TopicSession session) throws JMSException {
				Topic topic = session.createTopic(topicName);
				
				TopicPublisher publisher = session.createPublisher(topic);
				TextMessage message = session.createTextMessage("Hello world");
				
				publisher.publish(topic, message);
				
				publisher.close();
			}
		}, transacted);
	}
	
	@Test
	public void testReceiveMessageOnceFromTopic(){ //run first and run testSend
		final CountDownLatch latch = new CountDownLatch(1);
		boolean transacted = false;
		
		doInTopicQuiet(new Process() {
			@Override
			public void doInTopicSession(TopicSession session) throws JMSException {
				Topic topic = session.createTopic(topicName);
				TopicSubscriber topicSubscriber = session.createSubscriber(topic);
				topicSubscriber.setMessageListener(new CountDownTextMessageListener(latch));
				
				try {
					latch.await();
				} catch (InterruptedException e) {
					return;
				}
			}
		},transacted );

	}
	
	@Test
	public void testDurableSubscriber(){
		ActiveMQConnectionFactory connectionFactory = getDefaultConnectionFactory();
		
		TopicConnection connection = null;
		TopicSession session = null;
		try {
			connection = connectionFactory.createTopicConnection();
			connection.start();
			
			session = connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
			Topic topic = session.createTopic(topicName);
			
			TopicSubscriber durableSubscriber = session
					.createDurableSubscriber(topic, "durable-joke-subscriber");
			
			connection.close();// !!!
			
			final AtomicInteger countDown = new AtomicInteger(5);
			final CountDownLatch latch = new CountDownLatch(1);
			durableSubscriber.setMessageListener(new MessageListener() {
				@Override
				public void onMessage(Message message) {
					if(countDown.getAndDecrement() == 0){
						latch.countDown();
					}
					if (message instanceof TextMessage) {
						TextMessage msg = (TextMessage) message;
						try {
							String text = msg.getText();
							System.out.println(text);
						} catch (JMSException e) {
							e.printStackTrace();
						}
					} else{
						latch.countDown();
					}
				}
			});
			//session.close();
			connection.start();
			
			latch.await();
			System.out.println("FINISHED");
		} catch (JMSException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			if(connection!=null){
				try {
					connection.close();
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
