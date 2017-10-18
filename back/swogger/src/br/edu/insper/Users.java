package br.edu.insper;

public class Users {   
	private Integer id = 0;
	private String email;
	private String username; 
	private String password;
	private String salt;
	private String avatar;
	
	public Users(Integer id,String email,String username, String password, String salt, String avatar){
		this.id = id;
		this.email = email;
		this.username = username;
		this.password = password;
		this.salt = salt;
		this.avatar = avatar;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
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
	public String getSalt() {
		return salt;
	}
	public void setSalt(String salt) {
		this.salt = salt;
	}
		public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	
}