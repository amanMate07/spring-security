package com.auth.service;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;

import com.auth.entity.User;
import com.auth.model.LoginRequest;

public interface AuthService {

	ResponseEntity<Map<String, Object>> signUp(@Valid User user);

	ResponseEntity<?> login(@Valid LoginRequest loginRequest);

}
