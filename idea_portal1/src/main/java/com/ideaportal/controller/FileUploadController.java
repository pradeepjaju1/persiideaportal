package com.ideaportal.controller;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.management.relation.InvalidRoleInfoException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.ideaportal.dao.DaoUtils;
import com.ideaportal.helper.FileUploadHelper;
import com.ideaportal.models.ThemeIdeaFiles;
import com.ideaportal.models.Themes;
import com.ideaportal.models.User;
import com.ideaportal.repo.ThemeIdeaFilesRepository;
import com.ideaportal.repo.ThemesRepository;

@RestController
public class FileUploadController {

	boolean isUpload;
	@Autowired
	ThemesRepository themeRepo;
	@Autowired
	ThemeIdeaFilesRepository themeIdeaRepo;
	@Autowired
	private FileUploadHelper fileUploadHelper;
	@Autowired
	DaoUtils utils;	
	
	@PostMapping("/theme-files")
	public ResponseEntity<String> uploadFile(@RequestParam("userId")String userId,@RequestParam("themeName")String themeName,@RequestParam("themeDescription")String themeDescription,@RequestParam("file")MultipartFile[] file) throws InvalidRoleInfoException
	{
		List<ThemeIdeaFiles> files=new ArrayList<ThemeIdeaFiles>();
		User dbUser = this.utils.findByUserId(Long.parseLong(userId));
		if(dbUser==null)
		{
			throw new UsernameNotFoundException("User Not Found, Please try again!"); 
		}
		if(dbUser.getRoles().getRoleId()!=1)
		{
			throw new InvalidRoleInfoException("Invalid Role id was passed, Please try again!");
		}
		final Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
		final Themes themes = new Themes();
		themes.setThemeName(themeName);
		themes.setThemeDescription(themeDescription);
		themes.setUserId(dbUser);
		themes.setCreationDate(timestamp);
		ThemeIdeaFiles themeFiles=new ThemeIdeaFiles();
		try {
		for(MultipartFile f1:file)
		{
			if(f1.isEmpty())
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Please upload files!!");
			isUpload=fileUploadHelper.uploadFile(f1);
			themeFiles.setFileName(f1.getOriginalFilename());
			themeFiles.setFileType(f1.getContentType());
			themeFiles.setThemeId(themes);
			themeFiles.setIdeaId(null);
			themeIdeaRepo.save(themeFiles);
			files.add(themeFiles);
		}
		themes.setThemeFiles(files);
		if(isUpload)
		{
			int size=file.length;
			return ResponseEntity.ok(size+" file uploaded successfully");
		}
			themeRepo.save(themes);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong!!");
	}
}
