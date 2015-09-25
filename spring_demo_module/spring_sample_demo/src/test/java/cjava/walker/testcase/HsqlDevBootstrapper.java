package cjava.walker.testcase;


import javax.naming.NamingException;

import org.springframework.beans.factory.BeanCreationException;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;

/**
 * helper class to bootstrap the sybase database datasources
 * hsqldb 并绑定到jndi中 测试用 !
 */
public class HsqlDevBootstrapper {

	public static final String DRIVER_CLASS = "org.hsqldb.jdbcDriver";
	public String jndiBindingDb = "java:comp/env/jdbc/dataSource/mydb"; //default

	
	public void setJndiBindingDb(String jndiBindingDb) {
		this.jndiBindingDb = jndiBindingDb;
	}
	
	private SimpleNamingContextBuilder builder; //Spring JNDI mock class

	/**
	 * setup HSQL DB, and bind it to jndi tree
	 */
	public void start() {
		try {
			builder = SimpleNamingContextBuilder.emptyActivatedContextBuilder();
			//SimpleNamingContextBuilder 为我们简化了在JNDI绑定的过程
			
			DriverManagerDataSource ds = new DriverManagerDataSource();
			ds.setDriverClassName(DRIVER_CLASS);
			ds.setUrl("jdbc:hsqldb:mem:my_db"); //in memory HSQL DB URL
			ds.setUsername("root");
			ds.setPassword("eucita");
			
			builder.bind(jndiBindingDb, ds);

		} catch (NamingException e) {
			throw new BeanCreationException(e.getExplanation());
		}
	}

	public void stop() {
		builder.deactivate();
		builder.clear();
	}

}