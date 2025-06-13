package com.ssginc8.docto.fcm.dto;

import lombok.Getter;

public class AddFcmToken {

	@Getter
	public static class Request {

		private Long userId;
		private String token;
	}
}
