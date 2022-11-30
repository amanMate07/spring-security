package com.auth.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.auth.dao.UserDAO;
import com.auth.entity.User;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	UserDAO userDAO;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userDAO.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("User not found with email : " + email));
		return user;
	}

	@Transactional
	public UserDetails loadUserById(Long id) {
		User user = userDAO.findById(id)
				.orElseThrow(() -> new UsernameNotFoundException("User not found with id : " + id));
		return user;
	}
}