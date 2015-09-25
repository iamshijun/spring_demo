package org.activemq;

import java.util.concurrent.CountDownLatch;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;

import org.apache.activemq.ActiveMQConnectionFactory;

public class Subscriber implements Runnable{
		
		private final CountDownLatch latch;
		private final ActiveMQConnectionFactory connectionFactory;
		private final String topicName;
		
		public Subscriber(ActiveMQConnectionFactory connectionFactory,String topicName,int messageReceived){
			
//			assertThat(messageReceived, Matchers.greaterThan(0));
//			assertThat(connectionFactory,notNullValue());
//			assertThat(topicName,notNullValue());
			
			this.connectionFactory = connectionFactory;
			this.latch = new CountDownLatch(messageReceived);
			this.topicName = topicName;
			 
		}
		
		public void run(){
			TopicConnection topicConnection = null;
			try {
				topicConnection = connectionFactory.createTopicConnection();
				topicConnection.start();
				
				TopicSession session = topicConnection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
				Topic topic = session.createTopic(topicName);

				TopicSubscriber topicSubscriber = session.createSubscriber(topic);
				topicSubscriber.setMessageListener(new MessageListener() {
					@Override
					public void onMessage(Message message) {
						// assertThat(message,is(instanceOf(TextMessage.class)));
						if (message instanceof TextMessage) {
							TextMessage msg = (TextMessage) message;
							try {
								String text = msg.getText();
								System.out.println(text);
							} catch (JMSException e) {
								e.printStackTrace();
							} finally {
							}
						}
						latch.countDown();
					}
				});
				
				latch.await();
				
				topicSubscriber.close();
				session.close();
				
				//consumer.receive();
			} catch (JMSException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				//e.printStackTrace(); //ignore
			}finally{
				try {
					topicConnection.close();
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		}
	}