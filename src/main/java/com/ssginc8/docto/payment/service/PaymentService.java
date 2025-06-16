package com.ssginc8.docto.payment.service;

import com.ssginc8.docto.payment.service.dto.CreatePaymentRequest;

public interface PaymentService {
	void createPaymentRequest(Long appointmentId, CreatePaymentRequest request);
}
