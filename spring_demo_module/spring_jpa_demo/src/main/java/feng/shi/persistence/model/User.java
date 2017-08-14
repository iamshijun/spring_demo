package feng.shi.persistence.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "t_meicloud_user")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String username;

	private String rawPassword;
	
	@Column(name="password")
	private String digestPassword;

	private String salt;
	
	@Column(name="hash_iterations")
	private long hashIterations = 0;
	
	@Column(name = "expire_at")
	@Temporal(TemporalType.DATE)
	private Date expireAt;
	
	private String telephone;
	
	private String email;
	
	public User(){}
	
	public User(String username,String rawPassword){
		this.username = username;
		this.rawPassword = rawPassword;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setRawPassword(String rawPassword) {
		this.rawPassword = rawPassword;
	}
	
	public String getRawPassword() {
		return rawPassword;
	}
	
	public String getDigestPassword() {
		return digestPassword;
	}

	public void setDigestPassword(String digestPassword) {
		this.digestPassword = digestPassword;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public void setExpireAt(Date expireAt) {
		this.expireAt = expireAt;
	}
	
	public Date getExpireAt() {
		return expireAt;
	}

	public long getHashIterations() {
		return hashIterations;
	}

	public void setHashIterations(long hashIterations) {
		this.hashIterations = hashIterations;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	
	
}
