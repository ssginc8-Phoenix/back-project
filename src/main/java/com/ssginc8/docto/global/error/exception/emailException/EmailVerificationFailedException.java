package com.ssginc8.docto.global.error.exception.emailException;

import com.ssginc8.docto.global.error.ErrorCode;
import com.ssginc8.docto.global.error.exception.BusinessBaseException;

public class EmailVerificationFailedException extends BusinessBaseException {
	public EmailVerificationFailedException(ErrorCode code) {
		super(code);
	}

	public EmailVerificationFailedException() {
		super(ErrorCode.EMAIL_VERIFICATION_FAILED);
	}
}
