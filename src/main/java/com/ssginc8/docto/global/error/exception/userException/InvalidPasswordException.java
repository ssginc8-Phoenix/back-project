package com.ssginc8.docto.global.error.exception.userException;

import com.ssginc8.docto.global.error.ErrorCode;
import com.ssginc8.docto.global.error.exception.BusinessBaseException;

public class InvalidPasswordException extends BusinessBaseException {

	public InvalidPasswordException(ErrorCode code) {
		super(code);
	}

	public InvalidPasswordException() {
		super(ErrorCode.INVALID_PASSWORD);
	}
}