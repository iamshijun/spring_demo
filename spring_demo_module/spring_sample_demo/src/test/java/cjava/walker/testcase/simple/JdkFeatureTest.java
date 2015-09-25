package cjava.walker.testcase.simple;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class JdkFeatureTest {

	@Test
	public void testSubList(){
		int realSize = 489;
		List<Integer> ints = new ArrayList<Integer>(realSize);
		for(int i = 0 ; i < realSize;++i ){
			ints.add(null);
		}
		//Collections.fill(ints, 1);
		
		int size = ints.size();
		int fromIndex = 0 , toIndex = Math.min(500, size);
		do{
			List<Integer> subIndexLogs = ints.subList(fromIndex, toIndex);
			simulateBatch(subIndexLogs);
			
			fromIndex = fromIndex + 500;
			toIndex = Math.min(toIndex + 500, size);
		}while(fromIndex < toIndex);
	}
	
	
	protected void simulateBatch(List<Integer> ints){
		System.out.println(ints.size());
	}
}
