package com.ssginc8.docto.user.service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class FindEmail {
	@Getter
	@NoArgsConstructor
	public static class Request {
		private String name;
		private String phone;
	}

	@Getter
	@NoArgsConstructor
	public static class Response {
		private String email;

		@Builder
		public Response(String email) {
			this.email = email;
		}
	}
}
