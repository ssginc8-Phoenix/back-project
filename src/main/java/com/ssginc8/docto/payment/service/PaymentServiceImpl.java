package com.ssginc8.docto.payment.service;

import org.springframework.stereotype.Service;

import com.ssginc8.docto.payment.entity.PaymentRequest;
import com.ssginc8.docto.payment.provider.PaymentProvider;
import com.ssginc8.docto.payment.service.dto.CreatePaymentRequest;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PaymentServiceImpl implements PaymentService{
	private final PaymentProvider paymentProvider;

	@Override
	public void createPaymentRequest(Long appointmentId, CreatePaymentRequest request) {
		paymentProvider.save(PaymentRequest.create(appointmentId, request.getAmount()));
	}
}
