package footmark.springdata.jpa.domain;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="t_accountinfo")
public class AccountInfo implements Serializable
{
	private static final long serialVersionUID = 7477436428494722994L;

	private Long accountId;

    private UserInfo userInfo;

    private Integer balance;

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    public Long getAccountId()
    {
        return accountId;
    }

    public void setAccountId(Long accountId)
    {
        this.accountId = accountId;
    }

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "userinfo_id")
    public UserInfo getUserInfo()
    {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo)
    {
        this.userInfo = userInfo;
    }

    public Integer getBalance()
    {
        return balance;
    }

    public void setBalance(Integer balance)
    {
        this.balance = balance;
    }

    @Override
    public String toString()
    {
        return "AccountInfo{" +
                "accountId=" + accountId +
                ", userInfo=" + userInfo +
                ", balance=" + balance +
                '}';
    }
}
