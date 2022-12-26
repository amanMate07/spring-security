package com.auth.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionResponseHandler {
	
	@ExceptionHandler(value = {RuntimeException.class})
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Map<String,Object> expiredToken(RuntimeException ex)
    {
		Map<String,Object> map = new HashMap<>();
		map.put("status", "Token expired");
        return map;
    }
	
	@ExceptionHandler(value = {AccessDeniedException.class})
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Map<String,Object> badRequest(AccessDeniedException ex)
    {
		Map<String,Object> map = new HashMap<>();
		if(ex.getMessage().startsWith("JWT expired"))
			map.put("status", "Token expired");
		else
			map.put("status", "Authentication required");
        return map;
    }
	

}
