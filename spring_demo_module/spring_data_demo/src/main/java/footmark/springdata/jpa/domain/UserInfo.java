package footmark.springdata.jpa.domain;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "t_userinfo")
public class UserInfo implements Serializable {
	private static final long serialVersionUID = -5497576489656420425L;

	private Long userid;

	private String username;

	private String password;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getUserid() {
		return userid;
	}

	public void setUserid(Long userid) {
		this.userid = userid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "UserInfo{" + "userid=" + userid + ", username='" + username
				+ '\'' + '}';
	}
}
