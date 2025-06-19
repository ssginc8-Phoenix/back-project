package com.ssginc8.docto.global.error.exception.paymentException;

import com.ssginc8.docto.global.error.ErrorCode;
import com.ssginc8.docto.global.error.exception.BusinessBaseException;

public class PaymentRequestNotFoundException extends BusinessBaseException {
	public PaymentRequestNotFoundException(ErrorCode errorCode) {
		super(errorCode);
	}

	public PaymentRequestNotFoundException() {
		super(ErrorCode.PAYMENT_REQUEST_NOT_FOUND);
	}
}

