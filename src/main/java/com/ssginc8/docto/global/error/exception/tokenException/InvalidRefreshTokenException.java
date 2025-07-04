package com.ssginc8.docto.global.error.exception.tokenException;

import com.ssginc8.docto.global.error.ErrorCode;
import com.ssginc8.docto.global.error.exception.BusinessBaseException;

public class InvalidRefreshTokenException extends BusinessBaseException {
	public InvalidRefreshTokenException(ErrorCode errorCode) {
		super(errorCode);
	}

	public InvalidRefreshTokenException() {
		super(ErrorCode.INVALID_REFRESH_TOKEN);
	}
}
