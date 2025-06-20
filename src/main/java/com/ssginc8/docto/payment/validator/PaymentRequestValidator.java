package com.ssginc8.docto.payment.validator;

import java.util.Objects;

import org.springframework.stereotype.Component;

import com.ssginc8.docto.global.error.exception.paymentException.InvalidPaymentAmountException;

@Component
public class PaymentRequestValidator {

	public void validateAmount(Long actualAmount, Long expectedAmount) {
		if (!Objects.equals(actualAmount, expectedAmount)) {
			throw new InvalidPaymentAmountException();
		}
	}

}
