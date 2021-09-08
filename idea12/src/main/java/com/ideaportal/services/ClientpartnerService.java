package com.ideaportal.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.ideaportal.controller.ClientPartnerController;
import com.ideaportal.datainteraction.CpInteract;
import com.ideaportal.models.Rp;
import com.ideaportal.models.ThemeIdeaFiles;
import com.ideaportal.models.Themes;

import jdk.internal.org.jline.utils.Log;



@Service
public class ClientpartnerService {
	
	@Autowired
     CpInteract cpInteract;
	
	private static final Logger LOG = LoggerFactory.getLogger(ClientPartnerController.class);
	
	public void saveFiles(List<ThemeIdeaFiles> artifactList, long themeID) {
		LOG.info("Saving Files of Themes");
		cpInteract.saveFile(artifactList, themeID);
	}
	public Rp<Themes> saveTheme(Themes themes)
	{
		Rp<Themes> rpm=new Rp<>();
		rpm.setResult(cpInteract.saveTheme(themes));
		rpm.setStatus(HttpStatus.CREATED.value());
		rpm.setStatusText("theme_created");
		rpm.setTotalElements(1);
		return rpm;
	}

}
