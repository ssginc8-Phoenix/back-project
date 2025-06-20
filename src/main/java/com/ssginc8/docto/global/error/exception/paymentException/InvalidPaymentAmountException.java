package com.ssginc8.docto.global.error.exception.paymentException;

import com.ssginc8.docto.global.error.ErrorCode;
import com.ssginc8.docto.global.error.exception.BusinessBaseException;

public class InvalidPaymentAmountException extends BusinessBaseException {
	public InvalidPaymentAmountException(ErrorCode errorCode) {
		super(errorCode);
	}

	public InvalidPaymentAmountException() {
		super(ErrorCode.INVALID_PAYMENT_AMOUNT);
	}
}

