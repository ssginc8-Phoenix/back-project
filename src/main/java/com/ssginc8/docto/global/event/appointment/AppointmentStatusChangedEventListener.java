package com.ssginc8.docto.global.event.appointment;

import java.util.Optional;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import com.ssginc8.docto.appointment.entity.Appointment;
import com.ssginc8.docto.appointment.entity.AppointmentStatus;
import com.ssginc8.docto.appointment.provider.AppointmentProvider;
import com.ssginc8.docto.notification.service.NotificationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Component
@RequiredArgsConstructor
@Log4j2
public class AppointmentStatusChangedEventListener {

	private final NotificationService notificationService;
	private final AppointmentProvider appointmentProvider;

	@Async
	@TransactionalEventListener
	public void handleAppointmentStatusChangedEvent(AppointmentStatusChangedEvent event) {
		Appointment originalAppointment = event.getAppointment();
		log.info("예약 상태 변경 이벤트 발생: {}", originalAppointment.getStatus());

		Optional<Appointment> optionalFetched = appointmentProvider.findByIdWithUser(originalAppointment.getAppointmentId());

		if (optionalFetched.isEmpty()) {
			log.warn("fetch join 으로 Appointment를 찾을 수 없습니다.");
			return;
		}

		Appointment appointment = optionalFetched.get();

		try {
			if (appointment.getStatus() == AppointmentStatus.CONFIRMED) {
				log.info("예약 확정 알림 호출 시작");
				notificationService.notifyAppointmentConfirmed(appointment);

			} else if (appointment.getStatus() == AppointmentStatus.CANCELED) {
				log.info("예약 취소 알림 호출 시작");
				notificationService.notifyAppointmentCanceled(appointment);
			}
		} catch (Exception e) {
			log.error("알림 전송 중 예외 발생", e);
		}
	}
}
