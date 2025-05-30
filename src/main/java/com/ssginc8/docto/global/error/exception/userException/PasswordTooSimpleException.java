package com.ssginc8.docto.global.error.exception.userException;

import com.ssginc8.docto.global.error.ErrorCode;
import com.ssginc8.docto.global.error.exception.BusinessBaseException;

public class PasswordTooSimpleException extends BusinessBaseException {
	public PasswordTooSimpleException(ErrorCode code) {
		super(code);
	}

	public PasswordTooSimpleException() {
		super(ErrorCode.PASSWORD_TOO_SIMPLE);
	}
}

