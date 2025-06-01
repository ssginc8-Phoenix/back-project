package com.ssginc8.docto.user.service.dto;

import lombok.Builder;

public class GetProvider {
	public static class Response {
		private final String providerId;

		@Builder
		public Response(String providerId) {
			this.providerId = providerId;
		}
	}
}
