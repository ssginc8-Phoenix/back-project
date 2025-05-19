package com.ssginc8.docto.hospital.dto;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.ssginc8.docto.hospital.entity.Hospital;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data

@NoArgsConstructor
public class HospitalScheduleDTO {

	private Long hospitalScheduleId;
	private String dayOfWeek;
	private LocalTime openTime;
	private LocalTime closeTime;
	private LocalTime lunchStart;
	private LocalTime lunchEnd;

	public HospitalScheduleDTO(Long hospitalScheduleId, DayOfWeek dayOfWeek, LocalTime openTime, LocalTime closeTime, LocalTime lunchStart, LocalTime lunchEnd) {
		this.dayOfWeek = dayOfWeek.toString();
		this.openTime = openTime;
		this.closeTime = closeTime;
		this.lunchStart = lunchStart;
		this.lunchEnd = lunchEnd;
		this.hospitalScheduleId = hospitalScheduleId;
	}
}