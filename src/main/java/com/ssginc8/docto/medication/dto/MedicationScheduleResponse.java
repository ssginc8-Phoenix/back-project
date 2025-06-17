package com.ssginc8.docto.medication.dto;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssginc8.docto.medication.entity.MedicationAlertDay;
import com.ssginc8.docto.medication.entity.MedicationAlertTime;
import com.ssginc8.docto.medication.entity.MedicationInformation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 약 복용 스케줄 응답 DTO
 *
 * - 약 ID
 * - 약 이름
 * - 복용 시간 리스트
 * - 복용 요일 리스트
 * - 복용 기간
 */
@Getter
@Builder
public class MedicationScheduleResponse {
	private final Long medicationId;
	private final String medicationName;
	private final List<MealTime> times;   // MealTime: { String meal; LocalTime time; }
	private final List<DayOfWeek> days;
	private final LocalDate startDate;
	private final LocalDate endDate;

	@Getter @AllArgsConstructor
	@NoArgsConstructor
	public static class MealTime {
		private String meal;
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
		private LocalTime time;
	}

	public static MedicationScheduleResponse from(MedicationInformation info) {
		List<MealTime> times = info.getAlertTimes().stream()
			.map(at -> new MealTime(at.getMeal(), at.getTimeToTake()))
			.collect(Collectors.toList());
		List<DayOfWeek> days = info.getAlertTimes().stream()
			.flatMap(at -> at.getAlertDays().stream())
			.map(MedicationAlertDay::getDayOfWeek)
			.distinct()
			.collect(Collectors.toList());;  // 기존대로
		return MedicationScheduleResponse.builder()
			.medicationId(info.getMedicationId())
			.medicationName(info.getMedicationName())
			.times(times)
			.days(days)
			.startDate(info.getStartDate())
			.endDate(info.getEndDate())
			.build();
	}
}