package com.ssginc8.docto.payment.service;

import com.ssginc8.docto.payment.service.dto.CreatePaymentRequest;
import com.ssginc8.docto.payment.service.dto.GetPaymentHistory;
import com.ssginc8.docto.payment.service.dto.UpdatePaymentInfo;

public interface PaymentService {
	GetPaymentHistory.Response getPaymentHistory(Long paymentRequestId);

	void createPaymentRequest(Long appointmentId, CreatePaymentRequest request);

	void updatePaymentRequestInfo(UpdatePaymentInfo.Request request);
}
