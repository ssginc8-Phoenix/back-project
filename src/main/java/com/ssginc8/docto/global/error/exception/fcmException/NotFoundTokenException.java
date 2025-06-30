package com.ssginc8.docto.global.error.exception.fcmException;

import com.ssginc8.docto.global.error.ErrorCode;
import com.ssginc8.docto.global.error.exception.BusinessBaseException;

public class NotFoundTokenException extends BusinessBaseException {
	public NotFoundTokenException(ErrorCode code) {
		super(code);
	}

	public NotFoundTokenException() {
		super(ErrorCode.NOT_FOUND_TOKEN);
	}
}
