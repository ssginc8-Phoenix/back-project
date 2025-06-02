package com.ssginc8.docto.medication.dto;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
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

	private final Long medicationId;
	private final String medicationName;
	private final LocalTime timeToTake;
	private final List<DayOfWeek> days;

	public MedicationScheduleResponse(Long medicationId, String medicationName, LocalTime timeToTake, List<DayOfWeek> days) {
		this.medicationId = medicationId;
		this.medicationName = medicationName;
		this.timeToTake = timeToTake;
		this.days = days;
	}

	public static MedicationScheduleResponse from(MedicationInformation info, MedicationAlertTime time, List<DayOfWeek> days) {
		return new MedicationScheduleResponse(
			info.getMedicationId(),
			info.getMedicationName(),
			time.getTimeToTake(),
			days
		);
	}
}