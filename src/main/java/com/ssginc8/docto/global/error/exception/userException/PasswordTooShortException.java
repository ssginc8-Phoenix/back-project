package com.ssginc8.docto.global.error.exception.userException;

import com.ssginc8.docto.global.error.ErrorCode;
import com.ssginc8.docto.global.error.exception.BusinessBaseException;

public class PasswordTooShortException extends BusinessBaseException {
	public PasswordTooShortException(ErrorCode code) {
		super(code);
	}

	public PasswordTooShortException() {
		super(ErrorCode.PASSWORD_TOO_SHORT);
	}
}
