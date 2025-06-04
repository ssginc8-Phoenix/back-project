package com.ssginc8.docto.appointment.dto;

import java.time.LocalDateTime;
import java.time.LocalTime;

import lombok.Getter;

@Getter
public class TimeSlotDto {
	private LocalTime start;
	private LocalTime end;
	private boolean available;

	public TimeSlotDto(LocalDateTime start, LocalDateTime end, boolean available) {
		this.start = start.toLocalTime();
		this.end = end.toLocalTime();
		this.available = available;
	}
}
