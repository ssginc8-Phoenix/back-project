package com.ssginc8.docto.notification.service;

import java.util.List;

import com.ssginc8.docto.appointment.entity.Appointment;
import com.ssginc8.docto.notification.dto.NotificationResponse;
import com.ssginc8.docto.qna.entity.QaComment;

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
	 * MEDICATION 복용 알림 전송 (환자)
	 */

	/**
	 * MEDICATION 미복용 알림 전송 (보호자)
	 */

	/**
	 * GUARDIAN 초대 알림 전송
	 */
}
