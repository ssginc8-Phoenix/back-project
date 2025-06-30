package com.ssginc8.docto.global.error.exception.appointmentException;

import com.ssginc8.docto.global.error.ErrorCode;
import com.ssginc8.docto.global.error.exception.BusinessBaseException;

public class IsSuspendedUserException extends BusinessBaseException {
	public IsSuspendedUserException(ErrorCode errorCode) { super (errorCode);}

	public IsSuspendedUserException() {super(ErrorCode.IS_SUSPENDED_USER);
	}
}
