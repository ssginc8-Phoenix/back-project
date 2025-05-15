package com.ssginc8.docto.global.error;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class ErrorResponse {
	private String message;
	private String code;

	private ErrorResponse(final ErrorCode code) {
		this.message = code.getMessage();
		this.code = code.getCode();
	}

	private ErrorResponse(final ErrorCode code, final String message) {
		this.message = message;
		this.code = code.getCode();
	}

	public static ErrorResponse of(final ErrorCode code) {
		return new ErrorResponse(code);
	}
}
