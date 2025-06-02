package com.ssginc8.docto.global.error.exception.appointmentException;

import com.ssginc8.docto.global.error.ErrorCode;
import com.ssginc8.docto.global.error.exception.BusinessBaseException;

public class InvalidPaymentValueException extends BusinessBaseException {
	public InvalidPaymentValueException(ErrorCode errorCode) { super (errorCode);}

	public InvalidPaymentValueException() {
		super(ErrorCode.INVALID_PAYMENT_VALUE);
	}
}
