package com.ideaportal.repo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ideaportal.models.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
	
	
}