package com.ideaportal.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import javax.validation.constraints.Pattern;

public class UserDto {

	@NotBlank(message="Username required")
	@Size(min=2,max=30,message="minimum 2 characters required and maximum 30 characters allowed")
	private String userName;
	@NotBlank(message="Email id required!!")
	@Pattern(regexp ="^(.+)@(.+)$",message="Enter valid email id")
	private String userEmail;
	@NotBlank(message="Please Enter Password")
	@Size(min=8,max=20,message="PLease enter minimum 8 character combination of alphanumerics")
	private String userPassword;
	@NotBlank(message = "Please Enter Company Name")
	private String companyName;
	@NotBlank(message = "Please Select Designation: Client PArtner,Project Manager,Employee")
	private String designation;
	
	
	public UserDto() {
	}
	
	
	public UserDto(String userName, String userEmail, String userPassword, String companyName, String designation) {
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
