package com.ssginc8.docto.medication.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 복약 기록 Entity
 *
 * - 알림 시간과 약 정보에 대해 실제 복용 여부를 기록
 */
@Entity
@Table(name = "tbl_medication_log")
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MedicationLog {

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

	@Column
	private LocalDateTime timeToTake;

	@CreatedDate
	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@LastModifiedDate
	@Column(nullable = false)
	private LocalDateTime updatedAt;

	public static MedicationLog create(MedicationAlertTime alertTime,
		MedicationInformation medication,
		MedicationStatus status,
		LocalDateTime timeToTake) {
		MedicationLog log = new MedicationLog();
		log.medicationAlertTime = alertTime;
		log.medication = medication;
		log.status = status;
		log.timeToTake = timeToTake;
		return log;
	}
}
