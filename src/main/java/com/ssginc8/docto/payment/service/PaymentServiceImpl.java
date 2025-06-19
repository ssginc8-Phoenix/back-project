package com.ssginc8.docto.payment.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssginc8.docto.appointment.entity.Appointment;
import com.ssginc8.docto.appointment.provider.AppointmentProvider;
import com.ssginc8.docto.notification.service.NotificationService;
import com.ssginc8.docto.payment.entity.PaymentRequest;
import com.ssginc8.docto.payment.provider.PaymentProvider;
import com.ssginc8.docto.payment.service.dto.ConfirmPaymentRequest;
import com.ssginc8.docto.payment.service.dto.CreatePaymentRequest;
import com.ssginc8.docto.payment.service.dto.GetPaymentHistory;
import com.ssginc8.docto.payment.service.dto.UpdatePaymentInfo;
import com.ssginc8.docto.payment.validator.PaymentRequestValidator;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
@Service
public class PaymentServiceImpl implements PaymentService{
	private final AppointmentProvider appointmentProvider;
	private final PaymentProvider paymentProvider;
	private final NotificationService notificationService;
	private final PaymentRequestValidator paymentRequestValidator;

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
	public void confirmPaymentRequest(ConfirmPaymentRequest.Request request) {
		PaymentRequest paymentRequest = paymentProvider.findPaymentRequestByOrderId(request.getOrderId());

		paymentRequestValidator.validateAmount(request.getAmount(), paymentRequest.getAmount());

		paymentRequest.updateStatus();
	}

	@Transactional
	@Override
	public void updatePaymentRequestInfo(UpdatePaymentInfo.Request request) {
		PaymentRequest paymentRequest = paymentProvider.findPaymentRequestByPaymentRequestId(
			request.getPaymentRequestId());

		paymentRequest.updatePaymentInfo(request.getOrderId(), request.getCustomerKey());
	}
}
