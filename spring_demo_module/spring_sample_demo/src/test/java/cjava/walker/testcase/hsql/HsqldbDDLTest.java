package cjava.walker.testcase.hsql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.mysql.jdbc.Driver;

public class HsqldbDDLTest {

	private Connection conn;

	@Before
	public void setUp() throws ClassNotFoundException, SQLException {
		Class.forName(Driver.class.getName().replaceAll("\\.class", ""));
		conn = DriverManager.getConnection("jdbc:hsqldb:file:hsql/mytestdb", "sa", "");
	}

	@Test //use    information_schema.schemata
	public void testCheckTableExists() throws Exception {
//		ResultSet rs = query("select schema_name from information_schema.schemata where schema_name like 'person'");
		System.out.println(tableExist("person"));
	}
	
	boolean tableExist(String tableName) throws SQLException{
		tableName = tableName.toUpperCase();//ÐèÒª´óÐ´!!!
		
		ResultSet rs = query("select count(*) from information_schema.system_tables " +
				" where table_schem = 'PUBLIC'"+ 
				"and table_name = '"+tableName+"'");
		rs.next();
		
		return rs.getInt(1) >= 1;

		/*DatabaseMetaData metaData = conn.getMetaData();
		ResultSet tables = metaData.getTables(null, "PUBLIC", tableName, new String[]{"TABLE"});
		ResultSetMetaData resultSetMetaData = tables.getMetaData();
		int columnCount = resultSetMetaData.getColumnCount();
		while(tables.next()){
			int count = 1;
			while(count++ <= columnCount){
				System.out.print(tables.getObject(1) + " \t");
			}
			System.out.println();
		}*/
	}
	
	@Test
	public void testCreateTable() throws SQLException{
		if(tableExist("person")) return;
		boolean f = execute("create table person (" +
				"id int ," +
				"name varchar(20)," +
				"age int," +
				"birth datetime," +
				"primary key (id)" +
				")");
		System.out.println(f);
	}
	
	@Test
	public void testDrop() throws SQLException{
		System.out.println(execute("drop table person if exists"));
	}
	
	
	Statement getStatement() throws SQLException{
		return conn.createStatement();
	}
	
	boolean execute(String sql) throws SQLException{
		return getStatement().execute(sql);
	}
	
	ResultSet query(String sql) throws SQLException{
		return getStatement().executeQuery(sql);
	}

	@After
	public void tearDown() throws Exception {
		if (conn != null) {
			Statement statement = conn.createStatement();
			statement.execute("shutdown");
		}
	}
}
