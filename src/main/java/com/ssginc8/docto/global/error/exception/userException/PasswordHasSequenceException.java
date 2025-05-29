package com.ssginc8.docto.global.error.exception.userException;

import com.ssginc8.docto.global.error.ErrorCode;
import com.ssginc8.docto.global.error.exception.BusinessBaseException;

public class PasswordHasSequenceException extends BusinessBaseException {
	public PasswordHasSequenceException(ErrorCode code) {
		super(code);
	}

	public PasswordHasSequenceException() {
		super(ErrorCode.PASSWORD_HAS_SEQUENCE);
	}
}