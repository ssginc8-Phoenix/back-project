package com.ssginc8.docto.payment.service.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

public class UpdatePaymentInfo {
	@Getter
	@NoArgsConstructor
	public static class Request {
		private Long paymentRequestId;

		private String orderId;

		private String customerKey;
	}
}
