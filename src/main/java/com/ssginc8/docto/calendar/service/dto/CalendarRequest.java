package com.ssginc8.docto.calendar.service.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class CalendarRequest {
	private Integer year;
	private Integer month;

	public LocalDateTime getStartDateTime() {
		return LocalDate.of(year, month, 1).atStartOfDay();
	}

	public LocalDateTime getEndDateTime() {
		return LocalDate.of(year, month, 1).plusMonths(1).atStartOfDay();
	}

	// ✅ 추가: LocalDate 범위 반환용
	public LocalDate getStartDate() {
		return LocalDate.of(year, month, 1);
	}

	public LocalDate getEndDate() {
		return LocalDate.of(year, month, 1).plusMonths(1).minusDays(1);
	}
}
