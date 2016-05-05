package cjava.walker.common.service;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

public interface IBookingService {

	/**
	 * ��spring��Autowire byType byNameʹ��(���û��@Autowired�Ļ� ��Ҫsetter,spring����ֱ������Field,ֻ��ͨ��setter) ,�Լ�����
	 * @param jdbcTemplate
	 */
	public abstract void setJdbcTemplate(JdbcTemplate jdbcTemplate);

	public abstract void book(String... persons);

	public abstract List<String> findAllBookings();

}