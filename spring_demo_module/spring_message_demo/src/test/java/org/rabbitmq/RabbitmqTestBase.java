package org.rabbitmq;

import java.io.IOException;

import org.junit.Test;

import com.rabbitmq.client.AMQP.Queue.DeleteOk;
import com.rabbitmq.client.AMQP.Queue.PurgeOk;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public abstract class RabbitmqTestBase {

	public final static String HOST = ConnectionFactory.DEFAULT_HOST.intern();
	public final static int PORT = ConnectionFactory.DEFAULT_AMQP_PORT; // 5672
	
	public final static String DEFAULT_EXCHANGE = "".intern();
	
	private final ExceptionHandler defaultExHandler = new ExceptionHandler() {
		@Override
		public void handle(Throwable t) {
			t.printStackTrace();
		}
	};
	
	protected Connection createConnection() throws IOException {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(HOST);
		// factory.setPort(PORT);
		return factory.newConnection();
	}
	
	public void doProcess(Process process){
		doProcess(process, defaultExHandler);
	}
	
	public void doProcess(Process process,ExceptionHandler exHandler){
		Connection connection = null;
		Channel channel = null;
		try {
			connection = createConnection();
			channel = connection.createChannel();
			
			process.doProcess(connection, channel);
		} catch (IOException e) {
			exHandler.handle(e);
		}finally{
			try {
				channel.close();
				connection.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void purgeQueues(final String...queues) {
		doProcess(new Process() {
			@Override
			public void doProcess(Connection connection, Channel channel)
					throws IOException {
				
				for( String queueName : queues){
					PurgeOk purge = channel.queuePurge(queueName);
					
					int backlog = purge.getMessageCount();
					System.out.format("Purge queue %s , backlog : %d \n",queueName, backlog);
				}
			}
		});
	}
	
	public void deleteQueues(final String... queues) {
		doProcess(new Process() {
			@Override
			public void doProcess(Connection connection, Channel channel)
					throws IOException {
				for( String queueName : queues){
					DeleteOk delete = channel.queueDelete(queueName);
					
					int backlog = delete.getMessageCount();
					System.out.format("Delete queue %s , backlog : %d \n",queueName, backlog);
				}
			}
		});
	}
	
	/**
	 * Send message to specify queue with the server run in localhost
	 * 
	 * @param queueName
	 * @param message
	 */
	public void sendDirectMessage(final String queueName,final String message){
		doProcess(new Process() {
			@Override
			public void doProcess(Connection connection, Channel channel)
					throws IOException {
				//make sure the queue exist
				channel.queueDeclare(queueName, false, false, false, null);
				channel.basicPublish(DEFAULT_EXCHANGE, queueName, null, message.getBytes());
			}
		});
	}
	@Test
	public void testSendDirectMessage(){
		sendDirectMessage("flower", "wosio_reina");
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////
	
	protected String getMessage(String... strings) {
		if (strings.length < 1)
			return "Hello World!";
		return joinStrings(strings, " ");
	}


	protected String joinStrings(String[] strings, String delimiter) {
		int length = strings.length;
		if (length == 0)
			return "";
		StringBuilder words = new StringBuilder(strings[0]);
		for (int i = 1; i < length; i++) {
			words.append(delimiter).append(strings[i]);
		}
		return words.toString();
	}

	/**
	 * Our fake task to simulate execution time:
	 * 
	 * @param task
	 * @throws InterruptedException
	 */

	protected void doWork(String task) throws InterruptedException {
		for (char ch : task.toCharArray()) {
			if (ch == '.')
				Thread.sleep(1000);
		}
	}
	
	
	public static interface Process{
		void doProcess(Connection connection,Channel channel) throws IOException;
	}
	
	public static interface ExceptionHandler{
		void handle(Throwable t);
	}
}

