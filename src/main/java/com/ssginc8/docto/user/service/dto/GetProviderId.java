package com.ssginc8.docto.user.service.dto;

import lombok.Builder;
import lombok.Getter;

public class GetProviderId {
	@Getter
	public static class Response {
		private final String providerId;

		@Builder
		public Response(String providerId) {
			this.providerId = providerId;
		}
	}
}
