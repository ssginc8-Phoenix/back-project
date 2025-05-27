package com.ssginc8.docto.user.service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class VerifyEmail {
	@Getter
	@Setter
	@NoArgsConstructor
	public static class Request {
		@Email
		@NotBlank(message = "이메일은 비어있을 수 없습니다.")
		private String email;
	}
}
