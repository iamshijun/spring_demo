package cjava.walker.testcase.hsql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcWithHsqldbTutorial {

	public Connection getConnection() {
		Connection con = null;
		try {
			//load the HSQLDB Database Driver. 
			//This gets loaded from the hsqldb-xxx.jar
			Class.forName("org.hsqldb.jdbcDriver");
		} catch (ClassNotFoundException cnfe) {
			System.out.println("ERROR: failed to load HSQLDB JDBC driver.");
			cnfe.printStackTrace();
		}

		try {
			//connect to the database. 
			con = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/mytestdb", "sa", "");//默认就是9001了!
		} catch (SQLException e) {
			System.out.println("ERROR: failed to connect to the databse");
			e.printStackTrace();
		}

		return con;
	}

	public void executeInsertQuery(Connection con) {
		PreparedStatement ps = null;

		try {
			ps = con.prepareStatement("INSERT INTO COURSE VALUES(?,?,?)");
			ps.setInt(1, 6);
			ps.setString(2, "Lewis");
			ps.setString(3, "JDBC");
			ps.executeUpdate(); // executes the insert query

		} catch (Exception e) {
			System.out.println("ERROR executing query: ");
			e.printStackTrace();
		} finally {
			try {
				//close the <span id="IL_AD3" class="IL_AD">statement</span>
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void executeSelectQuery(Connection con) {
		PreparedStatement ps = null;

		try {
			ps = con.prepareStatement("SELECT COURSE_ID, NAME, COURSE  FROM COURSE");
			ResultSet rs = ps.executeQuery(); // read from database
			while (rs.next()) {
				Integer id = rs.getInt("COURSE_ID");
				String name = rs.getString("NAME");
				String course = rs.getString("COURSE");
				System.out.println("id:" + id + ", name:" + name + ", course:" + course);
			}

		} catch (Exception e) {
			System.out.println("ERROR executing query: ");
			e.printStackTrace();
		} finally {
			try {
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	//main method
	public static void main(String[] args) {

		JdbcWithHsqldbTutorial tut = new JdbcWithHsqldbTutorial();

		//1. get the connection to the database
		final Connection con = tut.getConnection();

		//2. Insert a record via JDBC
		tut.executeInsertQuery(con);

		//3. select all records from the database
		tut.executeSelectQuery(con);

		//4. close the connection to the databse
		try {
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}