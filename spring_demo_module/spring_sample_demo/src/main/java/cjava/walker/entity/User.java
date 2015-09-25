package cjava.walker.entity;

import java.io.Serializable;

public class User implements Serializable{
	
	private static final long serialVersionUID = 617052559554747231L;
	
	private Integer id;
	private String username;

	public User(Integer id, String username) {
		super();
		this.id = id;
		this.username = username;
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

	public String toString(){
		return String.format("[id : %s , name : %s]", id, username);
	}
}
