package com.ssginc8.docto.user.service.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

public class CheckPassword {
	@Getter
	@NoArgsConstructor
	public static class Request {
		private String password;
	}
}
