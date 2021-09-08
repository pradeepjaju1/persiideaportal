package com.ideaportal.datainteraction;

import java.util.List;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.ideaportal.models.ThemeIdeaFiles;
import com.ideaportal.models.Themes;
import com.ideaportal.repo.ThemeIdeaFilesRepository;
import com.ideaportal.repo.ThemesRepository;


@Repository
public class CpInteract {
	@Autowired
	ThemesRepository themeRepository;

	@Autowired
	DataInteract dataInteract;

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Autowired
	ThemeIdeaFilesRepository themeIdeaFilesRepository;
	
	public Themes saveTheme(Themes themes)
	{

		return themeRepository.save(themes);
	}

	

	public void saveFile(List<ThemeIdeaFiles> artifactList, long themeID) {
		Themes themes = themeRepository.findById(themeID).orElse(null);
		if (themes != null) {
			themes.setThemeFiles((List<ThemeIdeaFiles>) themeIdeaFilesRepository.saveAll(artifactList));
		}
	}

	
}