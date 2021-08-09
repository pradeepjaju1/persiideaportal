package com.ideaportal.repo;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

import com.ideaportal.dto.LoginDto;
import com.ideaportal.models.User;

@Repository
public interface UserRepository extends JpaRepository<User,Long>{
	@Query("select u from User u where u.userEmailId=:useremail")
	public User findByUserEmailId(String useremail);
}