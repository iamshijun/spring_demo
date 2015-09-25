package cjava.walker.spel;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.util.GregorianCalendar;
import java.util.Properties;

import org.junit.Test;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import cjava.walker.vo.Customer;

public class ExpressionParserTest {

	@Test
	public void testParse() {
		GregorianCalendar c = new GregorianCalendar();
		c.set(1856, 7, 9);

		final String name = "shijun.shi";
		Customer customer = new Customer(name, c.getTime());

		ExpressionParser parser = new SpelExpressionParser();
		Expression exp = parser.parseExpression("name");

		//act as ReflectionUtils
		assertThat((String) exp.getValue(customer), is(name));
	}
	
	@Test
	public void testParseWithContext() {
		GregorianCalendar c = new GregorianCalendar();
		c.set(1989, 1, 12);
		
		final String name = "shijun.shi";
		Customer customer = new Customer(name, c.getTime());
		
		ExpressionParser parser = new SpelExpressionParser();
		StandardEvaluationContext context = new StandardEvaluationContext(customer);
		
		Expression exp = parser.parseExpression("name");
		
		//act as ReflectionUtils
		assertThat(exp.getValue(context,String.class), is(name));
	}
	
	@Test
	public void testEvaludationContext(){
		GregorianCalendar c = new GregorianCalendar();
		c.set(1990,9,28);
		
		final String name = "mimi";
		Customer customer = new Customer(name, c.getTime());
		Customer client = new Customer("hxc", c.getTime());
		
		ExpressionParser parser = new SpelExpressionParser();
		StandardEvaluationContext context = new StandardEvaluationContext();
		
		context.setRootObject(customer);//name ����root����(��Ϊname��û���κε����� !)
		
		String me = "shijun.shi";
		context.setVariable("me", me);
		context.setVariable("client", client);
		
		Expression exp = parser.parseExpression("name");
		assertThat(exp.getValue(context,String.class), is(customer.getName()));

		exp = parser.parseExpression("#client.name");//#client�õ���context������Ϊclient������
		assertThat(exp.getValue(context,String.class), is(client.getName()));
	
		Object thisObj = parser.parseExpression("#this").getValue(context);
		System.out.println(thisObj);
		
		Class<?> clz = parser.parseExpression("#root").getValueType(context);
		assertTrue(clz.equals(Customer.class));
		
		assertThat(parser.parseExpression("#me").getValue(context,String.class),is(me));
		
		boolean b = parser.parseExpression("#this == #root").getValue(context,Boolean.class);//?
		assertTrue(b);
		
		//ʹ�á�#root�����ø�����ʹ�á�#this�����õ�ǰ�����Ķ���???
	}
	
	
	@Test
	public void testFunctionExpression() throws SecurityException,
			NoSuchMethodException {
		
		ExpressionParser parser = new SpelExpressionParser();
		StandardEvaluationContext context = new StandardEvaluationContext();
		
		Method parseInt = Integer.class.getDeclaredMethod("parseInt",String.class);
		
		context.registerFunction("parseInt", parseInt);
		context.setVariable("parseInt2", parseInt);
		
		String expression1 = "#parseInt('3') == #parseInt2('3')";
		
		boolean result1 = parser.parseExpression(expression1).getValue(context,
				boolean.class);
		assertTrue(result1);
	}
	
	@Test
	public void testBeanExpression() {
		/*
		 * SpEL֧��ʹ�á�@������������Bean��������Beanʱ��Ҫʹ��BeanResolver�ӿ�ʵ��������Bean��
		 * Spring�ṩBeanFactoryResolverʵ��
		 */
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext();
		ctx.refresh();
		
		ExpressionParser parser = new SpelExpressionParser();
		StandardEvaluationContext context = new StandardEvaluationContext();
		
		context.setBeanResolver(new BeanFactoryResolver(ctx));
		
		Properties result1 = parser.parseExpression("@systemProperties").getValue(context, Properties.class);
		
		assertEquals(System.getProperties(), result1);
	}
}
