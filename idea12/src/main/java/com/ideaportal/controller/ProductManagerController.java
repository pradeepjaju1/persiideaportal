package com.ideaportal.controller;

import java.io.File;



import java.io.IOException;


import java.net.URISyntaxException;


import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;


import com.fasterxml.jackson.databind.ObjectMapper;

import com.ideaportal.models.*;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.ideaportal.datainteraction.DataInteract;
import com.ideaportal.exception.InvalidRoleException;
import com.ideaportal.exception.UserNotFoundException;
import com.ideaportal.services.ProductManagerService;
import com.ideaportal.services.UserService;


@RestController

@RequestMapping(value = "/api")
public class ProductManagerController {
	@Autowired
	UserService userService;

	
	@Autowired
	ProductManagerService pmService;

	@Autowired
	ServletContext context;

	@Autowired
	DataInteract datainteract;

	

	

	private static final Logger LOG = LoggerFactory.getLogger(ProductManagerController.class);

	final ObjectMapper objectMapper = new ObjectMapper();

	@PostMapping(value = "/user/create/idea")
	public ResponseEntity<Rp<Ideas>> createNewIdea(@RequestParam ("userID") String userID, @RequestParam("themeID") String themeID,@RequestParam("ideaName") String ideaName, @RequestParam("ideaDescription") String ideaDescription,
																@RequestParam(value = "files", required = false) MultipartFile [] files) throws  IOException, URISyntaxException {

		LOG.info("Request URL: POST Create Idea");
		LOG.info("Control to datainteract.findUser");
		User user = datainteract.findUser(Long.parseLong(userID));
		LOG.info("Control back to ProductManagerController");
		LOG.info("Control to datainteract.findThemeByID");
		Themes themes=datainteract.findThemeByID(Long.parseLong(themeID));
		LOG.info("Control back to ProductManagerController");
		List<ThemeIdeaFiles> thfList=new ArrayList<>();
		if (user == null) {
			LOG.error("{} userID not found", userID);
			throw new UserNotFoundException("User not found");
		}
		if (user.getRoles().getRoleId() != 2) {
			LOG.error("You do not have rights to create Idea");
			throw new InvalidRoleException("invalid role exception");
		}
		String userName=user.getUserName();
		String cpUserName=themes.getUser().getUserName();
		Ideas idea =new Ideas();
		Timestamp timestamp=Timestamp.valueOf(LocalDateTime.now());
		idea.setIdeaDate(timestamp);
		idea.setIdeaDescription(ideaDescription);
		idea.setTheme(themes);
		idea.setUser(user);
		LOG.info("Adding date,description,theme,user of that idea in database");
		LOG.info("Control to pmService.createNewIdeaResponseMessage");
		Rp<Ideas> rpm = pmService.createNewIdeaResponseMessage(idea);
		LOG.info("Control back to ProductManagerController");
		String mainURL = null;
		String uploads_constant = null;

		
			mainURL = "http://" + "localhost" + ":" + "8081" + "/";
			uploads_constant = "Uploads" + File.separator + "Themes" + File.separator + cpUserName + File.separator +
					themeID + File.separator + "Ideas" + File.separator + userName + File.separator +
					rpm.getResult().getIdeaId() + File.separator + timestamp.getTime();
		
		
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
					 
					}
					String fileName = myFile.getOriginalFilename();
					ThemeIdeaFiles thf = new ThemeIdeaFiles();

					thf.setIdeaId(idea);
					thf.setThemeId(themes);
					thf.setUser(user);
					LOG.info("Theme,User,Idea added");
					thf.setThemeideaUrl(mainURL + File.separator + uploads_constant + File.separator + fileName);
					LOG.info("URL added");
					thf.setFileType(FilenameUtils.getExtension(fileName));
					thf.setFileName(fileName);
					thfList.add(thf);
					LOG.info("List of files added to idea");
					idea.setIdeaFiles(thfList);
				}
			
		}
		LOG.info("Control to pmService.saveFiles");
		this.pmService.saveFiles(thfList,rpm.getResult().getIdeaId());
		LOG.info("Control back to ProductManagerController");
		LOG.info("Files Record Saved");

		return new ResponseEntity<>(rpm, HttpStatus.valueOf(rpm.getStatus()));
	
	}
	
}
