package com.ideaportal.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ideaportal.dto.LoginDto;
import com.ideaportal.dto.UserDto;
import com.ideaportal.models.Roles;
import com.ideaportal.models.User;
import com.ideaportal.repo.UserRepository;

@RestController
public class UserController {

	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@PostMapping("/signup")
	public ResponseEntity<String> registerUser(@Valid @RequestBody UserDto userDto)
	{
		Roles r1=null;
		userDto.setUserPassword(bCryptPasswordEncoder.encode(userDto.getUserPassword()));
		if(userDto.getDesignation().equals("Client Partner"))
			r1=new Roles(1,userDto.getDesignation());
		else if(userDto.getDesignation().equals("Project Manager"))
			r1=new Roles(2,userDto.getDesignation());
		else
			r1=new Roles(3,userDto.getDesignation());
		
		User user=new User(userDto.getUserName(),userDto.getUserEmail(),userDto.getUserPassword(),userDto.getCompanyName(),r1);
		
		if(this.userRepo.exists(Example.of(user)))
			return ResponseEntity.status(403).body("User Already present.Please Enter diffrent Email id");
		this.userRepo.save(user);
			return ResponseEntity.status(200).body("User Registered Successfully");
	}
	
	@GetMapping("/login")
	public ResponseEntity<String> loginUser(@RequestBody LoginDto userDetails)
	{
		return ResponseEntity.status(200).body("User Login Successfully!!");
	}
	
	
}
