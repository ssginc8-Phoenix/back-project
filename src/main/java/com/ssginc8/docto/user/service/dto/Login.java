package com.ssginc8.docto.user.service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class Login {
	@Getter
	@NoArgsConstructor
	public static class Request {
		private String email;
		private String password;
	}

	@Getter
	@NoArgsConstructor
	public static class Response {
		private String accessToken;
		private String refreshToken;
		private int accessTokenCookieMaxAge;
		private int refreshTokenCookieMaxAge;

		@Builder
		private Response(String accessToken, String refreshToken, int accessTokenCookieMaxAge,
			int refreshTokenCookieMaxAge) {
			this.accessToken = accessToken;
			this.refreshToken = refreshToken;
			this.accessTokenCookieMaxAge = accessTokenCookieMaxAge;
			this.refreshTokenCookieMaxAge = refreshTokenCookieMaxAge;
		}
	}
}

