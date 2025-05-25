package com.ssginc8.docto.user.service.dto;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
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

		@NotBlank(message = "휴대폰 번호는 비어있을 수 없습니다.")
		private String phone;

		private MultipartFile profileImage;

		@NotBlank(message = "역할은 비어있을 수 없습니다.")
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
