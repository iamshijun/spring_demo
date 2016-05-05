package cjava.walker.common.service;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

public interface IBookingService {

	/**
	 * 给spring的Autowire byType byName使用(如果没有@Autowired的话 需要setter,spring不会直接设置Field,只会通过setter) ,以及测试
	 * @param jdbcTemplate
	 */
	public abstract void setJdbcTemplate(JdbcTemplate jdbcTemplate);

	public abstract void book(String... persons);

	public abstract List<String> findAllBookings();

}