package com.ssginc8.docto.medication.dto;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;

import com.ssginc8.docto.medication.entity.MedicationAlertTime;
import com.ssginc8.docto.medication.entity.MedicationInformation;

import lombok.Getter;

/**
 * 약 복용 스케줄 응답 DTO
 *
 * - 약 ID
 * - 약 이름
 * - 복용 시간
 * - 요일 리스트
 *
 * 클라이언트에게 약 정보 + 복용 요일을 반환할 때 사용
 */
@Getter
public class MedicationScheduleResponse {

	private final Long medicationId;          // 약 ID
	private final String medicationName;      // 약 이름
	private final LocalDateTime timeToTake;   // 복약 시간
	private final List<DayOfWeek> days;       // 요일 리스트

	public MedicationScheduleResponse(Long medicationId, String medicationName, LocalDateTime timeToTake, List<DayOfWeek> days) {
		this.medicationId = medicationId;
		this.medicationName = medicationName;
		this.timeToTake = timeToTake;
		this.days = days;
	}

	/**
	 * MedicationInformation + MedicationAlertTime + 요일 → DTO로 변환
	 */
	public static MedicationScheduleResponse from(MedicationInformation info, MedicationAlertTime time, List<DayOfWeek> days) {
		return new MedicationScheduleResponse(
			info.getMedicationId(),
			info.getMedicationName(),
			time.getTimeToTake(),
			days
		);
	}
}