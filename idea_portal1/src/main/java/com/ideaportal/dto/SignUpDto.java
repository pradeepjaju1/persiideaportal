package com.ideaportal.dto;

public class SignUpDto {
	private String userName;
	private String userEmail;
	private String userPassword;
	private String companyName;
	private String designation;
	
	public SignUpDto() {
	}

	public SignUpDto(String userName, String userEmail, String userPassword, String companyName, String designation) {
		this.userName = userName;
		this.userEmail = userEmail;
		this.userPassword = userPassword;
		this.companyName = companyName;
		this.designation = designation;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}
	
	
}
