package cjava.walker.testcase.hsql.embeded;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.sql.DataSource;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class EmbededDatasourceHsqlUsingSpring {

	@Autowired
	private QueryRunner queryRunner;

	@Before
	public void setUp() {
		Assert.assertThat(queryRunner, CoreMatchers.notNullValue());
	}

	@Test
	public void testDummy() {

	}

	@Test
	public void testSimple() {
		try {
			String name = queryRunner.query("select name from person where id = 1", new ScalarHandler<String>(1));
			Assert.assertThat(name, CoreMatchers.is("shijun"));

			Number count = queryRunner.query("select count(*) from person", new ScalarHandler<Number>(1));
			Assert.assertThat(count.intValue(), CoreMatchers.is(2));
			
			Person person = queryRunner.query("select * from person where id = 2", new BeanHandler<Person>(Person.class));
			Assert.assertThat(person.getName(), CoreMatchers.is("mimi"));
			
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String actualDate = format.format(person.getBirth());
			Assert.assertThat(actualDate, CoreMatchers.is("1990-09-28"));
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static class Person {
		private Integer id;
		private String name;
		private Integer age;
		private Date birth;

		public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Integer getAge() {
			return age;
		}

		public void setAge(Integer age) {
			this.age = age;
		}

		public Date getBirth() {
			return birth;
		}

		public void setBirth(Date birth) {
			this.birth = birth;
		}

	}
}
