package com.bank.test;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.support.GenericXmlApplicationContext;

import com.bank.repository.internal.AccountRepository;
import com.bank.service.internal.TransferService;

public class IntegrationTests {
	
	@Test
	public void transferTenDollars(){
		try{
		GenericXmlApplicationContext ctx = new GenericXmlApplicationContext();
		ctx.getEnvironment().setActiveProfiles("dev"); //caveat : set profile before load configurations!!!
		ctx.load("classpath:applicationContext.xml"); //建议使用GenericXmlApplicationContext不使用ClassPathXmlApplicationContext,这个更灵活
		ctx.refresh();
		
		TransferService transferService = ctx.getBean(TransferService.class);
		AccountRepository accountRepository = ctx.getBean(AccountRepository.class);
		
		Assert.assertThat(transferService,CoreMatchers.notNullValue());
		Assert.assertThat(accountRepository,CoreMatchers.notNullValue());
		
		}catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
}
