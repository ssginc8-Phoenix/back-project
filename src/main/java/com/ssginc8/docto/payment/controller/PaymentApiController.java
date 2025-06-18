package com.ssginc8.docto.payment.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssginc8.docto.payment.service.PaymentService;
import com.ssginc8.docto.payment.service.dto.CreatePaymentRequest;
import com.ssginc8.docto.payment.service.dto.GetPaymentHistory;
import com.ssginc8.docto.payment.service.dto.UpdatePaymentInfo;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class PaymentApiController {
	private final PaymentService paymentService;

	@GetMapping("/payment-history/{paymentRequestId}")
	public GetPaymentHistory.Response getPaymentHistory(@PathVariable("paymentRequestId") Long paymentRequestId) {
		return paymentService.getPaymentHistory(paymentRequestId);
	}

	@PostMapping("/appointments/{appointmentId}/payment-request")
	public void createPaymentRequest(@PathVariable("appointmentId") Long appointmentId,
		@RequestBody CreatePaymentRequest request) {
		paymentService.createPaymentRequest(appointmentId, request);
	}

	@PatchMapping("/payments/init")
	public void updatePaymentInfo(@RequestBody UpdatePaymentInfo.Request request) {
		paymentService.updatePaymentRequestInfo(request);
	}
}
