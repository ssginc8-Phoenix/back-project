package com.ssginc8.docto.hospital.dto;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.temporal.WeekFields;

import com.ssginc8.docto.hospital.entity.HospitalSchedule;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HospitalScheduleRequest {

	private DayOfWeek dayOfWeek;
	private LocalTime openTime;
	private LocalTime closeTime;
	private LocalTime lunchStart;
	private LocalTime lunchEnd;



	public HospitalScheduleRequest(HospitalSchedule saved) {

		this.dayOfWeek = saved.getDayOfWeek();
		this.openTime = saved.getOpenTime();
		this.closeTime = saved.getCloseTime();
		this.lunchStart = saved.getLunchStart();
		this.lunchEnd = saved.getLunchEnd();
	}


}
