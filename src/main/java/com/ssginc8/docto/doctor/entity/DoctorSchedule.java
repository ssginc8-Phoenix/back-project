package com.ssginc8.docto.doctor.entity;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.ssginc8.docto.doctor.dto.DoctorScheduleRequest;
import com.ssginc8.docto.global.base.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tbl_doctor_schedule")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class DoctorSchedule extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long scheduleId;

	@ManyToOne
	@JoinColumn(name = "doctorId", nullable = false)
	private Doctor doctor;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private DayOfWeek dayOfWeek;

	@Column(nullable = false)
	private LocalTime startTime;

	@Column(nullable = false)
	private LocalTime endTime;

	@Column(nullable = false)
	private LocalTime lunchStart;

	@Column(nullable = false)
	private LocalTime lunchEnd;

	public DoctorSchedule(Doctor doctor, DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime, LocalTime lunchStart, LocalTime lunchEnd) {
		this.doctor = doctor;
		this.dayOfWeek = dayOfWeek;
		this.startTime = startTime;
		this.endTime = endTime;
		this.lunchStart = lunchStart;
		this.lunchEnd = lunchEnd;

	}

	public static DoctorSchedule create(

		Doctor doctor,
		DayOfWeek dayOfWeek,
		LocalTime startTime,
		LocalTime endTime,
		LocalTime lunchStart,
		LocalTime lunchEnd) {
		return new DoctorSchedule(doctor, dayOfWeek, startTime, endTime, lunchStart, lunchEnd);
	}
	public void updateDoctorSchedule(DoctorScheduleRequest request) {
		this.dayOfWeek = request.getDayOfWeek();
		this.startTime = request.getStartTime();
		this.endTime = request.getEndTime();
		this.lunchStart = request.getLunchStart();
		this.lunchEnd = request.getLunchEnd();

	}
}
