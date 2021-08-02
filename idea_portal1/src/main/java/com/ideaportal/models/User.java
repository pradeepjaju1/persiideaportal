package com.ideaportal.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="User")
public class User {

		@Id
		@GeneratedValue(strategy = GenerationType.AUTO)
		@Column(name="user_id")
		private long userId;
		@Column(nullable=false,name="user_name",columnDefinition = "TEXT",unique = true)
		private String userName;
		@Column(nullable=false,name="user_email_id",columnDefinition = "TEXT",unique = true)
		private String userEmail;
		@Column(nullable=false,name="user_password",columnDefinition = "TEXT")
		private String userPassword;
		@Column(nullable=false,name="company_name",columnDefinition = "TEXT")
		private String companyName;
		@OneToOne
		@JoinColumn(name = "role_id")
		private Roles role;
		
		
		public User() {
			// TODO Auto-generated constructor stub
		}


		public User(final long userId,final String userName,final String userEmail,final String userPassword,final String companyName,final
				Roles role) {
			this.userId = userId;
			this.userName = userName;
			this.userEmail = userEmail;
			this.userPassword = userPassword;
			this.companyName = companyName;
			this.role = role;
		}


		public long getUserId() {
			return this.userId;
		}


		public void setUserId(long userId) {
			this.userId = userId;
		}


		public String getUserName() {
			return this.userName;
		}


		public void setUserName(String userName) {
			this.userName = userName;
		}


		public String getUserEmail() {
			return this.userEmail;
		}


		public void setUserEmail(String userEmail) {
			this.userEmail = userEmail;
		}


		public String getUserPassword() {
			return this.userPassword;
		}


		public void setUserPassword(String userPassword) {
			this.userPassword = userPassword;
		}


		public String getCompanyName() {
			return this.companyName;
		}


		public void setCompanyName(String companyName) {
			this.companyName = companyName;
		}


		public Roles getRole() {
			return this.role;
		}


		public void setRole(Roles role) {
			this.role = role;
		}


		@Override
		public String toString() {
			return "User [userId=" + userId + ", userName=" + userName + ", userEmail=" + userEmail + ", userPassword="
					+ userPassword + ", companyName=" + companyName + ", role=" + role + "]";
		}
		
		
}