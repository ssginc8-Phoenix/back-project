package com.ssginc8.docto.global.event.appointment;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import com.ssginc8.docto.appointment.entity.Appointment;
import com.ssginc8.docto.appointment.entity.AppointmentStatus;
import com.ssginc8.docto.notification.service.NotificationService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AppointmentStatusChangedEventListener {

	private final NotificationService notificationService;

	@Async
	@TransactionalEventListener
	public void handleAppointmentStatusChangedEvent(AppointmentStatusChangedEvent event) {
		Appointment appointment = event.getAppointment();

		if (appointment.getStatus() == AppointmentStatus.CONFIRMED) {
			notificationService.notifyAppointmentConfirmed(appointment);

		} else if (appointment.getStatus() == AppointmentStatus.CANCELED) {
			notificationService.notifyAppointmentCanceled(appointment);
		}
	}
}
