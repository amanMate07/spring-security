package com.auth.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth.entity.User;
import com.auth.model.LoginRequest;
import com.auth.service.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private AuthService authService;
	
	@PostMapping("/signin")
	public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest)
	{
		return authService.login(loginRequest);
	}
	
	@PostMapping("/signup")
	public ResponseEntity<?> signup(@Valid @RequestBody User user)
	{
		return authService.signUp(user);
	}
 }
