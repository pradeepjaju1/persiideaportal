package com.ideaportal.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name ="theme_idea_files")
public class ThemeIdeaFiles {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="file_id")
	private long fileId;
	@Column(name="file_name",columnDefinition = "TEXT",nullable = false)
	private String fileName;
	
	@Column(name="file_type",columnDefinition = "TEXT",nullable = false)
	private String fileType;
	@ManyToOne
	@JoinColumn(name="theme_id")
	private Themes theme;
	@ManyToOne
	@JoinColumn(name="idea_id")
	private Ideas idea;
	
	public ThemeIdeaFiles() {
		// TODO Auto-generated constructor stub
	}	
	
	public ThemeIdeaFiles(long fileId, String fileName, String fileType, Themes themeId, Ideas ideaId) {
		this.fileId = fileId;
		this.fileName = fileName;
		this.fileType = fileType;
		this.theme = themeId;
		this.idea = ideaId;
	}

	public long getFileId() {
		return fileId;
	}

	public void setFileId(long fileId) {
		this.fileId = fileId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public  Themes getThemeId() {
		return this.theme;
	}

	public void setThemeId(Themes themeId) {
		this.theme = themeId;
	}

	public Ideas getIdeaId() {
		return this.idea;
	}

	public void setIdeaId(Ideas ideaId) {
		this.idea = ideaId;
	}



	@Override
	public String toString() {
		return "ThemeIdeaFiles [fileId=" + fileId + ", fileName=" + fileName + ", fileType=" + fileType + ", themeId="
				+ theme + ", ideaId=" + idea + "]";
	}
	
	
}