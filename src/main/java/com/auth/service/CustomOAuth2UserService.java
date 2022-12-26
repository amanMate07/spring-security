package com.auth.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.auth.constant.AuthProvider;
import com.auth.dao.UserDAO;
import com.auth.entity.User;
import com.auth.exception.OAuth2AuthenticationProcessingException;
import com.auth.model.CustomOAuth2User;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

	@Autowired
	private UserDAO userDAO;
	
	@Override
	public CustomOAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2User user = super.loadUser(userRequest);
		try {
			return processOAuth2User(userRequest,user);
		} catch (OAuth2AuthenticationProcessingException e) {
			throw new OAuth2AuthenticationException(e.getMessage());
		}
	}

	private CustomOAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) throws OAuth2AuthenticationProcessingException {
		CustomOAuth2User oauthUser = new CustomOAuth2User(oAuth2User);
		String email = oauthUser.getEmail();
		
		if (email==null || email.isBlank()) {
			throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
		}

		Optional<User> userOptional = userDAO.findByEmail(email);
		if (userOptional.isPresent()) {
			User user = userOptional.get();
			if (!user.getProvider().equals(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()))) {
				throw new OAuth2AuthenticationProcessingException("Looks like you're signed up with " + user.getProvider() + " account. Please use your "+ user.getProvider() + " account to login.");
			}
			updateExistingUser(user, oauthUser);
		} else {
			registerNewUser(oAuth2UserRequest, oauthUser);
		}

		return oauthUser;
	}

	private void registerNewUser(OAuth2UserRequest oAuth2UserRequest, CustomOAuth2User oauthUser) {
		User user = new User();

		user.setProvider(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()));
		user.setName(oauthUser.getName());
		user.setEmail(oauthUser.getEmail());
		user.setPassword("UUID");
		userDAO.save(user);
	}

	private void updateExistingUser(User existingUser, CustomOAuth2User oauthUser) {
		existingUser.setName(oauthUser.getName());
		userDAO.save(existingUser);
	}

}
