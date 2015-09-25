package feng.shi.test;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.Collator;
import java.util.Locale;

import org.junit.Assert;
import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class SimpleTest {

	@Test
	public void testCodePoint(){
		String a = "a";
		System.out.println(a.getBytes().length);
		System.out.println(a.codePointAt(0));
		System.out.println(a.codePointCount(0, 1));
		
		
		System.out.println("========================================");
		
		
		String ch = "÷–";
		System.out.println(ch.getBytes().length);
		System.out.println(ch.codePointAt(0));
		System.out.println(ch.codePointCount(0, 1));
		
	}
	
	@Test
	public void testFastJson(){
		JSONArray jsonArray = JSON.parseArray("[{'itemId':1,'itemDetailId':2,'value':'long......text'},"
				+ "{'itemId':2,'value':'»’”Ô'}"
				+ "]");
		for(int i = 0, size = jsonArray.size(); i < size ; ++i){
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			Long itemId = jsonObject.getLong("itemId");
			Long itemDetailId = jsonObject.getLong("itemDetailId");
			String value = jsonObject.getString("value");
			System.out.format("%s-%s-%s\n", new Object[]{itemId,itemDetailId,value});
		}
	}
	
	@Test
	public void testEqual(){
		long lg = 0L;
		Assert.assertTrue(lg == 0);
	}
	
	@Test
	public void testCollator(){
		String str1 = "÷–";
		String str2 = "A";
		
		Collator collator = Collator.getInstance(Locale.CHINA);
		
		int ret = collator.compare(str1, str2);
		
		System.out.println(ret);
		
		System.out.println(Character.isLetter('_'));
		
	}
	
	@Test
	public void testUrl() throws MalformedURLException{
		URL url = new URL("http://www.beta.ablesky.com/exam/sfsdf?fromurl=http://www.salesdemo.com/");
		System.out.println(url.getHost());
		System.out.println(url.getPath());
		System.out.println(url.getQuery());
	}
}

