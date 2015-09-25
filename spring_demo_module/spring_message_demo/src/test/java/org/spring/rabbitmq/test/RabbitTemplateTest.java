package org.spring.rabbitmq.test;

import java.util.Arrays;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { RabbitTemplateTest.AppContext.class })
public class RabbitTemplateTest {

	private final String simpleQueueName = "myQueue";
	
	private final String fanoutQueueName = "fanoutQueue";
	private final String fanoutExchangeName = "fanoutExchange";
	
	private final String directQueueName = "direct";
	private final String directExchangeName = "directExchange";
	
	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	@Autowired
	private RabbitAdmin rabbitAdmin;
	
	@Before
	public void setUp(){
	}
	
	@After
	public void tearDown(){
	}
	
	@Test
	public void testDoSendAndReceive() {
		try {
			rabbitAdmin.declareQueue(new Queue(simpleQueueName));
			
			String message = "Hi AMQP";
			
			String routingKey = simpleQueueName;
			rabbitTemplate.convertAndSend(routingKey, message);

			String receive = (String) rabbitTemplate.receiveAndConvert(simpleQueueName);

			Assert.assertEquals(message, receive);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testFanoutExchange(){
		String message = "Hi Fanout";
		
		Queue fanoutQueue = new Queue(fanoutQueueName);
		rabbitAdmin.declareQueue(fanoutQueue);
		
		FanoutExchange fanoutExchange = new FanoutExchange(fanoutExchangeName);
		rabbitAdmin.declareExchange(fanoutExchange);
		
		Binding binding = BindingBuilder.bind(fanoutQueue).to(fanoutExchange);
		rabbitAdmin.declareBinding(binding);
		
		rabbitTemplate.setExchange(fanoutExchange.getName());
		//send to exchange the former set , and routeKey using the default routing key("")
		rabbitTemplate.convertAndSend(message);
		
		String receive = (String) rabbitTemplate.receiveAndConvert(fanoutQueueName);
		Assert.assertEquals(message, receive);
	}
	
	
	@Test
	public void testDirectExchange(){
		
		Queue directQueue = new Queue(directQueueName);
		rabbitAdmin.declareQueue(directQueue);
		
		DirectExchange directExchange = new DirectExchange(directExchangeName);
		rabbitAdmin.declareExchange(directExchange);
		
		//multi bindings
		Binding binding = BindingBuilder.bind(directQueue).to(directExchange).with("rk1");
		Binding binding2 = BindingBuilder.bind(directQueue).to(directExchange).with("rk2");
		
		rabbitAdmin.declareBinding(binding);
		rabbitAdmin.declareBinding(binding2);
		
		
		String msg1 = "Hi direct:rk1",msg2 = "Hi direct:rk2";
		
		rabbitTemplate.convertAndSend(directExchangeName, "rk1", msg1);
		rabbitTemplate.convertAndSend(directExchangeName, "rk2", msg2);
		
		/////////////////////////////////////////////////////////////
		
		String recv1 = (String) rabbitTemplate.receiveAndConvert(directQueueName);
		String recv2 = (String) rabbitTemplate.receiveAndConvert(directQueueName);
		
		Assert.assertThat(Arrays.asList(msg1, msg2),Matchers.hasItems(recv1,recv2));
	}
	
	
	@Test
	public void sholdDeleteQueuesAndExchangesSuccess(){
		rabbitAdmin.deleteQueue(fanoutQueueName);
		rabbitAdmin.deleteQueue(simpleQueueName);
		rabbitAdmin.deleteQueue("spring-boot");
		
		rabbitAdmin.deleteExchange("spring-boot-exchange");
		rabbitAdmin.deleteExchange(fanoutExchangeName);
	}
	
	@Test
	@Ignore
	public void testAMQPWithTransaction(){
		rabbitTemplate.setChannelTransacted(true);
		
		String routingKey = simpleQueueName;
		rabbitTemplate.convertAndSend(routingKey, "msg1");
		rabbitTemplate.convertAndSend(routingKey, "msg2");
		
		//...
		// do something  which can throw amqp exception and check the message (Is send operation rollback)!  
	}

	@Test
	public void testDummy(){
		
	}
	
	@ComponentScan
	@Configuration
	public static class AppContext {
		/*@Bean
		com.rabbitmq.client.ConnectionFactory connectionFactory(){
			return new com.rabbitmq.client.ConnectionFactory();
		}*/
		
		@Bean
		PropertyPlaceholderConfigurer configurer(){
			PropertyPlaceholderConfigurer configurer = new PropertyPlaceholderConfigurer();
			configurer.setFileEncoding("utf-8");
			configurer.setIgnoreUnresolvablePlaceholders(true);
			configurer.setSystemPropertiesModeName("SYSTEM_PROPERTIES_MODE_OVERRIDE");
			
			Resource location = new ClassPathResource("rabbitmq.properties",getClass());
			configurer.setLocation(location);
			return configurer;
		}
		
		
		@Bean
		CachingConnectionFactory cachingConnectionFactory(@Value("${rabbit.hostname}") String hostname,@Value("${rabbit.port}") int port){
			return new CachingConnectionFactory(hostname, port);
		}
		
		@Bean
		RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory){//CachingConnectionFactory
			return new RabbitTemplate(connectionFactory);
		}
		
		@Bean
		RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory){//CachingConnectionFactory
			return new RabbitAdmin(connectionFactory);
		}
	}
}
