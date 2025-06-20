package com.ssginc8.docto.notification.service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import com.ssginc8.docto.appointment.entity.Appointment;
import com.ssginc8.docto.guardian.entity.PatientGuardian;
import com.ssginc8.docto.medication.entity.MedicationAlertTime;
import com.ssginc8.docto.medication.entity.MedicationInformation;
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
	 * 결제 요청 알림 전송 (보호자)
	 */
	void notifyPaymentRequest(Appointment appointment, Long paymentRequestId);

	/** 
   * Appointment 노쇼 알림 전송 (보호자)
	 */
	void notifyAppointmentNoShow(Appointment appointment);

	/**
	 * QNA 알림 전송
	 */
	void notifyQnaResponse(Long qnaPostId, LocalDateTime answeredAt, Long qnaCommentId);

	/**
	 * GUARDIAN 초대 알림 전송
	 */
	void notifyGuardianInvite(User guardian, String patientName, Long patientGuardianId);

	/**
	 * MEDICATION 복용 알림 전송 (환자)
	 */
	void notifyMedicationAlert(User receiver, String medicationName, LocalTime timeToTake, Long medicationInfoId, Map<String, String> data);

	/**
	 * MEDICATION 미복용 알림 전송 (보호자)
	 */
	void notifyMedicationMissed(MedicationInformation info, MedicationAlertTime alertTime, User patientUser);
}
