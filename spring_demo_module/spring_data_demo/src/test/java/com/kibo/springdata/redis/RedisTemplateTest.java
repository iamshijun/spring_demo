package com.kibo.springdata.redis;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RedisTemplateTest.TestContext.class)
public class RedisTemplateTest {
	
	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	@Resource(name="redisTemplate")
	private ValueOperations<String, String> valueOps;
	@Resource(name="redisTemplate")
	private ListOperations/*<K, V>*/ listOps;
	@Resource(name="redisTemplate")
	private SetOperations/*<K, V>*/ setOps;
	@Resource(name="redisTemplate")
	private ZSetOperations/*<K, V>*/ zSetOps;
	
	@Autowired
	private	JedisShardInfo shardInfo;
	
	@Before
	public void setUp(){
		Assert.assertNotNull(redisTemplate);
		redisTemplate.execute(new RedisCallback<Void>() {
			
			@Override
			public Void doInRedis(RedisConnection connection)
					throws DataAccessException {
				String pong = connection.ping();
				Assert.assertEquals("PONG", pong);
				return null;
			}
		}, false);
		
		
	}

	@Test
	public void testKeysAndDel(){
		Jedis jedis = new Jedis(shardInfo);
		try{
			final Set<String> keys = redisTemplate.keys("redis_list");
			for(String key:keys){
				System.out.println(key);
			}
			
			jedis.connect();
			Set<String> keys2 = jedis.keys("*redis_list");
			for(String key:keys2){
				System.out.println(key); //���Կ���spring��RedisTemplateд���key���ǵ�����keyǰ�滹�б���ֽڲ�֪������ʲô��
			}
			
			final List<Long> response = new ArrayList<Long>();
			redisTemplate.executePipelined(new RedisCallback<List<Long>>() {
				@Override
				public List<Long> doInRedis(RedisConnection connection)
						throws DataAccessException {
					for(String key:keys){
						response.add(connection.del(key.getBytes()));
					}
					return null;//pipeline�����зǿյķ���! e!
				}
			});
			System.out.println(response);
			 //���Կ���redisTemplate�еĸ����� ʹ�� connection.del(byte[]..)��redisTemplate.delete(String) ��ͬ,��Ϊdelete(String)������ʹ�������һ��private����
			//rawValue���õ�һ��������key.Ȼ���ٴ���connection.del��!
			//Ĭ����ʹ�� defaultSerializer->JdkSerializationRedisSerializer
			
			//��������ڳ�ʼ����ʱ������ʹ��RedisTemplate�����е�һ��StringSerializer�Ļ� ��TestContext�е�����,�������ʹ��redis-cli������ǰ��key�����ý�ȥ��key��һģһ������
			//redisTemplate.setDefaultSerializer(redisTemplate.getStringSerializer());
			redisTemplate.delete(keys);
		}catch(Exception e){
			e.printStackTrace();
		}
		jedis.disconnect();
//		jedis.close();
	}
	
	@Test
	public void testExec(){
		Long ret = redisTemplate.boundListOps("redis_list").leftPush("l1");
		System.out.println(ret);
	}
	
	@Test
	public void testDummy(){
	}

	@ComponentScan
	@Configuration
	public static class TestContext {

		private String host = "localhost";
		private int port = 6379;
		private int timeout = 2000;

		@Bean
		JedisShardInfo shardInfo(){
			return new JedisShardInfo(host, port, timeout);
		}
		
		@Bean
		JedisPoolConfig poolConfig(){
			JedisPoolConfig poolConfig = new JedisPoolConfig();
			poolConfig.setMaxTotal(10);
			poolConfig.setMaxIdle(2);
			poolConfig.setTestOnBorrow(true);
			poolConfig.setTestOnReturn(true);
			return poolConfig;
		}
		
		@Bean
		JedisConnectionFactory jedisConnectionFactory(JedisShardInfo shardInfo,JedisPoolConfig poolConfig) {
			JedisConnectionFactory factory = new JedisConnectionFactory();
			factory.setShardInfo(shardInfo);
			factory.setPoolConfig(poolConfig);
			factory.setUsePool(true);
			return factory;
		}
		
		@Bean
		static <K,V> RedisTemplate<K,V> redisTemplate(RedisConnectionFactory connectionFactory){
			RedisTemplate<K, V> redisTemplate = new RedisTemplate<K,V>();
			redisTemplate.setConnectionFactory(connectionFactory);
			redisTemplate.setDefaultSerializer(redisTemplate.getStringSerializer());
			return redisTemplate;
		}
	}
}
