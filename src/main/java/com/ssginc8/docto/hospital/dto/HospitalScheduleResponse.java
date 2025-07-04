package com.ssginc8.docto.hospital.dto;

import java.time.DayOfWeek;

import java.time.LocalTime;



import lombok.Data;

import lombok.NoArgsConstructor;


@Data

@NoArgsConstructor
public class HospitalScheduleResponse {

	private Long hospitalScheduleId;
	private DayOfWeek dayOfWeek;
	private LocalTime openTime;
	private LocalTime closeTime;
	private LocalTime lunchStart;
	private LocalTime lunchEnd;

	public HospitalScheduleResponse(Long hospitalScheduleId, DayOfWeek dayOfWeek, LocalTime openTime, LocalTime closeTime, LocalTime lunchStart, LocalTime lunchEnd) {
		this.hospitalScheduleId = hospitalScheduleId;
		this.dayOfWeek = DayOfWeek.valueOf(dayOfWeek.toString());
		this.openTime = openTime;
		this.closeTime = closeTime;
		this.lunchStart = lunchStart;
		this.lunchEnd = lunchEnd;

	}


}