package com.ssginc8.docto.medication.entity;

import java.time.LocalDateTime;

import com.ssginc8.docto.global.base.BaseTimeEntity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 복약 기록 Entity
 *
 * - 알림 시간과 약 정보에 대해 실제 복용 여부를 기록
 * - BaseTimeEntity 상속으로 createdAt, updatedAt, deletedAt 자동 관리
 */
@Entity
@Table(name = "tbl_medication_log")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MedicationLog extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long medicationLogId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "medication_alert_time_id", nullable = false)
	private MedicationAlertTime medicationAlertTime;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "medication_id", nullable = false)
	private MedicationInformation medication;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private MedicationStatus status;

	@Column(nullable = false)
	private LocalDateTime timeToTake;

	public static MedicationLog create(MedicationAlertTime alertTime, MedicationInformation medication, MedicationStatus status, LocalDateTime timeToTake) {
		MedicationLog log = new MedicationLog();
		log.medicationAlertTime = alertTime;   // ❗ 알림 시간 세팅
		log.medication = medication;
		log.status = status;
		log.timeToTake = timeToTake;
		return log;
	}
}
