package com.ssginc8.docto.payment.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssginc8.docto.payment.service.PaymentService;
import com.ssginc8.docto.payment.service.dto.CreatePaymentRequest;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/appointments")
public class PaymentApiController {
	private final PaymentService paymentService;

	@PostMapping("/{appointmentId}/payment-request")
	public void createPaymentRequest(@PathVariable("appointmentId") Long appointmentId,  @RequestBody CreatePaymentRequest request) {
		paymentService.createPaymentRequest(appointmentId, request);
	}
}
