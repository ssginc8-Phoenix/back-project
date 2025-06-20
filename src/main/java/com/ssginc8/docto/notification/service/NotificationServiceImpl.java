package com.ssginc8.docto.notification.service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssginc8.docto.appointment.entity.Appointment;
import com.ssginc8.docto.fcm.service.FirebaseCloudMessageService;
import com.ssginc8.docto.global.error.exception.commentException.CommentNotFoundException;
import com.ssginc8.docto.global.error.exception.notificationException.NotificationSendFailed;
import com.ssginc8.docto.guardian.entity.PatientGuardian;
import com.ssginc8.docto.guardian.provider.PatientGuardianProvider;
import com.ssginc8.docto.medication.entity.MedicationAlertTime;
import com.ssginc8.docto.medication.entity.MedicationInformation;
import com.ssginc8.docto.medication.provider.MedicationProvider;
import com.ssginc8.docto.notification.dto.NotificationResponse;
import com.ssginc8.docto.notification.entity.Notification;
import com.ssginc8.docto.notification.entity.NotificationType;
import com.ssginc8.docto.notification.provider.NotificationProvider;
import com.ssginc8.docto.patient.entity.Patient;
import com.ssginc8.docto.patient.provider.PatientProvider;
import com.ssginc8.docto.qna.entity.QaComment;
import com.ssginc8.docto.qna.provider.CommentProvider;
import com.ssginc8.docto.user.entity.User;
import com.ssginc8.docto.user.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Transactional
@Log4j2
public class NotificationServiceImpl implements NotificationService {

	private final NotificationProvider notificationProvider;
	private final UserService userService;
	private final FirebaseCloudMessageService fcmService;
	private final CommentProvider commentProvider;
	private final MedicationProvider medicationProvider;
	private final PatientGuardianProvider guardianProvider;
	private final PatientProvider patientProvider;

	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yy.MM.d");
	private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

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

	@Override
	public void deleteReadNotifications() {
		User loginUser = userService.getUserFromUuid();

		// 해당 유저의 읽은 알림만 가져오기
		List<Notification> readNotifications = notificationProvider
			.getUserNotifications(loginUser.getUserId()).stream()
			.filter(Notification::isRead)
			.toList();

		// 소프트 삭제
		readNotifications.forEach(Notification::delete);
	}

	/**
	 * Notification 생성 메서드 (재사용을 위한)
	 */
	private void createNotification(User receiver, NotificationType type, String content, Long referenceId) {
		// 1. DB에 알림 저장
		Notification notification = new Notification(
			receiver,
			type,
			content,
			referenceId
		);
		notificationProvider.save(notification);

		// 2. FCM 전송
		try {
			fcmService.sendMessage(receiver.getUserId(), type.name(), content);
		} catch (Exception e) {
			log.error("FCM 메시지 전송 실패 (userId: {}, type: {}): {}", receiver.getUserId(), type.name(), e.getMessage(), e);
		}
	}

	private void createNotificationWithData(User receiver, NotificationType type, String title, String body,
		Long referenceId, Map<String, String> data) {
		// 1. DB 저장
		Notification notification = new Notification(receiver, type, body, referenceId);
		notificationProvider.save(notification);

		// 2. FCM 전송
		try {
			fcmService.sendMessageWithData(receiver.getUserId(), title, body, data);
		} catch (Exception e) {
			log.error("FCM 메시지 전송 실패 (userId: {}, type: {}): {}", receiver.getUserId(), type.name(), e.getMessage(), e);
		}
	}

	/**
	 * Appointment 확정 알림 전송 (보호자)
	 */
	@Override
	public void notifyAppointmentConfirmed(Appointment appointment) {
		User receiver = appointment.getPatientGuardian().getUser();
		String hospitalName = appointment.getHospital().getName();
		String patientName = appointment.getPatientGuardian().getPatient().getUser().getName();
		String time = appointment.getAppointmentTime().format(DATE_FORMATTER);

		String content = String.format("%s, %s의 %s님 예약이 확정되었습니다.", time, hospitalName, patientName);
		createNotification(receiver, NotificationType.APPOINTMENT_CONFIRMED, content, appointment.getAppointmentId());
	}



	/**
	 * Appointment 취소 알림 전송 (보호자)
	 */
	@Override
	public void notifyAppointmentCanceled(Appointment appointment) {
		User receiver = appointment.getGuardian();

		String hospitalName = appointment.getHospital().getName();
		String patientName = appointment.getPatientGuardian().getPatient().getUser().getName();
		String time = appointment.getAppointmentTime().format(DATE_FORMATTER);

		String content = String.format("%s, %s의 %s님 예약이 취소되었습니다.", time, hospitalName, patientName);
		createNotification(receiver, NotificationType.APPOINTMENT_CANCELED, content, appointment.getAppointmentId());

	}

	/**
	 * Appointment 노쇼 알림 전송 (보호자)
	 */
	@Override
	public void notifyAppointmentNoShow(Appointment appointment) {
		User receiver = appointment.getPatientGuardian().getUser();

		String hospitalName = appointment.getHospital().getName();
		String patientName = appointment.getPatientGuardian().getPatient().getUser().getName();
		String time = appointment.getAppointmentTime().format(DATE_FORMATTER);

		String content = String.format("%s, %s의 %s님 예약이 노쇼 처리되어 패널티가 부과됩니다.", time, hospitalName, patientName);
		createNotification(receiver, NotificationType.APPOINTMENT_NOSHOW, content, appointment.getAppointmentId());
	}

	@Override
	public void notifyPaymentRequest(Appointment appointment, Long paymentRequestId) {
		User receiver = appointment.getGuardian();
		String patientName = appointment.getPatientName();

		String content = String.format("[%s]님의 진료에 대한 결제 요청이 도착했습니다. 알림을 클릭해 결제를 진행해주세요.", patientName);

		try {
			createNotification(receiver, NotificationType.PAYMENT_REQUEST, content, paymentRequestId);
		} catch (Exception e) {
			throw new NotificationSendFailed();
		}
	}

	/**
	 * QNA 답변 알림 전송
	 */
	@Override
	public void notifyQnaResponse(QaComment qaComment) {
		Object[] data = commentProvider.findNotificationDataByQnaCommentId(qaComment.getQnaCommentId());

		if (data == null) {
			throw new CommentNotFoundException();
		}

		User receiver = (User)data[0];
		String hospitalName = (String)data[1];
		String patientName = (String)data[2];
		LocalDateTime time = (LocalDateTime)data[3];

		String content = String.format("%s에 작성하신 QnA에 답변이 등록되었습니다. (%s, %s)",
			time.format(DATE_FORMATTER), hospitalName, patientName);
		createNotification(receiver, NotificationType.QNA_RESPONSE, content, qaComment.getQnaCommentId());

	}

	/**
	 * GUARDIAN 초대 알림 전송
	 */
	@Override
	public void notifyGuardianInvite(User receiver, String patientName, Long patientGuardianId) {

		String content = String.format("%s님께서 당신을 보호자로 초대했습니다.", patientName);
		createNotification(receiver, NotificationType.GUARDIAN_INVITE, content, patientGuardianId);
	}

	/**
	 * Medication 복욕 알림 전송
	 */
	@Override
	public void notifyMedicationAlert(User receiver, String medicationName, LocalTime timeToTake, Long medicationInfoId, Map<String, String> data) {
		String title = "복약 알림 💊";
		String body = String.format("%s님, %s에 복용할 약 '%s'이 있습니다.", receiver.getName(), timeToTake.format(TIME_FORMATTER),
			medicationName);

		// `data` 맵에 'action' 필드 추가 (Service Worker 에서 이 필드를 보고 버튼 생성 여부 판단)
		data.put("action", "TAKE_MEDICATION");

		createNotificationWithData(receiver, NotificationType.MEDICATION_ALERT, title, body, medicationInfoId, data);
	}

	/**
	 * MEDICATION 미복용 알림 전송 (보호자)
	 */
	@Override
	public void notifyMedicationMissed(MedicationInformation info, MedicationAlertTime alertTime, User patientUser) {
		String medicationName = info.getMedicationName();
		LocalTime missedTime = alertTime.getTimeToTake();

		// 보호자 목록 조회 (여러 명)
		Patient patient = patientProvider.getPatientByUserId(patientUser.getUserId());
		List<PatientGuardian> guardians = guardianProvider.getAllAcceptedGuardiansByPatientId(patient.getPatientId());

		if (guardians.isEmpty()) {
			return;
		}

		String title = "미복용 알림 🚨";
		String body = String.format("%s님이 %s에 복용 예정이었던 '%s'을(를) 복용하지 않았습니다.",
			patientUser.getName(), missedTime.format(TIME_FORMATTER), medicationName);

		// 프론트엔드 전달용 데이터
		Map<String, String> data = new HashMap<>();
		data.put("type", "MEDICATION_MISSED_ALERT");
		data.put("patientId", String.valueOf(patientUser.getUserId()));
		data.put("patientName", patientUser.getName());
		data.put("medicationName", medicationName);
		data.put("missedTime", missedTime.format(TIME_FORMATTER));

		for (PatientGuardian guardian : guardians) {
			User guardianUser = guardian.getUser();

			createNotificationWithData(
				guardian.getUser(),
				NotificationType.MEDICATION_MISSED,
				title,
				body,
				patientUser.getUserId(),
				data
			);
		}

	}

}
