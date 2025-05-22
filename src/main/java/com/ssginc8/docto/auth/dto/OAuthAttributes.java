package com.ssginc8.docto.auth.dto;

import com.ssginc8.docto.user.entity.LoginType;
import com.ssginc8.docto.user.entity.User;

import lombok.Getter;

@Getter
public class OAuthAttributes {
	private String providerId;
	private String email;
	private String name;
	private LoginType loginType;

	private OAuthAttributes(String providerId, String email, String name, LoginType loginType) {
		this.providerId = providerId;
		this.email = email;
		this.name = name;
		this.loginType = loginType;
	}

	public static OAuthAttributes oAuthAttributesByNaver(String providerId, String email, String name) {
		return new OAuthAttributes(providerId, email, name, LoginType.NAVER);
	}

	public static OAuthAttributes oAuthAttributesByKakao(String providerId, String email, String name) {
		return new OAuthAttributes(providerId, email, name, LoginType.KAKAO);
	}

	public User toEntity() {
		return User.createUserBySocial(this.providerId, this.name, this.email, this.loginType);
	}
}
