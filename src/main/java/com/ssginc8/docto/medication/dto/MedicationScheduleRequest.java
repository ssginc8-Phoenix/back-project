package com.ssginc8.docto.medication.dto;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 약 복용 스케줄 등록 요청 DTO
 *
 * - 보호자-환자 관계 ID(patientGuardianId)
 * - 복약 이름
 * - 복약 시간
 * - 복약 요일 리스트
 *
 * 클라이언트가 약을 등록할 때 요청 본문으로 사용
 */
@Getter
@NoArgsConstructor
@Builder
public class MedicationScheduleRequest {

	private Long patientGuardianId;          // 환자-보호자 관계 ID
	private String medicationName;           // 약 이름
	private LocalDateTime timeToTake;        // 복용 시간
	private List<DayOfWeek> days;            // 복용 요일 리스트

	public MedicationScheduleRequest(Long patientGuardianId, String medicationName,
		LocalDateTime timeToTake, List<DayOfWeek> days) {
		this.patientGuardianId = patientGuardianId;
		this.medicationName = medicationName;
		this.timeToTake = timeToTake;
		this.days = days;
	}
}