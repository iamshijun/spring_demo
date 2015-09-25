package footmark.springdata.jpa.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;

import footmark.springdata.jpa.domain.AccountInfo;

public interface UserDao extends Repository<AccountInfo, Long> {
	
    public AccountInfo save(AccountInfo accountInfo);

    public AccountInfo findByAccountId(Long accountId);

    public Page<AccountInfo> findByBalanceGreaterThan(Integer balance,Pageable pageable);
}
