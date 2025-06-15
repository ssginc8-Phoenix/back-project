package com.ssginc8.docto.appointment.dto;

import java.time.LocalDate;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AppointmentDailyCountResponse {
	private LocalDate date;
	private Long count;

	public AppointmentDailyCountResponse(LocalDate date, Long count) {
		this.date = date;
		this.count = count;
	}
}
