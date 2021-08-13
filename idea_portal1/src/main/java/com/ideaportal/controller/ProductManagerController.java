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
import org.springframework.beans.factory.annotation.Value;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import com.ideaportal.dao.DaoUtils;

import com.ideaportal.services.ProductManagerService;
import com.ideaportal.services.UserService;


@RestController

public class ProductManagerController {
	@Autowired
	UserService userService;

	@Value("${server.servlet.context-path}")
	private String contextPath;

	@Value("${server.domain}")
	private String domain;

	@Value("${server.port}")
	private String port;

	@Autowired
	ProductManagerService pmService;

	@Autowired
	ServletContext context;

	@Autowired
	DaoUtils utils;

	

	

	private static final Logger LOGGER = LoggerFactory.getLogger(ProductManagerController.class);

	final ObjectMapper objectMapper = new ObjectMapper();

	@PostMapping(value = "/user/create/idea")
	public ResponseEntity<ResponseMessage<Ideas>> createNewIdea(@RequestParam ("userID") String userID, @RequestParam("themeID") String themeID,@RequestParam("ideaName") String ideaName, @RequestParam("ideaDescription") String ideaDescription,
																@RequestParam(value = "files", required = false) MultipartFile [] files) throws  IOException, URISyntaxException {

		
		User user = utils.findByUserId(Long.parseLong(userID));
		Themes themes=utils.findThemeByID(Long.parseLong(themeID));
		
		List<ThemeIdeaFiles> thfList=new ArrayList<>();

		

		String userName=user.getUserName();
		
		String cpUserName=themes.getUser().getUserName();
		Ideas idea =new Ideas();
		
		Timestamp timestamp=Timestamp.valueOf(LocalDateTime.now());
	
		idea.setIdeaDate(timestamp);
		
		idea.setIdeaDescription(ideaDescription);
		idea.setTheme(themes);
		idea.setUser(user);
		

		ResponseMessage<Ideas> responseMessage = pmService.createNewIdeaResponseMessage(idea);

		String mainURL = null;
		String uploads_constant = null;

		
			mainURL = "http://" + domain + ":" + port + contextPath;
			uploads_constant = "Uploads" + File.separator + "Themes" + File.separator + cpUserName + File.separator +
					themeID + File.separator + "Ideas" + File.separator + userName + File.separator +
					responseMessage.getResult().getIdeaId() + File.separator + timestamp.getTime();
		
		
		if(files!=null) {
			for (MultipartFile myFile : files) {
				if (!myFile.isEmpty()) {
					
						boolean dirStatus = false;
						File dir = new File(context.getRealPath(uploads_constant));
						if (!dir.exists())
							dirStatus = dir.mkdirs();
						if (dirStatus)
							LOGGER.info("Directory created successfully");
						
					 
					}
					String fileName = myFile.getOriginalFilename();
					ThemeIdeaFiles thf = new ThemeIdeaFiles();

					thf.setIdeaId(idea);
					thf.setUser(user);
						thf.setThemeideaUrl(mainURL + File.separator + uploads_constant + File.separator + fileName);
					
					
					thf.setFileType(FilenameUtils.getExtension(fileName));
				

					thfList.add(thf);
				}
			
		}
		this.pmService.saveArtifacts(thfList,responseMessage.getResult().getIdeaId() );
		

		return new ResponseEntity<>(responseMessage, HttpStatus.valueOf(responseMessage.getStatus()));
	
	}
}
