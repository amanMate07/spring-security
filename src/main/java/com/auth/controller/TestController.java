package com.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

	@PostMapping("/test")
	public ResponseEntity<?> log1in()
	{
		return new ResponseEntity<>("ji",HttpStatus.OK);
	}
	
}
