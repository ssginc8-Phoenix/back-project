package com.ssginc8.docto.global.error.exception.userException;

import com.ssginc8.docto.global.error.ErrorCode;
import com.ssginc8.docto.global.error.exception.BusinessBaseException;

public class SameAsPreviousPasswordException extends BusinessBaseException {
	public SameAsPreviousPasswordException(ErrorCode code) {
		super(code);
	}

	public SameAsPreviousPasswordException() {
		super(ErrorCode.SAME_AS_PREVIOUS_PASSWORD);
	}
}

