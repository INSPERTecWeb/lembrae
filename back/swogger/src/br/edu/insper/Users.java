package br.edu.insper;

import org.springframework.web.multipart.MultipartFile;

public class Users {   
	private Integer id = 0;
	private String email;
	private String username; 
	private String password;
	private MultipartFile avatar;
	
	public Users(Integer id,String email,String username, String password, MultipartFile avatar){
		this.id = id;
		this.email = email;
		this.username = username;
		this.password = password;
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
	public MultipartFile getAvatar() {
		return avatar;
	} 
	public void setAvatar(MultipartFile avatar) {
		this.avatar = avatar;
	} 
	
}