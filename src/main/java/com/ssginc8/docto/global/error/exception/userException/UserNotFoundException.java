package com.ssginc8.docto.global.error.exception.userException;

import com.ssginc8.docto.global.error.ErrorCode;
import com.ssginc8.docto.global.error.exception.BusinessBaseException;

public class UserNotFoundException extends BusinessBaseException {
	public UserNotFoundException(ErrorCode errorCode) {
		super(errorCode);
	}

	public UserNotFoundException() {
		super(ErrorCode.USER_NOT_FOUND);
	}
}