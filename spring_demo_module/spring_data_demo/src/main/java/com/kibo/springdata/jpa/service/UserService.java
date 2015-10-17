package com.kibo.springdata.jpa.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.kibo.springdata.jpa.domain.AccountInfo;


public interface UserService {
    public AccountInfo createNewAccount(String username, String password, Integer initBalance);

    public AccountInfo findAccountInfoById(Long id);

    public Page<AccountInfo> findByBalanceGreaterThan(Integer balance,Pageable pageable);
}
