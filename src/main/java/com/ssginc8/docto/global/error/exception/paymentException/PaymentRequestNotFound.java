package com.ssginc8.docto.global.error.exception.paymentException;

import com.ssginc8.docto.global.error.ErrorCode;
import com.ssginc8.docto.global.error.exception.BusinessBaseException;

public class PaymentRequestNotFound extends BusinessBaseException {
	public PaymentRequestNotFound(ErrorCode errorCode) {
		super(errorCode);
	}

	public PaymentRequestNotFound() {
		super(ErrorCode.DUPLICATE_EMAIL);
	}
}

