package com.ssginc8.docto.global.error.exception.tokenException;

import com.ssginc8.docto.global.error.ErrorCode;
import com.ssginc8.docto.global.error.exception.BusinessBaseException;

public class UnauthorizedRequestException extends BusinessBaseException {
	public UnauthorizedRequestException(ErrorCode errorCode) {
		super(errorCode);
	}

	public UnauthorizedRequestException() {
		super(ErrorCode.UNAUTHORIZED_REQUEST);
	}
}
