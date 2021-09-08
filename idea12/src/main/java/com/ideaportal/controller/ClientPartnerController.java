package com.ideaportal.controller;


import java.io.File;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ideaportal.datainteraction.DataInteract;
import com.ideaportal.datainteraction.PMInteract;
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
	DataInteract datainteract;
	@Autowired
	ServletConfig servelet;
	@Autowired
	PMInteract pmInteract;
	@Autowired 
	ClientpartnerService clientpartnerServices;
	final ObjectMapper objectMapper = new ObjectMapper();

	private static final Logger LOG = LoggerFactory.getLogger(ClientPartnerController.class);

	@PostMapping(value = "/user/create/theme")
	public ResponseEntity<Rp<Themes>> uploadReviews(@RequestParam(value="files", required = false) MultipartFile [] files,
																@RequestParam ("userID") String userID, 
																@RequestParam("themeName") String themeName,
																@RequestParam("themeCategory") long themeCategory,
																@RequestParam("themeDescription") String themeDesc) throws ThemeNameSameException, InvalidRoleException {
		LOG.info("Request URL: POST Create Theme");
		List<ThemeIdeaFiles> thflist=new ArrayList<>();
		LOG.info("Control to datainteract.findUser");
		User dbUser=datainteract.findUser(Long.parseLong(userID));
		LOG.info("Control back to ClientPartnerController");
		if (dbUser == null) {
			LOG.error("{} userID not found", userID);
			throw new UserNotFoundException("User not found");
		}
		if (dbUser.getRoles().getRoleId() != 1) {
			LOG.error("You do not have rights to create Theme");
			throw new InvalidRoleException("invalid role exception");
		}
		LOG.info("Control to datainteract.findThemeCategory");
		ThemesCategory themesCategory = datainteract.findThemeCategory(themeCategory);
		LOG.info("Control back to ClientPartnerController");
		Themes themes = new Themes();
		themes.setThemeName(themeName);
		themes.setThemeDescription(themeDesc);
		themes.setThemesCategory(themesCategory);
		themes.setCreationDate(new Date());
		String userName=dbUser.getUserName();
		themes.setUserId(dbUser);
		LOG.info("Control to clientpartnerServices.saveTheme");
		Rp<Themes> rpm = clientpartnerServices.saveTheme(themes);
		LOG.info("Control back to ClientPartnerController");
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
							LOG.info("Directory created");
						else
							LOG.info("Directory not created");
						LOG.info("Control to userService.saveFile");
						boolean saveStatus = userService.saveFile(myFile, dir);
						LOG.info("Control back to ClientPartnerController");
						if (saveStatus)
							LOG.info("File saved at local machine");
					 
					String fileName = myFile.getOriginalFilename();
					ThemeIdeaFiles thf = new ThemeIdeaFiles();
					thf.setThemeId(themes);
					thf.setIdeaId(null);
					thf.setUser(dbUser);
					LOG.info("Theme,User added");
					thf.setThemeideaUrl(mainURL + File.separator + uploads_constant + File.separator + fileName);
					LOG.info("URL added");
					thf.setFileType(FilenameUtils.getExtension(fileName));
					thf.setFileName(fileName);
					thflist.add(thf);
					LOG.info("List of files added to theme");
					themes.setThemeFiles(thflist);
				}
			}
		}
		LOG.info("Control to clientpartnerServices.saveFiles");
		clientpartnerServices.saveFiles(thflist, rpm.getResult().getThemeId());
		LOG.info("Control back to ClientPartnerController");
		LOG.info("Files Record Saved");
		return new ResponseEntity<>(rpm, new HttpHeaders(),  HttpStatus.valueOf(rpm.getStatus()));
	}
}