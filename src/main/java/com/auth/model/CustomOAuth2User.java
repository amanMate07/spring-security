package com.auth.model;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class CustomOAuth2User implements OAuth2User {

	private OAuth2User user;

	public CustomOAuth2User(OAuth2User user) {
		this.user = user;
	}

	@Override
	public Map<String, Object> getAttributes() {
		return user.getAttributes();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return user.getAuthorities();
	}
	
	public String getId() {
		return (String) getAttributes().get("sub");
	}

	public String getName() {
		return (String) getAttributes().get("name");
	}

	public String getEmail() {
		return (String) getAttributes().get("email");
	}

	public String getImageUrl() {
		return (String) getAttributes().get("picture");
	}

}
