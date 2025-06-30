package com.ssginc8.docto.global.error.exception.tokenException;

import com.ssginc8.docto.global.error.ErrorCode;
import com.ssginc8.docto.global.error.exception.BusinessBaseException;

public class InvalidAccessTokenException extends BusinessBaseException {

	public InvalidAccessTokenException(ErrorCode errorCode) {
		super(errorCode);
	}

	public InvalidAccessTokenException() {
		super(ErrorCode.INVALID_ACCESS_TOKEN);
	}
}

