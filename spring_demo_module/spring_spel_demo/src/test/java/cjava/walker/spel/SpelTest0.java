package cjava.walker.spel;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cjava.walker.vo.NumberGuess;
import cjava.walker.vo.ShapeGuess;
import cjava.walker.vo.TaxCalculator;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class SpelTest0 {

	@Autowired
	private NumberGuess numberGuess;
	@Autowired
	private TaxCalculator taxCalculator;
	@Autowired
	private ShapeGuess shapeGuess;
	
	@Autowired @Qualifier("hello3")	
	private String hello3;
	
	@Autowired @Qualifier("world")
	private String world;

	@Test
	public void testDummy() {

	}

	@Test
	public void test01() {
		Assert.assertTrue(numberGuess.getRandomNumber() == shapeGuess.getInitialShapeSeed());
	}
	
	@Test
	public void testBeanReferenceWithAT(){
		Assert.assertTrue(("Hello" + world).equals(hello3));
	}

}
