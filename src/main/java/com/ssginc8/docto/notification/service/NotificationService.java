package com.ssginc8.docto.notification.service;

import java.time.LocalTime;
import java.util.List;

import com.ssginc8.docto.appointment.entity.Appointment;
import com.ssginc8.docto.guardian.entity.PatientGuardian;
import com.ssginc8.docto.notification.dto.NotificationResponse;
import com.ssginc8.docto.notification.entity.Notification;
import com.ssginc8.docto.qna.entity.QaComment;
import com.ssginc8.docto.user.entity.User;

public interface NotificationService {

	/**
	 * 알림 읽음
	 */
	void markAsRead(Long notificationId);

	/**
	 * 알림 조회 (한 유저의)
	 */
	List<NotificationResponse> getNotificationsByLoginUser();

	/**
	 * isRead = true 인 알림들 삭제
	 */
	void deleteReadNotifications();

	/**
	 * Appointment 확정 알림 전송 (보호자)
	 */
	void notifyAppointmentConfirmed(Appointment appointment);

	/**
	 * Appointment 취소 알림 전송 (보호자)
	 */
	void notifyAppointmentCanceled(Appointment appointment);

	/**
	 * QNA 알림 전송
	 */
	void notifyQnaResponse(QaComment qaComment);

	/**
	 * GUARDIAN 초대 알림 전송
	 */
	void notifyGuardianInvite(PatientGuardian guardian);

	/**
	 * MEDICATION 복용 알림 전송 (환자)
	 */
	void notifyMedicationAlert(User user, String medicationName, LocalTime timeToTake, Long medicationInfoId);

	/**
	 * MEDICATION 미복용 알림 전송 (보호자)
	 */
	void notifyMedicationMissed();
}
