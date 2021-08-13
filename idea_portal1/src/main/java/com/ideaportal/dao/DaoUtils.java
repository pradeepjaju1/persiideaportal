package com.ideaportal.dao;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ideaportal.models.Themes;
import com.ideaportal.models.User;
import com.ideaportal.repo.ThemesRepository;
import com.ideaportal.repo.UserRepository;

@Repository
public class DaoUtils {

	@Autowired
	UserRepository userRepo;
	
	@Autowired
	ThemesRepository themesRepository;
	public User findByUserId(final long userID) {
        User user = null;
        try {
            user = this.userRepo.findById(userID).orElse(null);
        }
        catch (NoSuchElementException exception) {
            return user;
        }
        return user;
    }
    public Themes findThemeByID( long themeID) {

		try
		{
			Themes themes = themesRepository.findById(themeID).orElse(null);
			
			return themes;
		}catch(NoSuchElementException e) {return null;}
    }
	
}
