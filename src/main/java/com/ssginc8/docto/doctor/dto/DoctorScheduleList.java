package com.ssginc8.docto.doctor.dto;

import java.time.DayOfWeek;

import java.time.LocalTime;

import com.ssginc8.docto.doctor.entity.DoctorSchedule;



import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DoctorScheduleList {

	private Long scheduleId;
	private Long doctorId;
	private DayOfWeek dayOfWeek;
	private LocalTime startTime;
	private LocalTime endTime;
	private LocalTime lunchEnd;
	private LocalTime lunchStart;




	public DoctorScheduleList(DoctorSchedule schedule) {
		this.scheduleId = schedule.getScheduleId();
		this.doctorId = schedule.getDoctor().getDoctorId();
		this.dayOfWeek = schedule.getDayOfWeek();
		this.startTime = schedule.getStartTime();
		this.endTime = schedule.getEndTime();
		this.lunchStart = schedule.getLunchStart();
		this.lunchEnd = schedule.getLunchEnd();
	}
}
