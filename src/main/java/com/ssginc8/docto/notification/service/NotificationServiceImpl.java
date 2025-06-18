package com.ssginc8.docto.notification.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

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
		log.info("알림 생성 대상 user: {}", receiver);
		log.info("알림 내용: {}", content);
		log.info("타겟 ID: {}", referenceId);


		Notification notification = new Notification(
			receiver,
			type,
			content,
			referenceId
		);
		notificationProvider.save(notification);

		// 토큰 조회 전 로그
		log.info("FCM 전송 전, userId={}", receiver.getUserId());
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

		try {
			createNotification(receiver, NotificationType.APPOINTMENT_CONFIRMED, content, appointment.getAppointmentId());
		} catch (Exception e) {
			throw new NotificationSendFailed();
		}
	}



	/**
	 * Appointment 취소 알림 전송 (보호자)
	 */
	@Override
	public void notifyAppointmentCanceled(Appointment appointment) {
		User receiver = appointment.getGuardian();

		String hospitalName = appointment.getHospital().getName();
		String patientName = appointment.getPatientName();
		String time = appointment.getAppointmentTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

		String content = String.format("%s %s의 %s 환자의 예약이 취소되었습니다.", time, hospitalName, patientName);

		try {
			createNotification(receiver, NotificationType.APPOINTMENT_CANCELED, content, appointment.getAppointmentId());
		} catch (Exception e) {
			throw new NotificationSendFailed();
		}
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

		User receiver = (User) data[0];
		String hospitalName = (String) data[1];
		String patientName = (String) data[2];
		LocalDateTime time = (LocalDateTime) data[3];

		String content = String.format("%s %s 병원의 QnA에 답변이 등록되었습니다. (%s)",
			time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")), hospitalName, patientName);

		try {
			createNotification(receiver, NotificationType.QNA_RESPONSE, content, qaComment.getQnaCommentId());
		} catch (Exception e) {
			throw new NotificationSendFailed();
		}
	}

	/**
	 * GUARDIAN 초대 알림 전송
	 */
	@Override
	public void notifyGuardianInvite(PatientGuardian guardian) {
		User receiver = guardian.getUser();

		String patientName = guardian.getPatient().getUser().getName();

		String content = String.format("환자 %s님이 당신을 보호자로 초대했습니다.", patientName);

		try {
			createNotification(receiver, NotificationType.GUARDIAN_INVITE, content, guardian.getPatientGuardianId());
		} catch (Exception e) {
			throw new NotificationSendFailed();
		}
	}

	/**
	 * Medication 복욕 알림 전송
	 */
	@Override
	public void notifyMedicationAlert(User receiver, String medicationName, LocalTime timeToTake, Long medicationInfoId) {
		String content = String.format("💊 %s님, %s에 복용할 약 '%s'이 있습니다.",
			receiver.getName(), timeToTake.toString(), medicationName);

		try {
			createNotification(receiver, NotificationType.MEDICATION_ALERT, content, medicationInfoId);
		} catch (Exception e) {
			throw new NotificationSendFailed();
		}
	}

	/**
	 * MEDICATION 미복용 알림 전송 (보호자)
	 */
	@Override
	public void notifyMedicationMissed() {
		LocalTime fiveMinutesAgo = LocalTime.now().minusMinutes(5).withSecond(0).withNano(0);
		DayOfWeek today = LocalDateTime.now().getDayOfWeek();
		LocalDate date = LocalDate.now();

		// 5분 전 복약 알림 시간 중, 아직 복약 로그가 없는 것 조회
		List<MedicationAlertTime> missedAlertTimes =
			medicationProvider.findAlertTimesDayAndTime(today, fiveMinutesAgo).stream()
				.filter(alertTime -> !medicationProvider.existsMedicationLog(alertTime, date))
				.toList();

		for (MedicationAlertTime alertTime : missedAlertTimes) {
			MedicationInformation info = alertTime.getMedication();

			User patienUser = info.getUser();

			// 보호자 목록 조회 (여러 명)
			Patient patient = patientProvider.getPatientByUserId(patienUser.getUserId());
			List<PatientGuardian> guardians = guardianProvider.getAllAcceptedGuardiansByPatientId(patient.getPatientId());

			for (PatientGuardian guardian : guardians) {
				User guardianUser = guardian.getUser();

				String content = String.format("⚠️ %s님이 %s에 약 '%s'을 복용하지 않았습니다.",
					patienUser.getName(), alertTime.getTimeToTake(), info.getMedicationName());

				try {
					createNotification(guardianUser, NotificationType.MEDICATION_MISSED, content, info.getMedicationId());
				} catch (Exception e) {
					throw new NotificationSendFailed();
				}
			}
		}
	}

}
