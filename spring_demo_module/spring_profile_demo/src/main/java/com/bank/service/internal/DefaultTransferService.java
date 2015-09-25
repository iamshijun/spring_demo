package com.bank.service.internal;

import com.bank.repository.internal.AccountRepository;

public class DefaultTransferService implements TransferService {

	private AccountRepository accountRepository;

	private Policy policy;

	public DefaultTransferService(AccountRepository accountRepository, Policy policy) {
		this.accountRepository = accountRepository;
		this.policy = policy;
	}
	
	
}
