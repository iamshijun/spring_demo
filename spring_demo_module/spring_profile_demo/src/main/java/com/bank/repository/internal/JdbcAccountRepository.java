package com.bank.repository.internal;

import javax.sql.DataSource;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

public class JdbcAccountRepository implements AccountRepository ,InitializingBean{

	private DataSource dataSource;

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public JdbcAccountRepository(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	public JdbcAccountRepository(){}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(dataSource);
	}
}
