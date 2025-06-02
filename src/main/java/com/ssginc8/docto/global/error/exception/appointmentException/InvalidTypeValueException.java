package com.ssginc8.docto.global.error.exception.appointmentException;

import com.ssginc8.docto.global.error.ErrorCode;
import com.ssginc8.docto.global.error.exception.BusinessBaseException;

public class InvalidTypeValueException extends BusinessBaseException {
	public InvalidTypeValueException(ErrorCode errorCode) { super (errorCode);}

	public InvalidTypeValueException() {
		super(ErrorCode.INVALID_TYPE_VALUE);
	}
}
