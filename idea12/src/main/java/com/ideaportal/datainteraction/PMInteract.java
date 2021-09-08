package com.ideaportal.datainteraction;

import java.sql.ResultSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.stereotype.Repository;

import com.ideaportal.models.Ideas;
import com.ideaportal.models.ThemeIdeaFiles;
import com.ideaportal.repo.IdeasRepository;
import com.ideaportal.repo.ThemeIdeaFilesRepository;

@Repository
public class PMInteract {
	
	@Autowired
	IdeasRepository ideasRepository;
	@Autowired
	ThemeIdeaFilesRepository themeIdeaFilesRepository;
	@Autowired
	JdbcTemplate jdbcTemplate;
	@Autowired
	DataInteract datainteract;
	@Autowired
	IdeasRepository ideaRepo;
	
	
	public Ideas createNewIdeaObject(Ideas ideas)
	{

		return ideasRepository.save(ideas);
	}
	public void saveFile(List<ThemeIdeaFiles> thflist, long ideaID) {
		Ideas ideas = ideasRepository.findById(ideaID).orElse(null);
		if (ideas != null) {
			ideas.setIdeaFiles(thflist);
			ideaRepo.save(ideas);
		}
	}
	public List<Ideas> getMyIdeasList(long userID) 
	{
		return jdbcTemplate.execute("select idea_id from ideas where user_id=?", (PreparedStatementCallback<List<Ideas>>) ps -> {
			ps.setLong(1, userID);

			ResultSet resultSet=ps.executeQuery();

			return datainteract.buildList(resultSet);
		});
	}
	
}
