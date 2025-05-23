package com.ssginc8.docto.medication.dto;

import java.time.LocalDateTime;

import com.ssginc8.docto.medication.entity.MedicationLog;
import com.ssginc8.docto.medication.entity.MedicationStatus;

import lombok.Getter;

/**
 * 복약 기록 응답 DTO
 *
 * - 복약 로그 ID
 * - 약 ID
 * - 복약 상태 (TAKEN, MISSED)
 * - 실제 복용 시간
 *
 * 복약 기록 조회 시 클라이언트에게 반환하는 데이터 구조
 */
@Getter
public class MedicationLogResponse {

	private final Long medicationLogId;      // 복약 로그 ID
	private final Long medicationId;         // 약 ID
	private final MedicationStatus status;   // 복약 상태
	private final LocalDateTime timeToTake;  // 복용 시간

	public MedicationLogResponse(Long medicationLogId, Long medicationId, MedicationStatus status, LocalDateTime timeToTake) {
		this.medicationLogId = medicationLogId;
		this.medicationId = medicationId;
		this.status = status;
		this.timeToTake = timeToTake;
	}

	/**
	 * MedicationLog 엔티티 → DTO로 변환
	 */
	public static MedicationLogResponse from(MedicationLog log) {
		return new MedicationLogResponse(
			log.getMedicationLogId(),
			log.getMedication().getMedicationId(),
			log.getStatus(),
			log.getTimeToTake()
		);
	}
}