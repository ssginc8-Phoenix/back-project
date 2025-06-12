package com.ssginc8.docto.medication.scheduler;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ssginc8.docto.medication.entity.MedicationAlertTime;
import com.ssginc8.docto.medication.entity.MedicationInformation;
import com.ssginc8.docto.medication.provider.MedicationProvider;
import com.ssginc8.docto.medication.service.MedicationService;
import com.ssginc8.docto.notification.service.NotificationService;
import com.ssginc8.docto.user.entity.User;

import lombok.RequiredArgsConstructor;

/**
 * 정기적으로 복약 시간 조회
 */
@Component
@RequiredArgsConstructor
public class MedicationScheduler {

	private final MedicationProvider medicationProvider;
	private final NotificationService notificationService;

	@Scheduled(cron = "0 * * * * *")
	public void sendMedicationAlerts() {
		DayOfWeek today = LocalDate.now().getDayOfWeek();
		LocalTime now = LocalTime.now().withSecond(0).withNano(0);	// 초와 나노초 제거 (정시기준)

		// 현재 요일 + 시간에 해당하는 알림 시간 조회
		List<MedicationAlertTime> alertTimes = medicationProvider.findAlertTimesDayAndTime(today, now);

		for (MedicationAlertTime alertTime : alertTimes) {
			MedicationInformation info = alertTime.getMedication();
			User user = info.getUser();

			notificationService.notifyMedicationAlert(
				user,
				info.getMedicationName(),
				alertTime.getTimeToTake(),
				info.getMedicationId());
		}
	}
}
