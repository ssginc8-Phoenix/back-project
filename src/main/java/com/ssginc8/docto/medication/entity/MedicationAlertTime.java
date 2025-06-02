package com.ssginc8.docto.medication.entity;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.ssginc8.docto.global.base.BaseTimeEntity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 복약 시간 Entity
 *
 * - 복약 정보(MedicationInformation)에 연관된 복약 시간
 * - 복약 요일(MedicationAlertDay) 및 복약 기록(MedicationLog)과 연결됨
 */
@Entity
@Table(name = "tbl_medication_alert_time")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MedicationAlertTime extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long medicationAlertTimeId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "medication_id", nullable = false)
	private MedicationInformation medication;

	@Column(nullable = false)
	private LocalTime timeToTake;

	@OneToMany(mappedBy = "medicationAlertTime", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<MedicationAlertDay> alertDays = new ArrayList<>();

	public static MedicationAlertTime create(MedicationInformation medication, LocalTime timeToTake) {
		MedicationAlertTime time = new MedicationAlertTime();
		time.medication = medication;
		time.timeToTake = timeToTake;
		return time;
	}

	public void updateTimeToTake(LocalTime newTime) {
		this.timeToTake = newTime;
	}
}
