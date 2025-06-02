package com.ssginc8.docto.medication.entity;

import java.time.DayOfWeek;

import com.ssginc8.docto.global.base.BaseTimeEntity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 복약 요일 Entity
 *
 * - 특정 복약 시간(MedicationAlertTime)에 대해 알림이 울릴 요일
 */
@Entity
@Table(name = "tbl_medication_alert_day")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MedicationAlertDay extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long medicationAlertDayId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "medication_alert_time_id", nullable = false)
	private MedicationAlertTime medicationAlertTime;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private DayOfWeek dayOfWeek;

	public static MedicationAlertDay create(MedicationAlertTime alertTime, DayOfWeek dayOfWeek) {
		MedicationAlertDay day = new MedicationAlertDay();
		day.medicationAlertTime = alertTime;
		day.dayOfWeek = dayOfWeek;
		return day;
	}
}
