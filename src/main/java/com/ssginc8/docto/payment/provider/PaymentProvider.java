package com.ssginc8.docto.payment.provider;

import com.ssginc8.docto.payment.entity.PaymentRequest;
import com.ssginc8.docto.payment.repository.PaymentRequestRepo;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PaymentProvider {
	private final PaymentRequestRepo paymentRequestRepo;

	public void save(PaymentRequest paymentRequest) {
		paymentRequestRepo.save(paymentRequest);
	}
}
