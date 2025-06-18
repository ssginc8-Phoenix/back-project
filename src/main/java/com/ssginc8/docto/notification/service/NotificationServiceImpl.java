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
		log.info("알림이 생성: {}, type: {}, content: {}, refId: {}",
			receiver.getUserId(), type, content, referenceId);


		Notification notification = new Notification(
			receiver,
			type,
			content,
			referenceId
		);
		notificationProvider.save(notification);

		try {
			fcmService.sendMessage(receiver.getUserId(), type.name(), content);
		} catch (Exception e) {
			throw new NotificationSendFailed();
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
		User receiver = appointment.getPatientGuardian().getUser();

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


		String content = String.format("%s에 작성하신 QnA에 답변이 등록되었습니다. (%s, %s)",
			time.format(DATE_FORMATTER), hospitalName, patientName);
		createNotification(receiver, NotificationType.QNA_RESPONSE, content, qaComment.getQnaCommentId());

	}

	/**
	 * GUARDIAN 초대 알림 전송
	 */
	@Override
	public void notifyGuardianInvite(PatientGuardian guardian) {
		User receiver = guardian.getUser();

		String patientName = guardian.getPatient().getUser().getName();

		String content = String.format("%s님께서 당신을 보호자로 초대했습니다.", patientName);
		createNotification(receiver, NotificationType.GUARDIAN_INVITE, content, guardian.getPatientGuardianId());
	}

	/**
	 * Medication 복욕 알림 전송
	 */
	@Override
	public void notifyMedicationAlert(User receiver, String medicationName, LocalTime timeToTake, Long medicationInfoId) {
		String formattedTime = timeToTake.format(TIME_FORMATTER);
		String content = String.format("💊 %s님, %s에 복용할 약 '%s'이 있습니다.",
			receiver.getName(), formattedTime, medicationName);

		createNotification(receiver, NotificationType.MEDICATION_ALERT, content, medicationInfoId);
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

			String formattedAlertTime = alertTime.getTimeToTake().format(TIME_FORMATTER);

			for (PatientGuardian guardian : guardians) {
				User guardianUser = guardian.getUser();

				String content = String.format("⚠️ %s님께서 %s에 '%s' 복용을 놓쳤습니다.",
					patienUser.getName(), formattedAlertTime, info.getMedicationName());

				createNotification(guardianUser, NotificationType.MEDICATION_MISSED, content, info.getMedicationId());
			}
		}
	}

}
