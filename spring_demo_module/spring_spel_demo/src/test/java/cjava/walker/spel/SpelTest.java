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
	public void testContext01(){ //fileds/property method 都是在content的根对象 root中或其的 至于所谓的variable需要使用  # 前缀,并且需使用StandardEvaluationContext的setVariable设置
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
		
		//对象属性获取非常简单，即使用如“a.property.property”这种点缀式获取，SpEL对于属性名'首字母'是不区分大小写的
		Date date = new Date();  
		
		//暂时对StandardEvaluationContext 的认知是 作为一个上下文 除了设置rootObject对象之外 还可以设置rootObject之外的一些变量
		//如果没有特别的需要 -- 只是从rootObject中取值的话  直接在parseExpression 返回的Expression的getValue中指定rootObject即可!
		
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
	//SpEL引入了Groovy语言中的安全导航运算符“(对象|属性)?.属性”，用来避免当“?.”前边的表达式为null时抛出空指针异常，而是返回null
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
	
	@Test   // 此处可以看出“registerFunction”和“setVariable”都可以注册自定义函数，但是两个方法的含义不一样，推荐使用“registerFunction”方法注册自定义函数。
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
		
		//not recommanded (命名方式很像 property的获取 )
		result = defaultParser.parseExpression("Year").getValue(context, int.class);  
		Assert.assertEquals(date.getYear(), result);
	}
	///////////////////////////////////////////////////////////////////////////////////////////////
	//SpEL compilation?
	@Test  //注：逻辑运算符不支持 Java中的 && 和 || 。
	public void testLogicExpression(){
		String expression1 = "2>1 and (!true or !false)";
		boolean result1 = defaultParser.parseExpression(expression1).getValue(boolean.class);  
		Assert.assertEquals(true, result1);  
		   
		String expression2 = "2>1 and (NOT true or NOT false)";  
		boolean result2 = defaultParser.parseExpression(expression2).getValue(boolean.class);  
		Assert.assertEquals(true, result2);  
	}
	
	/*
	 * 三目运算符 “表达式1?表达式2:表达式3”用于构造三目运算表达式，如“2>1?true:false”将返回true；
	 * Elivis运算符“表达式1?:表达式2”从Groovy语言引入用于简化三目运算符的，
	 * 当表达式1为非null时则返回表达式1，当表达式1为null时则返回表达式2，简化了三目运算符方式“表达式1? 表达式1:表达式2”，
	 * e.g “null?:false”将返回false，而“true?:false”将返回true；
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
	 * 类类型表达式：使用“T(Type)”来表示java.lang.Class实例，“Type”必须是类全限定名，
	 * “java.lang”包除外，即该包下的类可以不指定包名；使用类类型表达式还可以进行访问类静态方法及类静态字段。
	 */
	@Test  
	public void testClassTypeExpression() {  
		
	    final ExpressionParser parser = defaultParser;
	    
	    //java.lang包类访问   --
	    Class<String> result1 = parser.parseExpression("T(String)").getValue(Class.class);  
	    Assert.assertEquals(String.class, result1);  
	    //其他包类访问  
	    String expression2 = "T(cjava.walker.spel.SpelDemo)";  
	    Class<String> result2 = parser.parseExpression(expression2).getValue(Class.class);   
	    Assert.assertEquals(SpelTest.class, result2);  
	    //类静态字段访问  
	    int result3=parser.parseExpression("T(Integer).MAX_VALUE").getValue(int.class);  
	    Assert.assertEquals(Integer.MAX_VALUE, result3);  
	    //类静态方法调用  
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
	 * 变量定义及引用：变量定义通过EvaluationContext接口的setVariable(variableName, value)方法定义；在表达式中使用“#variableName”引用；
	 * 除了引用自定义变量，SpE还允许引用根对象及当前上下文对象，使用“#root”引用根对象，使用“#this”引用当前上下文对象；
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
	
	/*从Spring3.0.4开始支持内联List，使用{表达式，……}定义内联List，如“{1,2,3}”将返回一个整型的ArrayList，而“{}”将返回空的List，
	 * 对于字面量表达式列表，SpEL会使用java.util.Collections.unmodifiableList方法将列表设置为不可修改。
	 */
	@Test(expected = UnsupportedOperationException.class)
	public void testCollection(){
		List<Integer> list = defaultParser.parseExpression("{}").getValue(List.class);
		list.add(1); //throws UnsupportedOperationException
	}
	
	@Test // (expected = UnsupportedOperationException.class)
	public void testCollection2(){
		//对于字面量列表也将返回不可修改的List  
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
	@SuppressWarnings("rawtypes") 当前版本不支持??
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
		
		//测试集合或数组  
		EvaluationContext context1 = new StandardEvaluationContext();  
		context1.setVariable("collection", collection);  
		List<Integer> result1 =  parser.parseExpression("#collection.![#this+1]").getValue(context1, List.class);  
		Assert.assertEquals(2, result1.size());  
		Assert.assertThat(result1, Matchers.hasItems(5,6));
		
		//3.测试字典  
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
		//如果确保都有city的话  >_>  Members.![placeOfBirth.city]
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
		   
		//集合选择和投影可以一起使用  有点像  stream !java8 23333
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
	//首先可以看到 上述的每一个例子当中 全程都没有使用  #{} 这样的符号包裹着 , 就好象在用java在编程一样  
	// 没有用引号引住的都会到rootObject中找对应的 属性 或者是方法 , #前缀的到varaibles变量集中找..
	@Test
	public void testExpressiontemplating(){
		ExpressionParser parser = new SpelExpressionParser();
		String randomPhrase = parser.parseExpression("'random number is ' + T(java.lang.Math).random()").getValue(String.class);
		System.out.println(randomPhrase);
		
		//------------------------------------------------------------------
		
		//TemplateParserContext
		//这里看到 使用了 非StandardEvaluationContext以外的context, 整个 expression表达式同时也和上述的不太一样!,字符串不再需要单引号  #{} 里面的和之前 StandardEvaluationContext时候的一样 将被解析
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
