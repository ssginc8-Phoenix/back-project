package com.ssginc8.docto.user.service.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class UpdateUser {
	@Getter
	@Setter
	@NoArgsConstructor
	public static class Request {
		private String name;
		private String email;
		private String phone;
		private String address;
		private MultipartFile profileImage;
	}
}
