package cjava.walker.common.service.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cjava.walker.common.service.IBookingService;

@Service // optianal :choose creat in @Configuration
public class BookingService implements IBookingService {

	private Logger Logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	JdbcTemplate jdbcTemplate;

	
	/**
	 * 给spring的Autowire byType byName使用(如果没有@Autowired的话 需要setter,spring不会直接设置Field,只会通过setter) ,以及测试
	 * @param jdbcTemplate
	 */
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	@Override	@Transactional
	public void book(String... persons) {
		for (String person : persons) {
			System.out.println("Booking " + person + " in a seat...");
			jdbcTemplate.update("insert into BOOKINGS(FIRST_NAME) values (?)",
					person);
		}
	};

	
	@Override	@Transactional(readOnly=true)
	public List<String> findAllBookings() {
		this.dummy();
		return jdbcTemplate.query("select FIRST_NAME from BOOKINGS",
				new RowMapper<String>() {
					@Override
					public String mapRow(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getString("FIRST_NAME");
					}
				});
	}

	
	public void dummy(){
		Logger.info("dummy");
	}
}
