package com.ssginc8.docto.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssginc8.docto.auth.jwt.dto.TokenType;
import com.ssginc8.docto.user.service.UserService;
import com.ssginc8.docto.user.service.dto.AddDoctorList;
import com.ssginc8.docto.user.service.dto.AddUser;
import com.ssginc8.docto.user.service.dto.FindEmail;
import com.ssginc8.docto.user.service.dto.Login;
import com.ssginc8.docto.user.service.dto.SocialSignup;
import com.ssginc8.docto.user.service.dto.UpdateUser;
import com.ssginc8.docto.user.service.dto.UserInfo;
import com.ssginc8.docto.util.CookieUtil;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@RestController
public class UserApiController {
	private final UserService userService;
	private final CookieUtil cookieUtil;

	@GetMapping("/users/me")
	public UserInfo.Response getMyInfo() {
		return userService.getMyInfo();
	}

	@GetMapping("/users/check-email")
	public void checkEmail(@RequestParam(value = "email") String email) {
		userService.checkEmail(email);
	}

	@GetMapping("/users/email/find")
	public FindEmail.Response findEmail(@RequestBody FindEmail.Request request) {
		return userService.findEmail(request);
	}

	@PostMapping("/users/register")
	public AddUser.Response signup(@Valid AddUser.Request request) {
		return userService.createUser(request);
	}

	@PostMapping("/users/social")
	public SocialSignup.Response signup(@Valid SocialSignup.Request request) {
		return userService.updateSocialInfo(request);
	}

	@PostMapping("/users/doctors")
	public AddDoctorList.Response registerDoctor(@RequestBody @Valid AddDoctorList.Request request) {
		return userService.registerDoctor(request);
	}

	@PostMapping("/auth/login")
	public ResponseEntity<String> login(@RequestBody Login.Request request, HttpServletResponse response) {
		Login.Response loginResponse = userService.login(request);

		response.addCookie(
			cookieUtil.createCookie(
				TokenType.ACCESS_TOKEN.getTokenType(), loginResponse.getAccessToken(),
				loginResponse.getAccessTokenCookieMaxAge()
			));

		response.addCookie(
			cookieUtil.createCookie(
				TokenType.REFRESH_TOKEN.getTokenType(), loginResponse.getRefreshToken(),
				loginResponse.getRefreshTokenCookieMaxAge()
			));

		return ResponseEntity.ok("로그인 성공");
	}

	@PatchMapping("/users/me")
	public ResponseEntity<Void> updateInfo(UpdateUser.Request request) {
		userService.updateInfo(request);

		return ResponseEntity.noContent().build();
	}
}