package org.activemq;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.concurrent.CountDownLatch;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.TopicConnection;
import javax.jms.TopicSession;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public abstract class ActiveMQTestBase {
	
	protected ActiveMQConnectionFactory getDefaultConnectionFactory(){
		
		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
				ActiveMQConnection.DEFAULT_USER,
				ActiveMQConnection.DEFAULT_PASSWORD, 
				ActiveMQConnection.DEFAULT_BROKER_URL);
		
		return  connectionFactory;
	}
	

	protected void doInTopicQuiet(Process process,boolean transacted) {
		ActiveMQConnectionFactory connectionFactory = getDefaultConnectionFactory();

		TopicConnection topicConnection = null;
		TopicSession session = null;
		try {
			topicConnection = connectionFactory.createTopicConnection();
			topicConnection.start();

			session = topicConnection.createTopicSession(transacted,
					Session.AUTO_ACKNOWLEDGE);

			process.doInTopicSession(session);

			if(transacted){
				session.commit();
			}
			session.close();
		} catch (JMSException e) {
			e.printStackTrace();
			if(transacted && session!=null){
				try {
					session.rollback();
				} catch (JMSException e1) {
					e1.printStackTrace();
				}
			}
		} finally {
			if(topicConnection != null){
				try {
					topicConnection.close();
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		}

	}

	static interface Process {
		void doInTopicSession(TopicSession session) throws JMSException;
	}
	

	static class CountDownTextMessageListener implements MessageListener {

		private final CountDownLatch latch;

		public CountDownTextMessageListener(CountDownLatch latch) {
			super();
			this.latch = latch;
		}

		@Override
		public void onMessage(Message message) {
			assertThat(message, is(instanceOf(TextMessage.class)));
			TextMessage msg = (TextMessage) message;
			try {
				String text = msg.getText();
				System.out.println(text);
			} catch (JMSException e) {
				e.printStackTrace();
			} finally {
				latch.countDown();
			}
		}

	}
}
