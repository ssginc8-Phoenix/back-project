package com.ssginc8.docto.user.dto;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

public class AddUser {
	@Getter
	@Setter
	@NoArgsConstructor
	@ToString
	public static class Request {
		@Email(message = "이메일 형식에 맞지 않습니다.")
		@NotBlank(message = "이메일은 비어있을 수 없습니다.")
		private String email;

		@NotBlank(message = "패스워드는 비어있을 수 없습니다.")
		private String password;

		@NotBlank(message = "이름은 비어있을 수 없습니다.")
		private String name;

		private MultipartFile profileImage;

		private String role;

		// 환자만 주민번호 필요
		private String residentRegistrationNumber;

		private String phone;

		private String address;
	}

	@Getter
	public static class Response {
		private Long userId;

		@Builder
		public Response(Long userId) {
			this.userId = userId;
		}
	}
}
