package com.ssginc8.docto.payment.provider;

import org.springframework.stereotype.Service;

import com.ssginc8.docto.payment.entity.PaymentRequest;
import com.ssginc8.docto.payment.repository.PaymentRequestRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentProvider {
	private final PaymentRequestRepo paymentRequestRepo;

	public PaymentRequest save(PaymentRequest paymentRequest) {
		return paymentRequestRepo.save(paymentRequest);
	}
}
