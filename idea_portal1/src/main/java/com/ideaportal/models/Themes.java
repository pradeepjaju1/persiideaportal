package com.ideaportal.models;


import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class Themes {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="theme_id")
	private long themeId;
	
	@Column(name = "theme_name",columnDefinition ="TEXT",nullable=false)
	private String themeName;
	
	@Column(name = "theme_description",columnDefinition ="TEXT",nullable=false)
	private String themeDescription;
	
	@OneToMany(mappedBy = "theme")
	@JsonManagedReference("theme_files")
	private List<ThemeIdeaFiles> themeFiles;
	
	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;
	
	@Basic
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "creation_date", nullable = false)
	private Date themeDate;
	
	
	public Themes() {
		// TODO Auto-generated constructor stub
	}
	
	public Themes(long themeId, String themeName, String themeDescription, List<ThemeIdeaFiles> themeFiles, User user,
			Date themeDate) {
		this.themeId = themeId;
		this.themeName = themeName;
		this.themeDescription = themeDescription;
		this.themeFiles = themeFiles;
		this.user = user;
		this.themeDate = themeDate;
	}
	
	public long getThemeId() {
		return themeId;
	}
	public void setThemeId(long themeId) {
		this.themeId = themeId;
	}
	public String getThemeName() {
		return themeName;
	}
	public void setThemeName(String themeName) {
		this.themeName = themeName;
	}
	public String getThemeDescription() {
		return themeDescription;
	}
	public void setThemeDescription(String themeDescription) {
		this.themeDescription = themeDescription;
	}
	public List<ThemeIdeaFiles> getThemeFiles() {
		return themeFiles;
	}
	public void setThemeFiles(List<ThemeIdeaFiles> themeFiles) {
		this.themeFiles = themeFiles;
	}
	public User getUser() {
		return user;
	}
	public void setUserId(User user) {
		this.user = user;
	}
	public Date getThemeDate() {
		return themeDate;
	}
	public void setCreationDate(Date themeDate) {
		this.themeDate = themeDate;
	}

	@Override
	public String toString() {
		return "Themes [themeId=" + themeId + ", themeName=" + themeName + ", themeDescription=" + themeDescription
				+ ", themeFiles=" + themeFiles + ", user=" + user + ", themeDate=" + themeDate + "]";
	}
	
}