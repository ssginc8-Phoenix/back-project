package com.ssginc8.docto.auth.jwt.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TokenType {
	ACCESS_TOKEN("accessToken"),
	REFRESH_TOKEN("refreshToken");

	private final String tokenType;
}
