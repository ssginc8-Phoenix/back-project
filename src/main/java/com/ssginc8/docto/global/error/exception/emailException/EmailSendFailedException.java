package com.ssginc8.docto.global.error.exception.emailException;

import com.ssginc8.docto.global.error.ErrorCode;
import com.ssginc8.docto.global.error.exception.BusinessBaseException;

public class EmailSendFailedException extends BusinessBaseException {
	public EmailSendFailedException(ErrorCode code) {
		super(code);
	}

	public EmailSendFailedException() {
		super(ErrorCode.EMAIL_SEND_FAILED);
	}
}
