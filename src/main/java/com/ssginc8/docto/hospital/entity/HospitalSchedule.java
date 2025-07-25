package com.ssginc8.docto.hospital.entity;

import java.time.DayOfWeek;
import java.time.LocalTime;

import org.hibernate.annotations.DynamicUpdate;


import com.ssginc8.docto.hospital.dto.HospitalScheduleRequest;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity

@DynamicUpdate

@Table(name = "tbl_hospital_schedule")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter

public class HospitalSchedule {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long hospitalScheduleId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "hospital_id", nullable = false)
	private Hospital hospital;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private DayOfWeek dayOfWeek;

	@Column(nullable = false, name = "open_time")
	private LocalTime openTime;

	@Column(nullable = false, name = "close_time")
	private LocalTime closeTime;

	@Column(nullable = true, name = "lunch_start")
	private LocalTime lunchStart;

	@Column(nullable = true, name = "lunch_end")
	private LocalTime lunchEnd;



	public HospitalSchedule(Hospital hospital, DayOfWeek dayOfWeek, LocalTime openTime, LocalTime closeTime, LocalTime lunchStart, LocalTime lunchEnd) {
		this.hospital = hospital;
		this.dayOfWeek = dayOfWeek;
		this.openTime = openTime;
		this.closeTime = closeTime;
		this.lunchStart = lunchStart;
		this.lunchEnd = lunchEnd;
	}

	public static HospitalSchedule create(

		Hospital hospital,
		DayOfWeek dayOfWeek,
		LocalTime openTime,
		LocalTime closeTime,
		LocalTime lunchStart,
		LocalTime lunchEnd) {
		return new HospitalSchedule(hospital,dayOfWeek, openTime, closeTime, lunchStart, lunchEnd);
	}

	public void updateSchedule(DayOfWeek dayOfWeek, LocalTime openTime, LocalTime closeTime, LocalTime lunchStart, LocalTime lunchEnd) {
		this.dayOfWeek = dayOfWeek;
		this.openTime = openTime;
		this.closeTime = closeTime;
		this.lunchStart = lunchStart;
		this.lunchEnd = lunchEnd;
	}
}
