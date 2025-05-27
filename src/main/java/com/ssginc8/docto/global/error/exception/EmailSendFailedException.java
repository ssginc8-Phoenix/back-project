package com.ssginc8.docto.global.error.exception;

import com.ssginc8.docto.global.error.ErrorCode;

public class EmailSendFailedException extends BusinessBaseException {
	public EmailSendFailedException(ErrorCode code) {
		super(code);
	}

	public EmailSendFailedException() {
		super(ErrorCode.EMAIL_SEND_FAILED);
	}
}
