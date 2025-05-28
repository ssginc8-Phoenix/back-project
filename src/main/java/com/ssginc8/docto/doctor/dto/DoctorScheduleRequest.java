package com.ssginc8.docto.doctor.dto;

import java.time.DayOfWeek;
import java.time.LocalTime;

import com.ssginc8.docto.doctor.entity.DoctorSchedule;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
public class DoctorScheduleRequest {



	private DayOfWeek dayOfWeek;
	private LocalTime startTime;
	private LocalTime endTime;
	private LocalTime lunchEnd;
	private LocalTime lunchStart;

	public DoctorScheduleRequest(DoctorSchedule schedule) {

		this.dayOfWeek = schedule.getDayOfWeek();
		this.startTime = schedule.getStartTime();
		this.endTime = schedule.getEndTime();
		this.lunchStart = schedule.getLunchStart();
		this.lunchEnd = schedule.getLunchEnd();
	}

	public DoctorScheduleRequest(DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime, LocalTime lunchStart, LocalTime lunchEnd) {
		this.dayOfWeek = dayOfWeek;
		this.startTime = startTime;
		this.endTime = endTime;
		this.lunchStart = lunchStart;
		this.lunchEnd = lunchEnd;

	}
}
