package com.ssginc8.docto.medication.dto;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 복약 시간 수정 요청 DTO
 *
 * - 클라이언트가 복약 시간을 수정할 때 사용
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicationUpdateRequest {

	private LocalTime newTimeToTake; // 수정할 복약 시간
	private List<DayOfWeek> newDays; // 수정할 요일 리스트
}
