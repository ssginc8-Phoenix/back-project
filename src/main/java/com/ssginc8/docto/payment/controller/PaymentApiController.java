package com.ssginc8.docto.payment.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssginc8.docto.payment.service.PaymentService;
import com.ssginc8.docto.payment.service.dto.ConfirmPaymentRequest;
import com.ssginc8.docto.payment.service.dto.CreatePaymentRequest;
import com.ssginc8.docto.payment.service.dto.GetPaymentHistory;
import com.ssginc8.docto.payment.service.dto.UpdatePaymentInfo;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class PaymentApiController {
	private final PaymentService paymentService;

	/**
	 * ✅ 결제 내역 조회
	 * URL: /api/v1/payment-history/{paymentRequestId}
	 * Method: GET
	 */
	@GetMapping("/payment-history/{paymentRequestId}")
	public GetPaymentHistory.Response getPaymentHistory(@PathVariable("paymentRequestId") Long paymentRequestId) {
		return paymentService.getPaymentHistory(paymentRequestId);
	}

	/**
	 * ✅ 결제 요청 생성
	 * URL: /api/v1/appointments/{appointmentId}/payment-request
	 * Method: POST
	 */
	@PostMapping("/appointments/{appointmentId}/payment-request")
	public void createPaymentRequest(@PathVariable("appointmentId") Long appointmentId,
		@RequestBody CreatePaymentRequest request) {
		paymentService.createPaymentRequest(appointmentId, request);
	}

	/**
	 * ✅ 결제 정보 확인
	 * URL: /api/v1/payments/init
	 * Method: POST
	 */
	@PostMapping("/payments/confirm")
	public void confirmPaymentRequest(@RequestBody ConfirmPaymentRequest.Request request) {
		paymentService.confirmPaymentRequest(request);
	}

	/**
	 * ✅ 결제 내역 정보 업데이트
	 * URL: /api/v1/payments/init
	 * Method: PATCH
	 */
	@PatchMapping("/payments/init")
	public void updatePaymentInfo(@RequestBody UpdatePaymentInfo.Request request) {
		paymentService.updatePaymentRequestInfo(request);
	}


}
