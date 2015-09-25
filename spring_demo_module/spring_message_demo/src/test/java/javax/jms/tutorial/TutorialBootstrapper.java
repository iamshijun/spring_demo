package javax.jms.tutorial;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Session;
import javax.naming.NamingException;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;

public class TutorialBootstrapper {

	private static SimpleNamingContextBuilder builder; // Spring JNDI mock class

	public static void doBind(String... queues) {
		if(builder == null){
			synchronized (TutorialBootstrapper.class) {
				if(builder != null) return;
				try {
					builder = SimpleNamingContextBuilder.emptyActivatedContextBuilder();
					// SimpleNamingContextBuilder 为我们简化了在JNDI绑定的过程
		
					ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
							ActiveMQConnection.DEFAULT_BROKER_URL);
					ActiveMQConnectionFactory queueConnectionFactory = connectionFactory;
					ActiveMQConnectionFactory topicConnectionFactory = connectionFactory;
		
					builder.bind(SampleUtilities.QUEUECONFAC, queueConnectionFactory);
					builder.bind(SampleUtilities.TOPICCONFAC, topicConnectionFactory);
					
					Connection connection = connectionFactory.createConnection();
					Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
					if(queues != null && queues.length!=0){
						for(String queue : queues){
							builder.bind("myQueue", session.createQueue(queue));
						}
					}
					session.close();
					connection.close();
				} catch (NamingException e) {
					e.printStackTrace();
				} catch (JMSException e) {
					unbind();
					e.printStackTrace();
				}finally{
				}
			}
		}
	}

	public static void unbind() {
		if (builder != null) {
			builder.deactivate();
			builder.clear();
		}
	}

	
	public static void main(String[] args) throws NamingException {
		String queue = "myQueue";
		doBind(queue);
		try {
			SampleUtilities.sendSynchronizeMessage("[Testing...]", queue);
		} catch (Exception e) {
			e.printStackTrace();
		}
//		SampleUtilities.jndiLookup(SampleUtilities.QUEUECONFAC);
		unbind();
	}
}

