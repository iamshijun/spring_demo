package cjava.walker.spel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cjava.walker.vo.Customer;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=SpelDemoWithValueAnno.TestConfig.class)
//@ComponentScan不能用在这里らしい
public class SpelDemoWithValueAnno {

	@Autowired
	private LegalServices legalServices;
	
	@Value("#{colSel.customers}")
	private List<Customer> customers;
	
	@Value("#{colSel.customers.?[dateOfBirth.before(legalServices.legalLimit)]}") // filter with Criterion : true/ false
	private List<Customer> filters;
	@Value("#{colSel.customers.^[true]}") //select the first member of collection which match the criterion
	private Customer first;
	@Value("#{colSel.customers.$[true]}") //select the last member
	private Customer last;
	
	//默认值用“:”隔开 
	@Value("${service.broke:tcp://192.168.1.188}") //default value specified by delimiter ":"!
	private String serviceBroke;

	//spel add in spring 3.0
	@Value("#{systemProperties['java.tmp.io']}")
	private String tmp;
	
	@Value("#{systemProperties['java.vm.version']}")
	private String vmVersion;

	@Value("#{systemProperties['user.locale']}")
	private String defaultLocal;

	@Value("#{T(java.lang.Math).random() * 100}")
	private Number randonNum;
	
	
	@Value("#{colSel.customers.![birth]}")  //.!
	private List<Date> births;
	
	@Before
	public void setUp(){
		Assert.assertThat(legalServices, CoreMatchers.notNullValue());
	}
	
	@Test
	public void testCollectionSelection(){
//		System.out.println(customers);
		Assert.assertThat(customers, CoreMatchers.notNullValue());
		Assert.assertThat(first, CoreMatchers.equalTo(customers.get(0)));
		Assert.assertThat(last, CoreMatchers.equalTo(customers.get(customers.size()-1)));
	}
	
	@Test
	public void testCollectionTransform(){
		//System.out.println(births);
		Assert.assertThat(births, CoreMatchers.notNullValue());
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		/*for(Date birth : births){
			System.out.println(format.format(birth));
		}*/
		for(Customer customer : filters){
			System.out.println(format.format(customer.getBirth()));
		}
	}
	
	@Test
	public void testDummy(){
		
	}
	
	@ComponentScan
	@Configuration
	static class TestConfig{
		
	}
}

