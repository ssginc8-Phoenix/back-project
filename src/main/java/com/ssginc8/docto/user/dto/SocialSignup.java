package com.ssginc8.docto.user.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

public class SocialSignup {
	@ToString
	@Getter
	@Setter
	@NoArgsConstructor
	public static class Request {
		private String providerId;
		private String phone;
		private MultipartFile profileImage;
		private String role;

		@Builder
		public Request(String providerId, String phone, MultipartFile profileImage, String role) {
			this.providerId = providerId;
			this.phone = phone;
			this.profileImage = profileImage;
			this.role = role;
		}
	}

	@Getter
	@NoArgsConstructor
	public static class Response {
		private Long userId;
		private String role;

		@Builder
		public Response(Long userId, String role) {
			this.userId = userId;
			this.role = role;
		}
	}
}
