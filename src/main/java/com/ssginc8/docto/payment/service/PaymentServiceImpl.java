package com.ssginc8.docto.payment.service;

import org.springframework.stereotype.Service;

import com.ssginc8.docto.appointment.entity.Appointment;
import com.ssginc8.docto.appointment.provider.AppointmentProvider;
import com.ssginc8.docto.fcm.service.FirebaseCloudMessageService;
import com.ssginc8.docto.notification.service.NotificationService;
import com.ssginc8.docto.payment.entity.PaymentRequest;
import com.ssginc8.docto.payment.provider.PaymentProvider;
import com.ssginc8.docto.payment.service.dto.CreatePaymentRequest;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PaymentServiceImpl implements PaymentService{
	private final AppointmentProvider appointmentProvider;
	private final PaymentProvider paymentProvider;
	private final NotificationService notificationService;

	@Override
	public void createPaymentRequest(Long appointmentId, CreatePaymentRequest request) {
		Appointment appointment = appointmentProvider.getAppointmentById(appointmentId);

		PaymentRequest paymentRequest = paymentProvider.save(PaymentRequest.create(appointmentId, request.getAmount()));

		notificationService.notifyPaymentRequest(appointment, paymentRequest.getPaymentRequestId());
	}
}
