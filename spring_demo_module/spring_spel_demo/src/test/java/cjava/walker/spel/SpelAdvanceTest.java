package cjava.walker.spel;

import org.junit.Test;


public class SpelAdvanceTest {

	@Test
	public void test01(){
		String table  = "org_service_student_authorization";
		String statement = "select * from #{table} where id = 1 "
				+ " #{hasShardKey != null && hasShardKey ? 'and shardKey = :shardKey' : ''}"
				+ " #{!T(org.springframework.util.StringUtils).isEmpty(authorityType) ? 'and authority_type = :authorityType' : ''}"
				+ " #{#isEmpty(authorityType) ? 'and authority_type in (:authorityType)' : ''}"
		;
		
		Long itemId = null;
		
		String sql = SqlTemplate.create(statement).withTable(table)
				//.addParameter("hasShardKey", null)
				.addParameter("hasShardKey", true)
				.addParameter("authorityType", "")
				.addIf("itemId", itemId, i -> i != null)
			.getSql();
		System.out.println(sql);
	}
	

}
