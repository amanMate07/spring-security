package com.auth.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.auth.constant.RoleName;
import com.auth.dao.RoleDAO;
import com.auth.dao.UserDAO;
import com.auth.entity.Role;
import com.auth.entity.User;
import com.auth.model.LoginRequest;
import com.auth.security.JwtTokenProvider;

@Service
public class AuthImpl implements AuthService {

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private RoleDAO roleDAO;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public ResponseEntity<Map<String, Object>> signUp(@Valid User user) {
		Map<String, Object> map = new HashMap<>();

		if (userDAO.existsByEmail(user.getEmail())) {
			map.put("status", "User already exists");
			return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
		}

		user.setPassword(passwordEncoder.encode(user.getPassword()));

		Role userRole = roleDAO.findByName(RoleName.USER).orElseThrow(() -> new RuntimeException("User Role not set."));

		user.setRoles(Collections.singleton(userRole));

		userDAO.save(user);

		map.put("status", "User created");
		return new ResponseEntity<>(map,HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<?> login(@Valid LoginRequest loginRequest) {

		Map<String,Object> map = new HashMap<>();
		
		if(!userDAO.existsByEmail(loginRequest.getEmail()))
		{
			map.put("status", "Email not exists");
			return new ResponseEntity<>(map,HttpStatus.UNAUTHORIZED);
		}
				
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtTokenProvider.generateToken(authentication);
        map.put("status", "success");
        map.put("token", jwt);
        
		return new ResponseEntity<>(map,HttpStatus.OK);
	}

}
