package com.ssginc8.docto.medication.dto;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import lombok.AllArgsConstructor;
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
@AllArgsConstructor
@Builder
public class MedicationScheduleRequest {

	private Long userId;
	private String medicationName;
	private LocalTime timeToTake;
	private List<DayOfWeek> days;
	private Long patientGuardianId;

	private LocalDate startDate; // ✅ 시작일 추가
	private LocalDate endDate;   // ✅ 종료일 추가
}