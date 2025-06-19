package com.ssginc8.docto.medication.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicationUpdateRequest {

	/**
	 * 수정할 복약 시간 목록.
	 * 각 요소에 'meal' 과 'time' 이 함께 담깁니다.
	 */
	private List<MealTime> newTimes;

	/** 수정할 요일 목록 */
	private List<DayOfWeek> newDays;

	/** 수정할 시작일 (optional) */
	private LocalDate newStartDate;

	/** 수정할 종료일 (optional) */
	private LocalDate newEndDate;

	/**
	 * 끼니별 시간 정보
	 */
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class MealTime {
		/**
		 * "morning", "lunch", "dinner" 중 하나
		 */
		private String meal;

		/**
		 * "HH:mm:ss" 형식으로 직렬화/역직렬화
		 */
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
		private LocalTime time;
	}
}
