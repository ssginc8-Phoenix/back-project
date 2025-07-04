package com.ssginc8.docto.global.error.exception.tokenException;

import com.ssginc8.docto.global.error.ErrorCode;
import com.ssginc8.docto.global.error.exception.BusinessBaseException;

public class ForbiddenRequestException extends BusinessBaseException {
	public ForbiddenRequestException(ErrorCode errorCode) {
		super(errorCode);
	}

	public ForbiddenRequestException() {
		super(ErrorCode.UNAUTHORIZED_REQUEST);
	}
}
