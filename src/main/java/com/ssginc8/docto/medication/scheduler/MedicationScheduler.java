package com.ssginc8.docto.medication.scheduler;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ssginc8.docto.global.error.exception.guardianException.GuardianNotFoundException;
import com.ssginc8.docto.global.error.exception.patientException.PatientNotFoundException;
import com.ssginc8.docto.guardian.entity.PatientGuardian;
import com.ssginc8.docto.guardian.provider.PatientGuardianProvider;
import com.ssginc8.docto.medication.entity.MedicationAlertTime;
import com.ssginc8.docto.medication.entity.MedicationInformation;
import com.ssginc8.docto.medication.entity.MedicationLog;
import com.ssginc8.docto.medication.entity.MedicationStatus;
import com.ssginc8.docto.medication.provider.MedicationProvider;
import com.ssginc8.docto.medication.service.MedicationService;
import com.ssginc8.docto.notification.service.NotificationService;
import com.ssginc8.docto.patient.entity.Patient;
import com.ssginc8.docto.user.entity.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 * 정기적으로 복약 시간 조회 및 미복용 알림 전송을 관리하는 스케줄러
 */
@Component
@RequiredArgsConstructor
@Log4j2
public class MedicationScheduler {

	private final MedicationProvider medicationProvider;
	private final NotificationService notificationService;

	private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
	private final PatientGuardianProvider patientGuardianProvider;

	/**
	 * 복용 알림 전송 (매분 0초에 실행)
	 * - 현재 시각과 일치하는 알림 시간을 찾아 사용자에게 알림을 전송
	 * - 이미 복용 완료 (TAKEN) 또는 미복용 (MISSED) 처리된 약은 다시 알림을 보내지 않음
	 */
	@Transactional
	@Scheduled(cron = "0 * * * * *")
	public void sendMedicationAlerts() {
		DayOfWeek today = LocalDate.now().getDayOfWeek();
		LocalTime now = LocalTime.now().withSecond(0).withNano(0);	// 현재 분의 정시

		// 현재 요일 + 시간에 해당하는 알림 시간 (MedicationAlertTime) 조회
		List<MedicationAlertTime> alertTimes = medicationProvider.findAlertTimesDayAndTime(today, now);

		for (MedicationAlertTime alertTime : alertTimes) {
			MedicationInformation info = alertTime.getMedication();

			// 해당 알림 시간(alertTime)에 대해 오늘 이미 복용 완료(TAKEN) 또는 미복용(MISSED) 로그가 있는지 확인
			if (medicationProvider.existsTakenLogForToday(alertTime) || medicationProvider.existsMissedLogForToday(alertTime)) {
				continue;
			}

			User patientUser = null;
			try {
				Long patientGuardianId = info.getPatientGuardianId();
				PatientGuardian patientGuardian = patientGuardianProvider.getPatientGuardianById(patientGuardianId);
				Patient patient = patientGuardian.getPatient();
				patientUser = patient.getUser();
			} catch (GuardianNotFoundException e) {
				log.warn("ID {} 에 해당하는 보호자-환자 관계를 찾을 수 없어 이 약의 미복용 알림을 건너뜁니다.", info.getMedicationId());
				continue;
			} catch (PatientNotFoundException e) {
				log.warn("ID {} 에 해당하는 환자 정보를 찾을 수 없어 이 약의 미복용 알림을 건너뜁니다.", info.getMedicationId());
				continue;
			} catch (Exception e) { // 기타 예상치 못한 오류
				log.error("미복용 알림 대상 사용자 정보 조회 중 오류 발생. MedicationId: {}, Error: {}", info.getMedicationId(), e.getMessage(), e);
				continue;
			}

			// FCM 메세지의 'data' 필드에 담을 정보 추가
			// medicationInfo Id와 medicationAlertTimeId를 함께 전달하여 프론트에서 활용
			Map<String, String> data = new HashMap<>();
			data.put("type", "MEDICATION_ALERT");
			data.put("referenceId", String.valueOf(info.getMedicationId()));
			data.put("medicationAlertTimeId", String.valueOf(alertTime.getMedicationAlertTimeId()));
			data.put("medicationName", info.getMedicationName());
			data.put("timeToTake", alertTime.getTimeToTake().format(TIME_FORMATTER));
			data.put("userId", String.valueOf(patientUser.getUserId()));

			notificationService.notifyMedicationAlert(
				patientUser,
				info.getMedicationName(),
				alertTime.getTimeToTake(),
				info.getMedicationId(),
				data
			);
		}
	}

	/**
	 * 미복용 알림 전송 (매분 30초에 실행)
	 * - 일정 시간 (알림 시간 5분 후)이 지났음에도 TAKEN 로그가 없는 알림에 대해 MISSED 로그를 생성하고
	 * 		환자의 보호자에게 미복용 알림을 전송
	 * 	- 이미 MISSED 처리되었거나 TAKEN 처리된 약은 다시 처리하지 않음
	 */
	@Scheduled(cron = "30 * * * * *")
	@Transactional
	public void sendMedicationMissedAlerts() {
		log.info("미복용 알림 시작!!!!");
		DayOfWeek today = LocalDate.now().getDayOfWeek();
		// 현재 시각으로부터 특정 시간 (5분) 전의 "정시"를 기준으로 조회
		// 즉, 11:20:30 에 스케줄러가 돌면, 11:15:00 에 알림이 예정되어 있던 약들을 체크
		LocalTime alertTimeToCheck = LocalTime.now().minusMinutes(5).withSecond(0).withNano(0);

		log.info("체크 대상 시간: 현재 요일={}, 5분 전 정시={}", today, alertTimeToCheck);

		// 5분 전 정시에 알림이 예정되어 있던 MedicationAlertTime 목록 조회
		List<MedicationAlertTime> candidateAlertTimes = medicationProvider.findAlertTimesDayAndTime(today, alertTimeToCheck);
		log.info("조회된 알림 대상 시간 (candidateAlertTimes) 개수: {}", candidateAlertTimes.size());

		if (candidateAlertTimes.isEmpty()) {
			log.info("미복용 알림을 보낼 대상이 없습니다.");
			return; // 대상이 없으면 종료
		}

		for (MedicationAlertTime alertTime : candidateAlertTimes) {
			MedicationInformation info = alertTime.getMedication();
			log.info("알림 시간 ID: {}, 약 이름: {}, 알림 예정 시간: {}", alertTime.getMedicationAlertTimeId(), info.getMedicationName(), alertTime.getTimeToTake());

			// 해당 알림 시간(alertTime)에 대해 오늘 이미 TAKEN 또는 MISSED 로그가 있는지 확인
			boolean hasTakenLog = medicationProvider.existsTakenLogForToday(alertTime);
			boolean hasMissedLog = medicationProvider.existsMissedLogForToday(alertTime);

			log.info("오늘 복용 로그 존재 여부 (TAKEN): {}, 미복용 로그 존재 여부 (MISSED): {}", hasTakenLog, hasMissedLog);

			// 만약 TAKEN 되지도 않았고, MISSED 처리되지도 않은 경우에만 미복용 처리
			if (!hasTakenLog && !hasMissedLog) {
				log.info("미복용 처리 대상입니다: MedicationId={}, AlertTimeId={}", info.getMedicationId(), alertTime.getMedicationAlertTimeId());
				User patientUser = null;
				try {
					Long patientGuardianId = info.getPatientGuardianId();
					PatientGuardian patientGuardian = patientGuardianProvider.getPatientGuardianById(patientGuardianId);
					Patient patient = patientGuardian.getPatient();
					patientUser = patient.getUser();
					log.info("환자 사용자 정보 조회 성공. UserId: {}", patientUser.getUserId());
				} catch (GuardianNotFoundException e) {
					log.warn("ID {} 에 해당하는 보호자-환자 관계를 찾을 수 없어 이 약의 미복용 알림을 건너뜁니다.", info.getMedicationId());
					continue;
				} catch (PatientNotFoundException e) {
					log.warn("ID {} 에 해당하는 환자 정보를 찾을 수 없어 이 약의 미복용 알림을 건너뜁니다.", info.getMedicationId());
					continue;
				} catch (Exception e) { // 기타 예상치 못한 오류
					log.error("미복용 알림 대상 사용자 정보 조회 중 오류 발생. MedicationId: {}, Error: {}", info.getMedicationId(), e.getMessage(), e);
					continue;
				}


				// 1. MISSED 상태의 MedicationLog 생성 및 저장
				MedicationLog missedLog = MedicationLog.create(
					alertTime,
					info,
					MedicationStatus.MISSED,
					LocalDateTime.now()
				);
				medicationProvider.saveMedicationLog(missedLog);
				log.info("미복용 로그 저장 성공 !");

				// 2. 보호자에게 미복용 알림 전송
				notificationService.notifyMedicationMissed(info, alertTime, patientUser);
			}
		}
	}
}
