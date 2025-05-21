package com.ssginc8.docto.user.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssginc8.docto.user.dto.AddUser;
import com.ssginc8.docto.user.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@RestController
public class UserApiController {
	private final UserService userService;

	@GetMapping("/users/check-email")
	public void checkEmail(@RequestParam(value = "email") String email) {
		userService.checkEmail(email);
	}

	@PostMapping("/users/register")
	public AddUser.Response signup(@Valid AddUser.Request request) {
		return AddUser.Response.builder()
			.userId(userService.createUser(request).getUserId())
			.build();
	}
}