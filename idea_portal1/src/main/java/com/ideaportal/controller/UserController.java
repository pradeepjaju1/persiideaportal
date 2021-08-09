package com.ideaportal.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ideaportal.dto.LoginDto;
import com.ideaportal.dto.SignUpDto;
import com.ideaportal.response.ApiResponse;
import com.ideaportal.service.UserService;

@RestController
public class UserController {
	
	@Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ApiResponse signUp(@RequestBody SignUpDto signUpDto){
        return this.userService.signUp(signUpDto);
    }
    
    @GetMapping("/login")
    public ApiResponse login(@RequestBody LoginDto loginDto)
    {
    	return this.userService.login(loginDto);
    }
}
