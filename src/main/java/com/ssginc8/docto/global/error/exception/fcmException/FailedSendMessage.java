package com.ssginc8.docto.global.error.exception.fcmException;

import com.ssginc8.docto.global.error.ErrorCode;
import com.ssginc8.docto.global.error.exception.BusinessBaseException;

public class FailedSendMessage extends BusinessBaseException {
	public FailedSendMessage(ErrorCode code) {
		super(code);
	}

	public FailedSendMessage() {
		super(ErrorCode.FAILED_SEND_MESSAGE);
	}
}
