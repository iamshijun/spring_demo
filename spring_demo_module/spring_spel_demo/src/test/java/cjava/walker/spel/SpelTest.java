package cjava.walker.spel;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParserContext;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.SpelEvaluationException;
import org.springframework.expression.spel.SpelParserConfiguration;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import cjava.walker.support.StringUtils;
import cjava.walker.vo.Inventor;
import cjava.walker.vo.PlaceOfBirth;
import cjava.walker.vo.Society;

@SuppressWarnings("unchecked")
public class SpelTest {

	
	ExpressionParser defaultParser;
	
	
	/**
	 * There are two exceptions that can be thrown, ParseException and
	 * EvaluationException when calling parser.parseExpression and exp.getValue
	 * respectively.
	 */
	@Rule
	public ExpectedException expectedException = ExpectedException.none();
	
	private Society ieee ;
	private List<Inventor> members;
	
	@Before 
	/*data preparation*/
	public void setUp(){
		defaultParser = new SpelExpressionParser();
		
		ieee = new Society();
		
		members = ieee.getMembers();
		Inventor i1 = new Inventor("shi shijun", "China");
		i1.setPlaceOfBirth(new PlaceOfBirth("Beijing", "China"));
		members.add(i1);
		
		Inventor i2 = new Inventor("Washio reina", "Japna");
		i2.setPlaceOfBirth(new PlaceOfBirth("Sakaken", "Japan"));
		members.add(i2);
		
		members.add(new Inventor("Nicolas Tesla", "Serbian"));
		members.add(new Inventor("Michael", "Serbian"));
	}
	
	@Test  /*# indicated the variables!*/
	public void testStringHelloWorld() {
		Expression expression = defaultParser.parseExpression("('Hello' + ' World').concat(#end)");
		EvaluationContext context = new StandardEvaluationContext();
		context.setVariable("end", "!");
		
		Assert.assertEquals("Hello World!", expression.getValue(context));
	}
	
	@Test
	public void testStringCharAt(){
		char c = defaultParser.parseExpression("'Hello'[0]").getValue(char.class);//Character.TYPE
		Assert.assertEquals('H', c);
	}
	
	@Test
	public void testContext01(){ //fileds/property method ������content�ĸ����� root�л���� ������ν��variable��Ҫʹ��  # ǰ׺,������ʹ��StandardEvaluationContext��setVariable����
		EvaluationContext context = new StandardEvaluationContext("Hello world"); 
		int length = defaultParser.parseExpression("length()").getValue(context,int.class);
		Assert.assertEquals(length,"Hello world".length());
		
		byte[] content = defaultParser.parseExpression("bytes").getValue(context,byte[].class);
		Assert.assertTrue(Arrays.equals(content, "Hello world".getBytes()));
		
	}
	//////////////////////////////////////////property,method invoke////////////////////////////////////////////
	@Test
	public void testPropertyAccess(){ // property / method
		//As an example of calling a JavaBean property, the String property 'Bytes' can be called as shown below.
		final ExpressionParser parser = defaultParser;

		// invokes 'getBytes()'
		Expression exp = parser.parseExpression("'Hello World'.bytes");  
//		Expression exp = parser.parseExpression("'Hello World'.Bytes");  //the same 

		byte[] bytes = (byte[]) exp.getValue();
		Assert.assertTrue(Arrays.equals("Hello World".getBytes(), bytes));
		
		//�������Ի�ȡ�ǳ��򵥣���ʹ���硰a.property.property�����ֵ�׺ʽ��ȡ��SpEL����������'����ĸ'�ǲ����ִ�Сд��
		Date date = new Date();  
		
		//��ʱ��StandardEvaluationContext ����֪�� ��Ϊһ�������� ��������rootObject����֮�� ����������rootObject֮���һЩ����
		//���û���ر����Ҫ -- ֻ�Ǵ�rootObject��ȡֵ�Ļ�  ֱ����parseExpression ���ص�Expression��getValue��ָ��rootObject����!
		
		StandardEvaluationContext context = new StandardEvaluationContext(date);  
		long result1 = parser.parseExpression("Time").getValue(context, long.class);  
		Assert.assertEquals(date.getTime(), result1);  
		
		
		long result2 = parser.parseExpression("time").getValue(context, long.class);  
		Assert.assertEquals(date.getTime(), result2);         
	}
	
	@Test
	public void testPropertySet(){
		class Simple {
			public List<Boolean> booleanList = new ArrayList<Boolean>();
		}
		Simple simple = new Simple();

		simple.booleanList.add(true);

		StandardEvaluationContext simpleContext = new StandardEvaluationContext(simple);

		// false is passed in here as a string. SpEL and the conversion service will
		// correctly recognize that it needs to be a Boolean and convert it
		defaultParser.parseExpression("booleanList[0]").setValue(simpleContext, "false");

		// b will be false
		Boolean b = simple.booleanList.get(0);
		Assert.assertFalse(b);
	}
	
	@Test
	public void testAssignment(){
		Inventor inventor = new Inventor();
		StandardEvaluationContext inventorContext = new StandardEvaluationContext(inventor);

		defaultParser.parseExpression("Name").setValue(inventorContext, "Alexander Seovic2");
		Assert.assertTrue(inventor.getName().equals("Alexander Seovic2"));
		// alternatively

		String aleks = defaultParser.parseExpression(
		        "Name = 'Alexandar Seovic'").getValue(inventorContext, String.class);
		Assert.assertTrue(aleks.equals("Alexandar Seovic"));
		Assert.assertTrue(inventor.getName().equals("Alexandar Seovic"));
		
	}
	
	//The Elvis Operator "?:" 
	@Test
	public void testElvisOperator(){
		ExpressionParser parser = new SpelExpressionParser();

		Inventor tesla = new Inventor("Nikola Tesla", "Serbian");
		StandardEvaluationContext context = new StandardEvaluationContext(tesla);

		String name = parser.parseExpression("Name?:'Elvis Presley'").getValue(context, String.class);
		Assert.assertEquals(name,  "Nikola Tesla");
		

		tesla.setName(null); // <<<<_<<<<
		name = parser.parseExpression("Name?:'Elvis Presley'").getValue(context, String.class);
		Assert.assertEquals(name,  "Elvis Presley");
	}
	
	//Safe Navigation operator "?."
	//SpEL������Groovy�����еİ�ȫ�����������(����|����)?.���ԡ����������⵱��?.��ǰ�ߵı��ʽΪnullʱ�׳���ָ���쳣�����Ƿ���null
	@Test
	public void testSafeNavigationOperator(){ // "?."
		ExpressionParser parser = new SpelExpressionParser();

		Inventor tesla = new Inventor("Nikola Tesla", "Serbian");
		tesla.setPlaceOfBirth(new PlaceOfBirth("Smiljan"));

		StandardEvaluationContext context = new StandardEvaluationContext(tesla);

		String city = parser.parseExpression("PlaceOfBirth?.City").getValue(context, String.class);
		Assert.assertEquals(city,  "Smiljan");
		

		tesla.setPlaceOfBirth(null);
		city = parser.parseExpression("PlaceOfBirth?.City").getValue(context, String.class); 
		// will not throw NullPointerException!!!
		Assert.assertNull(city); 
	}
	
	@Test   // �˴����Կ�����registerFunction���͡�setVariable��������ע���Զ��庯�����������������ĺ��岻һ�����Ƽ�ʹ�á�registerFunction������ע���Զ��庯����
	public void testRegistFunction() throws SecurityException, NoSuchMethodException {  
	    ExpressionParser parser = defaultParser;
	    StandardEvaluationContext context = new StandardEvaluationContext();
	    
	    Method parseInt = Integer.class.getDeclaredMethod("parseInt", String.class);
	    
	    context.registerFunction("parseInt", parseInt);  
	    context.setVariable("parseInt2", parseInt);
	    
	    String expression1 = "#parseInt('3') == #parseInt2('3')";  
	    boolean result1 = parser.parseExpression(expression1).getValue(context, boolean.class);  
	    Assert.assertEquals(true, result1);     
	    
	    // 2. 
	    parser = defaultParser;
	    context = new StandardEvaluationContext();

	    context.registerFunction("reverseString",
	        StringUtils.class.getDeclaredMethod("reverseString", new Class[] { String.class }));

	    String helloWorldReversed = parser.parseExpression(
	        "#reverseString('hello')").getValue(context, String.class);
	    Assert.assertEquals(helloWorldReversed, "olleh");     
	}
	
	@Test
	@SuppressWarnings("deprecation")
	public void testFunctionInvoke(){
		Date date = new Date();  
		StandardEvaluationContext context = new StandardEvaluationContext(date);  
		int result = defaultParser.parseExpression("getYear()").getValue(context, int.class);  
		Assert.assertEquals(date.getYear(), result);  
		
		//not recommanded (������ʽ���� property�Ļ�ȡ )
		result = defaultParser.parseExpression("Year").getValue(context, int.class);  
		Assert.assertEquals(date.getYear(), result);
	}
	///////////////////////////////////////////////////////////////////////////////////////////////
	//SpEL compilation?
	@Test  //ע���߼��������֧�� Java�е� && �� || ��
	public void testLogicExpression(){
		String expression1 = "2>1 and (!true or !false)";
		boolean result1 = defaultParser.parseExpression(expression1).getValue(boolean.class);  
		Assert.assertEquals(true, result1);  
		   
		String expression2 = "2>1 and (NOT true or NOT false)";  
		boolean result2 = defaultParser.parseExpression(expression2).getValue(boolean.class);  
		Assert.assertEquals(true, result2);  
	}
	
	/*
	 * ��Ŀ����� �����ʽ1?���ʽ2:���ʽ3�����ڹ�����Ŀ������ʽ���硰2>1?true:false��������true��
	 * Elivis����������ʽ1?:���ʽ2����Groovy�����������ڼ���Ŀ������ģ�
	 * �����ʽ1Ϊ��nullʱ�򷵻ر��ʽ1�������ʽ1Ϊnullʱ�򷵻ر��ʽ2��������Ŀ�������ʽ�����ʽ1? ���ʽ1:���ʽ2����
	 * e.g ��null?:false��������false������true?:false��������true��
	 */
	@Test
	public void testElivis(){
		boolean result1 = defaultParser.parseExpression("null?:false").getValue(boolean.class);
		Assert.assertFalse(result1);

		////////////////////////////////////////////////////////////////////////
		Expression parseExpression = defaultParser.parseExpression("#obj?:false");
		//1.set variable
		Object obj = new Object();
		EvaluationContext context = new StandardEvaluationContext();
		context.setVariable("obj",obj);
		
		//2.
		Object ret = parseExpression.getValue(context, Object.class);
		Assert.assertEquals(obj, ret); 
		
		//3. reset variable
		obj = null;
		context.setVariable("obj",obj);
		ret = parseExpression.getValue(context, Object.class);
		Assert.assertEquals(false, ret);
		
	}
	
	/*
	 * �����ͱ��ʽ��ʹ�á�T(Type)������ʾjava.lang.Classʵ������Type����������ȫ�޶�����
	 * ��java.lang�������⣬���ð��µ�����Բ�ָ��������ʹ�������ͱ��ʽ�����Խ��з����ྲ̬�������ྲ̬�ֶΡ�
	 */
	@Test  
	public void testClassTypeExpression() {  
		
	    final ExpressionParser parser = defaultParser;
	    
	    //java.lang�������   --
	    Class<String> result1 = parser.parseExpression("T(String)").getValue(Class.class);  
	    Assert.assertEquals(String.class, result1);  
	    //�����������  
	    String expression2 = "T(cjava.walker.spel.SpelDemo)";  
	    Class<String> result2 = parser.parseExpression(expression2).getValue(Class.class);   
	    Assert.assertEquals(SpelTest.class, result2);  
	    //�ྲ̬�ֶη���  
	    int result3=parser.parseExpression("T(Integer).MAX_VALUE").getValue(int.class);  
	    Assert.assertEquals(Integer.MAX_VALUE, result3);  
	    //�ྲ̬��������  
	    int result4 = parser.parseExpression("T(Integer).parseInt('1')").getValue(int.class);  
	    Assert.assertEquals(1, result4);  
	} 
	
	@Test  
	public void testConstructorExpression() {  
	    final ExpressionParser parser = defaultParser;
	    String result1 = parser.parseExpression("new String('haha')").getValue(String.class);  
	    Assert.assertEquals("haha", result1);  
	    Date result2 = parser.parseExpression("new java.util.Date()").getValue(Date.class);  
	    Assert.assertNotNull(result2);  
	} 
	
	@Test
	public void testInstanceof(){
		final ExpressionParser parser = defaultParser;
		boolean ret = parser.parseExpression("'haha' instanceof T(String)").getValue(boolean.class);
		Assert.assertTrue(ret);
	}
	
	@Test
	public void testBeanReferences(){
	    ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext();  
	    ctx.refresh();
	    
	    ExpressionParser parser = new SpelExpressionParser();  
	    StandardEvaluationContext context = new StandardEvaluationContext();  
	    
	    context.setBeanResolver(new BeanFactoryResolver(ctx));  
	    //This will end up calling resolve(context,"systemProperties") on MyBeanResolver during evaluation
	    Properties result = parser.parseExpression("@systemProperties").getValue(context, Properties.class);  
	    Assert.assertEquals(System.getProperties(), result);  
	}
	
	///////////////////////////////////////varaibles , #this ,#root///////////////////////////////////////////////////////
	/*
	 * �������弰���ã���������ͨ��EvaluationContext�ӿڵ�setVariable(variableName, value)�������壻�ڱ��ʽ��ʹ�á�#variableName�����ã�
	 * ���������Զ��������SpE���������ø����󼰵�ǰ�����Ķ���ʹ�á�#root�����ø�����ʹ�á�#this�����õ�ǰ�����Ķ���
	 */
	@Test  
	public void testVariableExpression() {  
	    ExpressionParser parser = defaultParser;
	    EvaluationContext context = new StandardEvaluationContext();  
	    context.setVariable("variable", "haha");  
	    //context.setVariable("variable", "haha");  
	    String result1 = parser.parseExpression("#variable").getValue(context, String.class);  
	    Assert.assertEquals("haha", result1);  
	    
	    
	    Inventor tesla = new Inventor("Nikola Tesla", "Serbian");
	    context = new StandardEvaluationContext(tesla);
	    context.setVariable("newName", "Mike Tesla");

	    parser.parseExpression("Name = #newName").getValue(context);

	    Assert.assertEquals("Mike Tesla", tesla.getName());  
	   
	   /* Inventor inventor = new Inventor("Nikola Tesla", "Serbian");
	    context = new StandardEvaluationContext(inventor);
	    Inventor result2 = parser.parseExpression("#root").getValue(context, Inventor.class);  
	    Assert.assertEquals(inventor, result2);  
	    Inventor result3 = parser.parseExpression("#this").getValue(context, Inventor.class);  
	    Assert.assertEquals(inventor, result3);  */
	}  
	
	
	/*The StandardEvaluationContext is relatively expensive to construct and during repeated usage it builds up cached state that enables subsequent expression evaluations to be performed more quickly. 
	*For this reason it is better to cache and reuse them where possible, rather than construct a new one for each expression evaluation.
	*/
	
	//////////////////////////////////////////////// Collection , Map /////////////////////////////////////////////////////////////////////
	
	/*��Spring3.0.4��ʼ֧������List��ʹ��{���ʽ������}��������List���硰{1,2,3}��������һ�����͵�ArrayList������{}�������ؿյ�List��
	 * �������������ʽ�б�SpEL��ʹ��java.util.Collections.unmodifiableList�������б�����Ϊ�����޸ġ�
	 */
	@Test(expected = UnsupportedOperationException.class)
	public void testCollection(){
		List<Integer> list = defaultParser.parseExpression("{}").getValue(List.class);
		list.add(1); //throws UnsupportedOperationException
	}
	
	@Test // (expected = UnsupportedOperationException.class)
	public void testCollection2(){
		//�����������б�Ҳ�����ز����޸ĵ�List  
		List<Integer> list2 = defaultParser.parseExpression("{1,2,3}").getValue(List.class);  
		Assert.assertEquals(new Integer(1), list2.get(0));  
		expectedException.expect(UnsupportedOperationException.class);
		list2.add(4);
	}
	
	@Test
	@SuppressWarnings("rawtypes")
	public void testInlineList(){
		ExpressionParser parser = defaultParser;
		StandardEvaluationContext context = new StandardEvaluationContext();
		List numbers = (List) parser.parseExpression("{1,2,3,4}").getValue(/*List.class*/context);
		System.out.println(numbers);
		
		List listOfLists = (List) parser.parseExpression("{{'a','b'},{'x','y'}}").getValue(/*List.class*/context);
		System.out.println(listOfLists);
	}
	
	/*@Test 
	@SuppressWarnings("rawtypes") ��ǰ�汾��֧��??
	public void testInlineMap(){
		ExpressionParser parser = defaultParser;
		StandardEvaluationContext context = new StandardEvaluationContext();
		Map inventorInfo = (Map) parser.parseExpression("{name:'Nikola',dob:'10-July-1856'}").getValue(context);
		System.out.println(inventorInfo);
		
		Map mapOfMaps = (Map) parser.parseExpression("{name:{first:'Nikola',last:'Tesla'},dob:{day:10,month:'July',year:1856}}").getValue(context);
		System.out.println(mapOfMaps);
	}*/
	
	//Collection Selection  !!!
	@Test 
	public void testCollectionSelection0() {  
		// create an array of integers
		List<Integer> primes = new ArrayList<Integer>();
		primes.addAll(Arrays.asList(2,3,5,7,11,13,17));

		// create parser and set variable 'primes' as the array of integers
		ExpressionParser parser = defaultParser;
		StandardEvaluationContext ctx = new StandardEvaluationContext();
		ctx.setVariable("primes", primes);
		
		Expression expression = parser.parseExpression("#primes.?[#this>10]");
		List<Integer> primesGreaterThanTen = (List<Integer>) expression.getValue(ctx);
		primesGreaterThanTen.forEach(System.out::println);
	}  
	
	@Test
	public void testCollectionSelection(){
		final ExpressionParser parser = defaultParser;
		StandardEvaluationContext societyContext = new StandardEvaluationContext(ieee);
		List<Inventor> list = (List<Inventor>) parser.parseExpression("Members.?[Nationality == 'Serbian']").getValue(societyContext);
		
		list.stream().map(Inventor::getName).forEach(System.out::println);
	}
	
	@Test   // ".!"
	public void testCollectionProjection(){
		final ExpressionParser parser = defaultParser;
		
		Collection<Integer> collection = new ArrayList<Integer>();  
		collection.add(4);   collection.add(5);  
		
		Map<String, Integer> map = new HashMap<String, Integer>();  
		map.put("a", 1);    map.put("b", 2);  
		
		//���Լ��ϻ�����  
		EvaluationContext context1 = new StandardEvaluationContext();  
		context1.setVariable("collection", collection);  
		List<Integer> result1 =  parser.parseExpression("#collection.![#this+1]").getValue(context1, List.class);  
		Assert.assertEquals(2, result1.size());  
		Assert.assertThat(result1, Matchers.hasItems(5,6));
		
		//3.�����ֵ�  
		EvaluationContext context2 = new StandardEvaluationContext();  
		context2.setVariable("map", map);  
		List<Integer> result2 =  parser.parseExpression("#map.![ value*2]").getValue(context2, List.class);  
		Assert.assertEquals(2, result2.size());  
		Assert.assertThat(result2, Matchers.hasItems(2,4));
		
		/*
		 * Projection allows a collection to drive the evaluation of a
		 * sub-expression and the result is a new collection. The syntax for
		 * projection is ![projectionExpression]. Most easily understood by
		 * example, suppose we have a list of inventors but want the list of
		 * cities where they were born. Effectively we want to evaluate
		 * placeOfBirth.city for every entry in the inventor list. Using
		 * projection:
		 */
		EvaluationContext context = new StandardEvaluationContext(ieee);
		List<String> placesOfBirth = (List<String>)parser.parseExpression("Members.![placeOfBirth?.city]").getValue(context);
		//���ȷ������city�Ļ�  >_>  Members.![placeOfBirth.city]
		System.out.println(placesOfBirth);
		Assert.assertThat(placesOfBirth, Matchers.hasItems("Beijing","Sakaken"));
	}
	
	@Test   //  ".?"
	public void testCollectionFilter(){ 
		final ExpressionParser parser = defaultParser;
		
		Collection<Integer> collection = new ArrayList<Integer>();
		collection.add(4);	collection.add(5);
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("a", 1);	map.put("b", 2);
		
		EvaluationContext context1 = new StandardEvaluationContext();  
		context1.setVariable("collection", collection);
		
		List<Integer> result1 =  	parser.parseExpression("#collection.?[#this>4]").getValue(context1, List.class);
		
		Assert.assertEquals(1, result1.size());  
		Assert.assertEquals(new Integer(5), result1.get(0));  
		
		
		EvaluationContext context2 = new StandardEvaluationContext();  
		context2.setVariable("map", map);  
		Map<String, Integer> result2 =  parser.parseExpression("#map.?[#this.key != 'a']").getValue(context2, Map.class);  
		Assert.assertEquals(1, result2.size());  
		   
		//����ѡ���ͶӰ����һ��ʹ��  �е���  stream !java8 23333
		List<Integer> result3 =  parser.parseExpression("#map.?[key != 'a'].![value+1]").getValue(context2, List.class);  
		Assert.assertEquals(new Integer(3), result3.iterator().next());  
	}
	
	
////////////////////////////////////////////////Expression Template/////////////////////////////////////////////////////////////////////
	@Test
	public void testExpressionTemplate(){
		String randomPhrase = 
				   defaultParser.parseExpression("random number is #{T(java.lang.Math).random()}", 
				                          new TemplateParserContext()).getValue(String.class);
		System.out.println(randomPhrase);
	}
	
	@Test  
	public void testXmlExpression() {  
	    ConfigurableApplicationContext ctx = new ClassPathXmlApplicationContext("SpelTest-context.xml", SpelTest.class);
	    
	    String hello1 = ctx.getBean("hello1", String.class);  
	    String hello2 = ctx.getBean("hello2", String.class);  
	    String hello3 = ctx.getBean("hello3", String.class);  
	    
	    Assert.assertEquals("Hello World!", hello1);  
	    Assert.assertEquals("Hello World!", hello2);  
	    Assert.assertEquals("Hello World!", hello3);  
	    
	    ctx.close();
	}   
	
////////////////////////////////////////////////SpelParseConfiguration/////////////////////////////////////////////////////////////////////
	@Test
	public void testWithSpelParseConfiguration(){
		class Demo {
		    public List<String> list;
		}
		// Turn on:
		// - auto null reference initialization
		// - auto collection growing
		SpelParserConfiguration config = new SpelParserConfiguration(true,true);

		ExpressionParser parser = new SpelExpressionParser(config);

		Expression expression = parser.parseExpression("list[3]");

		Demo demo = new Demo();
		Assert.assertNull(demo.list);
		
		Object o = expression.getValue(demo);
		Assert.assertTrue(o instanceof String);
		
		String str = (String)o;
		Assert.assertEquals(str, "");
		
		// demo.list will now be a real collection of 4 entries
		// Each entry is a new empty String
		Assert.assertThat(demo.list, Matchers.hasSize(4));
	}
	
	//---------------------------------	-------------------------
	@Test
	public void testPropertyNotExist(){
		Inventor inventor = new Inventor();
		ExpressionParser parser = new SpelExpressionParser();
		
		expectedException.expect(SpelEvaluationException.class);
		//when parseing expression a SpelEvaluationException will be thrown
		
		parser.parseExpression("language").getValue(inventor);
	}
	
	//------------------Expression templating--------------------
	//���ȿ��Կ��� ������ÿһ�����ӵ��� ȫ�̶�û��ʹ��  #{} �����ķ��Ű����� , �ͺ�������java�ڱ��һ��  
	// û����������ס�Ķ��ᵽrootObject���Ҷ�Ӧ�� ���� �����Ƿ��� , #ǰ׺�ĵ�varaibles����������..
	@Test
	public void testExpressiontemplating(){
		ExpressionParser parser = new SpelExpressionParser();
		String randomPhrase = parser.parseExpression("'random number is ' + T(java.lang.Math).random()").getValue(String.class);
		System.out.println(randomPhrase);
		
		//------------------------------------------------------------------
		
		//TemplateParserContext
		//���￴�� ʹ���� ��StandardEvaluationContext�����context, ���� expression���ʽͬʱҲ�������Ĳ�̫һ��!,�ַ���������Ҫ������  #{} ����ĺ�֮ǰ StandardEvaluationContextʱ���һ�� ��������
		randomPhrase = parser.parseExpression(
		        "random number is #{T(java.lang.Math).random()}",
		        new TemplateParserContext()).getValue(String.class);
		System.out.println(randomPhrase);
	}
	
	@Test
	public void testParserContext() {
		ExpressionParser parser = new SpelExpressionParser();
		ParserContext parserContext = new ParserContext() {
			public boolean isTemplate() {return true;}

			public String getExpressionPrefix() {return "#{";}

			public String getExpressionSuffix() {return "}";}
		};
		String template = "#{'Hello '}#{'World!'}";
		
		Expression expression = parser.parseExpression(template, parserContext);
		
		Assert.assertEquals("Hello World!", expression.getValue());
	}
	
	
}
