package com.ssginc8.docto.global.error.exception.userException;

import com.ssginc8.docto.global.error.ErrorCode;
import com.ssginc8.docto.global.error.exception.BusinessBaseException;

public class EmailNotFoundException extends BusinessBaseException {
	public EmailNotFoundException(ErrorCode code) {
		super(code);
	}

	public EmailNotFoundException() {
		super(ErrorCode.EMAIL_NOT_FOUND);
	}
}
