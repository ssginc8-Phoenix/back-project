package com.ssginc8.docto.payment.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssginc8.docto.appointment.entity.Appointment;
import com.ssginc8.docto.appointment.provider.AppointmentProvider;
import com.ssginc8.docto.notification.service.NotificationService;
import com.ssginc8.docto.payment.entity.PaymentRequest;
import com.ssginc8.docto.payment.provider.PaymentProvider;
import com.ssginc8.docto.payment.service.dto.CreatePaymentRequest;
import com.ssginc8.docto.payment.service.dto.GetPaymentHistory;
import com.ssginc8.docto.payment.service.dto.UpdatePaymentInfo;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PaymentServiceImpl implements PaymentService{
	private final AppointmentProvider appointmentProvider;
	private final PaymentProvider paymentProvider;
	private final NotificationService notificationService;

	@Transactional(readOnly = true)
	@Override
	public GetPaymentHistory.Response getPaymentHistory(Long paymentRequestId) {
		PaymentRequest paymentRequest = paymentProvider.findPaymentRequestByPaymentRequestId(paymentRequestId);

		Appointment appointment = appointmentProvider.getAppointmentById(paymentRequest.getAppointmentId());

		return GetPaymentHistory.toResponse(appointment, paymentRequest);
	}

	@Transactional
	@Override
	public void createPaymentRequest(Long appointmentId, CreatePaymentRequest request) {
		Appointment appointment = appointmentProvider.getAppointmentById(appointmentId);

		PaymentRequest paymentRequest = paymentProvider.save(PaymentRequest.create(appointmentId, request.getAmount()));

		notificationService.notifyPaymentRequest(appointment, paymentRequest.getPaymentRequestId());
	}

	@Transactional
	@Override
	public void updatePaymentRequestInfo(UpdatePaymentInfo.Request request) {
		PaymentRequest paymentRequest = paymentProvider.findPaymentRequestByPaymentRequestId(
			request.getPaymentRequestId());

		paymentRequest.updatePaymentInfo(request.getOrderId(), request.getCustomerKey());
	}
}
