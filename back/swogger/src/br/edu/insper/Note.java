package br.edu.insper;

import java.sql.Timestamp;

public class Note {   
	private Integer id;
	private Integer userId;
	private long createdAt; 
	private long updatedAt;
	private String content;
	private String color;
	private Boolean isPrivate;
	private Boolean isConcluded;
	private String commentary;
	private String ownerUsername;
	private String title;
	private String lastUser;
	
	public Note(Integer id,Integer userId,Timestamp createdAt,Timestamp updatedAt,String content,String color,
			Boolean isPrivate, Boolean isConcluded, String commentary, String ownerUsername,String title, String lastUser){
		this.id = id;
		this.userId = userId;
		this.createdAt = createdAt.getTime();
		this.updatedAt = updatedAt.getTime();
		this.content = content;
		this.color = color;
		this.isPrivate = isPrivate;
		this.isConcluded = isConcluded;
		this.commentary = commentary;
		this.ownerUsername = ownerUsername;
		this.title = title;
		this.lastUser = lastUser;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public long getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt.getTime();
	}

	public long getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt.getTime();
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public Boolean getIsPrivate() {
		return isPrivate;
	}

	public void setIsPrivate(Boolean isPrivate) {
		this.isPrivate = isPrivate;
	}
	
	public Boolean getIsConcluded() {
		return isConcluded;
	}

	public void setIsConcluded(Boolean isConcluded) {
		this.isConcluded = isConcluded;
	}
	
	public String getCommentary() {
		return commentary;
	}

	public void setCommentary(String commentary) {
		this.commentary = commentary;
	}

	public String getOwnerUsername() {
		return ownerUsername;
	}

	public void setOwnerUsername(String ownerUsername) {
		this.ownerUsername = ownerUsername;
	}
	
	public void setTitle(String title){
		this.title = title;
	}
	
	
	public String getTitle(){
		return title;
	}
	
	public String getlastUser(){
		return lastUser;
	}
	
	public void setlastUser(String lastUser){
		this.lastUser = lastUser;
	}
	
	public String getlastUser(){
		return this.lastUser;
	}
	
	public void setlastUser(String lastUser){
		this.lastUser = lastUser;
	}
	

		

	
}