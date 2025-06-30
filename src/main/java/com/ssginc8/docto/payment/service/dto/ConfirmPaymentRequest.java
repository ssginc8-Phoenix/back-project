package com.ssginc8.docto.payment.service.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

public class ConfirmPaymentRequest {
	@ToString
	@Getter
	@NoArgsConstructor
	public static class Request {
		private String paymentKey;
		private String orderId;
		private Long amount;
	}
}
