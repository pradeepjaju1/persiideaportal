package com.ideaportal.controller;


import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ideaportal.dao.DaoUtils;
import com.ideaportal.dao.ProductManagerdao;
import com.ideaportal.exception.InvalidRoleException;
import com.ideaportal.exception.ThemeNameSameException;
import com.ideaportal.exception.UserNotFoundException;
import com.ideaportal.models.*;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.ideaportal.services.ClientpartnerService;
import com.ideaportal.services.UserService;


@RestController
@RequestMapping("/api")
public class ClientPartnerController {

	@Autowired
    ClientpartnerService clientpartnerService;
	@Autowired
	UserService userService;

	@Value("${server.servlet.context-path}")
	private String contextPath;

	@Value("${server.domain}")
	private String domain;

	@Value("${server.port}")
	private String port;

	@Autowired
	ServletContext context;
	
	@Autowired
	DaoUtils utils;
	@Autowired
	ServletConfig servelet;
	@Autowired
	ProductManagerdao productManagerDAO;
	@Autowired 
	ClientpartnerService clientpartnerServices;

	

	final ObjectMapper objectMapper = new ObjectMapper();

	private static final Logger LOGGER = LoggerFactory.getLogger(ClientPartnerController.class);

	@PostMapping(value = "/user/create/theme")
	public ResponseEntity<Rp<Themes>> uploadReviews(@RequestParam(value="files", required = false) MultipartFile [] files,
																@RequestParam ("userID") String userID, 
																@RequestParam("themeName") String themeName,
																@RequestParam("themeCategory") long themeCategory,
																@RequestParam("themeDescription") String themeDesc) throws ThemeNameSameException, InvalidRoleException {


		

		List<ThemeIdeaFiles> thflist=new ArrayList<>();
		
		User dbUser=utils.findUser(Long.parseLong(userID));
		
		if (dbUser == null) {
			throw new UserNotFoundException("User not found");
		}
		if (dbUser.getRoles().getRoleId() != 1) {
			
			throw new InvalidRoleException("invalid role exception");
		}
	
		
		

		ThemesCategory themesCategory = utils.findThemeCategory(themeCategory);

		Themes themes = new Themes();
		themes.setThemeName(themeName);
		themes.setThemeDescription(themeDesc);
		themes.setThemesCategory(themesCategory);
		themes.setCreationDate(new Date());
		String userName=dbUser.getUserName();
		themes.setUserId(dbUser);




		Rp<Themes> rpm = clientpartnerServices.saveTheme(themes);

		String mainURL = null;
		String uploads_constant = null;

		
			mainURL = "http://" + domain + ":" + port + contextPath;

			uploads_constant = "Uploads" + File.separator + "Themes" + File.separator + userName + File.separator +
					rpm.getResult().getThemeId() + File.separator ;
			if(files!=null) {
			for (MultipartFile myFile : files) {
				if (!myFile.isEmpty()) {

						boolean dirStatus = false;
						File dir = new File(context.getRealPath(uploads_constant));
						if (!dir.exists())
							dirStatus = dir.mkdirs();
						if (dirStatus)
							LOGGER.info("Directory created successfully");
						else
							LOGGER.info("Directory was not created");
						boolean saveStatus = userService.saveFile(myFile, dir);
						if (saveStatus)
							LOGGER.info("File saved at local machine successfully");
					 
					String fileName = myFile.getOriginalFilename();
					ThemeIdeaFiles thf = new ThemeIdeaFiles();


					thf.setThemeId(themes);
					thf.setIdeaId(null);
					thf.setUser(dbUser);
					
			       thf.setThemeideaUrl(mainURL + File.separator + uploads_constant + File.separator + fileName);
					
					thf.setFileType(FilenameUtils.getExtension(fileName));
					
					thf.setFileName(fileName);
					thflist.add(thf);
					themes.setThemeFiles(thflist);
				}
			}
		}

		clientpartnerServices.saveArtifacts(thflist, rpm.getResult().getThemeId());



		

		return new ResponseEntity<>(rpm, new HttpHeaders(),  HttpStatus.valueOf(rpm.getStatus()));
	}
}