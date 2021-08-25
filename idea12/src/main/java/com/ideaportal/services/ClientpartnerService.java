package com.ideaportal.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.ideaportal.dao.ClientPartnerDAO;
import com.ideaportal.models.Rp;
import com.ideaportal.models.ThemeIdeaFiles;
import com.ideaportal.models.Themes;



@Service
public class ClientpartnerService {
	
	@Autowired
     ClientPartnerDAO clientPartnerDAO;

	public void saveArtifacts(List<ThemeIdeaFiles> artifactList, long themeID) {
		clientPartnerDAO.saveArtifacts(artifactList, themeID);
	}
	public Rp<Themes> saveTheme(Themes themes)
	{
		Rp<Themes> rpm=new Rp<>();
		rpm.setResult(clientPartnerDAO.saveTheme(themes));
		rpm.setStatus(HttpStatus.CREATED.value());
		rpm.setStatusText("theme_created");
		rpm.setTotalElements(1);
		return rpm;
	}

}
