package cjava.walker.testcase.hsql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.Test;

public class HsqldbInProcessAccessTest {

	@Test
	public void testMemDatabase() throws Exception {
		try {
			Connection conn = DriverManager.getConnection("jdbc:hsqldb:mem:my", "SA", "");
			Statement st = conn.createStatement();
			st.execute("create table person(id int,name varchar(20))");

			PreparedStatement pst = conn.prepareStatement("insert into person(id,name) values(?,?)");
			pst.setInt(1, 1);
			pst.setString(2,"shijun");
			pst.executeUpdate();

			pst = conn.prepareStatement("table person");
			ResultSet rst = pst.executeQuery();
			while (rst.next()) {
				System.out.println(String.format("id : %s , name : %s", rst.getInt(1), rst.getString(2)));
			}

			st.execute("SHUTDOWN");

			st.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Test
	public void testFileDatabase() throws SQLException {
		//		Connection conn = DriverManager.getConnection("jdbc:hsqldb:file:test", "SA", "");
		//file �����Լ����� hsql��������ݿ�����ƴ�������Ҫ���ļ� �� *.scipt *.lck  *.log *.data *.lobs  *.properties...
		Connection conn = DriverManager.getConnection("jdbc:hsqldb:file:hsqldb/testdb", "SA", "");//����ʹ��'/'-ָ��ʹ�����·��
		System.out.println(conn);
		Statement st = conn.createStatement();
		st.execute("SHUTDOWN");
		conn.close();
	}

	@Test
	//fail!!!!
	public void testResDatabase() throws SQLException {
		//��file��ͬ ��Ҫ�Լ�����resource�ļ�--�����ݿ�������ͬ
		//����url��ָ��һ��Java URL(��class��·������!) 
		Connection conn = DriverManager.getConnection("jdbc:hsqldb:res:res.mydb.resdb", "SA", "");///res/mydbĿ¼��
		//??? 
		System.out.println(conn);
		conn.close();
	}
}
