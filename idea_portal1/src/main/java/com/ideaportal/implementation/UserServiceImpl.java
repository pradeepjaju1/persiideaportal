package com.ideaportal.implementation;

import java.sql.ResultSet;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ideaportal.dto.LoginDto;
import com.ideaportal.dto.SignUpDto;
import com.ideaportal.models.Roles;
import com.ideaportal.models.User;
import com.ideaportal.repo.UserRepository;
import com.ideaportal.response.ApiResponse;
import com.ideaportal.service.UserService;
@Transactional
@Service
public class UserServiceImpl implements UserService{

	@Autowired
	JdbcTemplate jdbcTemplate;
	@Autowired
    private UserRepoImpl userRepo;
	@Autowired
	private UserRepository userRepository;

   @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
	@Override
	public ApiResponse signUp(SignUpDto signup) {
		Roles r1=null;
		signup.setUserPassword(passwordEncoder.encode(signup.getUserPassword()));
		if(signup.getDesignation().equals("Client Partner"))
			r1=new Roles(1,signup.getDesignation());
		else if(signup.getDesignation().equals("Project Manager"))
			r1=new Roles(2,signup.getDesignation());
		else
			r1=new Roles(3,signup.getDesignation());
		User user=new User(signup.getUserName(),signup.getUserEmail(),signup.getUserPassword(),signup.getCompanyName(),r1);
		BeanUtils.copyProperties(signup, user);
		userRepo.save(user);
		return new ApiResponse(200, "success", user);
	}

//	public String DataFetch(String userEmail)
//	{
//		return jdbcTemplate.execute("select u from User where u.userEmailId=:?", (PreparedStatementCallback<String>) ps -> {
//			ps.setString(1, userEmail);
//		
//			ResultSet resultSet=ps.executeQuery();
//
//			if(resultSet.next())
//				return resultSet.getString(1);
//		});
//	}
	@Override
	public ApiResponse login(LoginDto loginDto) {
		User user = userRepository.findByUserEmailId(loginDto.getUsername());
        if(user == null) {
            throw new RuntimeException("User does not exist.");
        }
        if(!user.getUserPassword().equals(passwordEncoder.encode(loginDto.getPassword()))){
            throw new RuntimeException("Password mismatch.");
        }
		return new ApiResponse(200,"login success",null);
	}

}
