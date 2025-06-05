package com.ssginc8.docto.global.error.exception.userException;

import com.ssginc8.docto.global.error.ErrorCode;
import com.ssginc8.docto.global.error.exception.BusinessBaseException;

public class UserMismatchException extends BusinessBaseException {
	public UserMismatchException(ErrorCode errorCode) {
		super(errorCode);
	}

	public UserMismatchException() {
		super(ErrorCode.USER_NOT_FOUND);
	}
}