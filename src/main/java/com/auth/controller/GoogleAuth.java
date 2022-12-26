package com.auth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GoogleAuth {

	@GetMapping("/login/oauth2/code/google")
	public void login()
	{
		System.out.println("in");
	}
}
