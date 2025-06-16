package com.ssginc8.docto.appointment.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ssginc8.docto.appointment.service.NoShowDetectionService;

import lombok.RequiredArgsConstructor;

/**
 * 매일 자정에 실행되는 스케줄러
 * 어제 날짜 기준으로 노쇼 감지 로직을 실행
 */

@Component
@RequiredArgsConstructor
public class AppointmentScheduler {

	private final NoShowDetectionService noShowDetectionService;

	/**
	 * 매일 자정 5분 후 실행 (00:05)
	 */
	@Scheduled(cron = "0 5 0 * * *")
	public void runNoShowDetection() {
		noShowDetectionService.detectAndApplyNoShowPenalties();
	}
}
