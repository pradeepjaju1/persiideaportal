package com.ideaportal.dao;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ideaportal.models.User;
import com.ideaportal.repo.UserRepository;

@Repository
public class DaoUtils {

	@Autowired
	UserRepository userRepo;
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
	
}
