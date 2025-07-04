package com.ssginc8.docto.global.event.appointment;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import com.ssginc8.docto.notification.service.NotificationService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AppointmentRequestEventListener {

	private final NotificationService notificationService;

	@Async
	@TransactionalEventListener
	public void handleAppointmentRequestEvent(AppointmentRequestEvent requestEvent) {

		notificationService.notifyAppointmentRequested(requestEvent.getAppointment());
	}
}
