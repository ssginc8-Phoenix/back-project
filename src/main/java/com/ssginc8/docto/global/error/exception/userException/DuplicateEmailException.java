package com.ssginc8.docto.global.error.exception.userException;

import com.ssginc8.docto.global.error.ErrorCode;
import com.ssginc8.docto.global.error.exception.BusinessBaseException;

public class DuplicateEmailException extends BusinessBaseException {
	public DuplicateEmailException(ErrorCode errorCode) {
		super(errorCode);
	}

	public DuplicateEmailException() {
		super(ErrorCode.DUPLICATE_EMAIL);
	}
}
