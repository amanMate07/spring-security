package com.auth.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth.service.CustomUserDetailsService;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private JwtTokenProvider tokenProvider;

	@Autowired
	private CustomUserDetailsService customUserDetailsService;

	private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			String jwt = getJwtFromRequest(request);

			if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt, request)) {
				String email = tokenProvider.getEmailFromJWT(jwt);

//				Map<String, Object> map = tokenProvider.getDataFromJWT(jwt);
//				User user = new User();
//				user.setEmail(map.get("email").toString());
//				user.setUserId(Long.valueOf(map.get("userId").toString()));
//				user.setName(map.get("email").toString());
//				
//				Collection<? extends GrantedAuthority> authorities = Arrays
//						.stream(map.get("roles").toString().split(",")).map(role -> {
//							return new SimpleGrantedAuthority(role);
//						}).collect(Collectors.toList());

				UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);

				// UserDetails userDetails =customUserDetailsService.loadUserByUsername(email);

				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		} catch (Exception ex) {
			logger.error("Could not authentication", ex);
		}

		filterChain.doFilter(request, response);
	}

	private String getJwtFromRequest(HttpServletRequest request) {
		String token = request.getHeader("Authorization");
		if (token != null && token.startsWith("Bearer ")) {
			return token.substring(7, token.length());
		}
		return null;
	}
}