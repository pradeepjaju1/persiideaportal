package com.ideaportal.service;

import org.springframework.stereotype.Service;

import com.ideaportal.dto.LoginDto;
import com.ideaportal.dto.SignUpDto;
import com.ideaportal.response.ApiResponse;

@Service
public interface UserService {
	public ApiResponse signUp(SignUpDto signup);
	public ApiResponse login(LoginDto loginDto);
}
