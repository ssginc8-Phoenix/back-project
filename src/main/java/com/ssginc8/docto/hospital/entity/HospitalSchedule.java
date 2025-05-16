package com.ssginc8.docto.hospital.entity;

import java.time.LocalDateTime;

import com.ssginc8.docto.global.base.DayOfWeek;

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
@Table
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class HospitalSchedule {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long hospitalScheduleId;

	@ManyToOne
	@JoinColumn(name = "hospitalId", nullable = false)
	private Hospital hospital;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private DayOfWeek dayOfWeek;

	@Column(nullable = false)
	private LocalDateTime openTime;

	@Column(nullable = false)
	private LocalDateTime closeTime;

	@Column(nullable = false)
	private LocalDateTime lunchStart;

	@Column(nullable = false)
	private LocalDateTime lunchEnd;
}
