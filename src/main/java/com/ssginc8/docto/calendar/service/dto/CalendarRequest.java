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
}
