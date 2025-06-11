package com.ssginc8.docto.notification.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssginc8.docto.appointment.entity.Appointment;
import com.ssginc8.docto.fcm.service.FirebaseCloudMessageService;
import com.ssginc8.docto.notification.dto.NotificationResponse;
import com.ssginc8.docto.notification.entity.Notification;
import com.ssginc8.docto.notification.entity.NotificationType;
import com.ssginc8.docto.notification.provider.NotificationProvider;
import com.ssginc8.docto.qna.entity.QaComment;
import com.ssginc8.docto.user.entity.User;
import com.ssginc8.docto.user.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationServiceImpl implements NotificationService {

	private final NotificationProvider notificationProvider;
	private final UserService userService;
	private final FirebaseCloudMessageService fcmService;

	/**
	 * 알림 읽음
	 * @param notificationId
	 */
	@Transactional
	@Override
	public void markAsRead(Long notificationId) {
		Notification notification = notificationProvider.findById(notificationId);
		notification.markAsRead();
	}

	@Override
	public List<NotificationResponse> getNotificationsByLoginUser() {
		// 1. 로그인한 사용자 가져오기
		User loginUser = userService.getUserFromUuid();

		return notificationProvider.getUserNotifications(loginUser.getUserId())
			.stream()
			.map(NotificationResponse::from)
			.toList();
	}

	/**
	 * Notification 생성 메서드 (재사용을 위한)
	 */
	private void createNotification(User receiver, NotificationType type, String content, Long referenceId) {
		Notification notification = new Notification(
			receiver,
			type,
			content,
			referenceId
		);

		notificationProvider.save(notification);
		fcmService.sendMessage(receiver.getUserId(), type.name(), content);
	}

	/**
	 * Appointment 확정 알림 전송 (보호자)
	 */
	public void notifyAppointmentConfirmed(Appointment appointment) {
		User receiver = appointment.getPatientGuardian().getUser();

		String hospitalName = appointment.getHospital().getName();
		String patientName = appointment.getPatientGuardian().getPatient().getUser().getName();
		String time = appointment.getAppointmentTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

		String content = String.format("%s %s의 %s 환자의 예약이 확정되었습니다.", time, hospitalName, patientName);

		createNotification(receiver, NotificationType.APPOINTMENT_CONFIRMED, content, appointment.getAppointmentId());
	}

	/**
	 * Appointment 취소 알림 전송 (보호자)
	 */
	@Override
	public void notifyAppointmentCanceled(Appointment appointment) {
		User receiver = appointment.getPatientGuardian().getUser();

		String hospitalName = appointment.getHospital().getName();
		String patientName = appointment.getPatientGuardian().getPatient().getUser().getName();
		String time = appointment.getAppointmentTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

		String content = String.format("%s %s의 %s 환자의 예약이 취소되었습니다.", time, hospitalName, patientName);

		createNotification(receiver, NotificationType.APPOINTMENT_CANCELED, content, appointment.getAppointmentId());
	}

	@Override
	public void notifyQnaResponse(QaComment qaComment) {

	}
}
