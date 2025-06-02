package com.ssginc8.docto.global.error.exception.appointmentException;

import com.ssginc8.docto.global.error.ErrorCode;
import com.ssginc8.docto.global.error.exception.BusinessBaseException;

public class InvalidStatusValueException extends BusinessBaseException {
	public InvalidStatusValueException(ErrorCode errorCode) { super (errorCode);}

	public InvalidStatusValueException() {
		super(ErrorCode.INVALID_STATUS_VALUE);
	}
}
