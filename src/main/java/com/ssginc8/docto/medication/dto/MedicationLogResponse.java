package com.ssginc8.docto.medication.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssginc8.docto.medication.entity.MedicationLog;
import com.ssginc8.docto.medication.entity.MedicationStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MedicationLogResponse {

	private final Long medicationLogId;
	private final Long medicationId;

	/** 약 이름 추가 */
	private final String medicationName;

	private final MedicationStatus status;

	/**
	 * 날짜 포맷을 ISO 8601 형식(예: 2025-06-21T14:30:00)으로 내려줍니다.
	 * 클라이언트에서 new Date(...) 시 Invalid Date 방지
	 */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
	private final LocalDateTime loggedAt;

	public MedicationLogResponse(Long medicationLogId, Long medicationId, String medicationName, MedicationStatus status, LocalDateTime loggedAt) {
		this.medicationLogId = medicationLogId;
		this.medicationId = medicationId;
		this.medicationName = medicationName;
		this.status = status;
		this.loggedAt = loggedAt;
	}

	public static MedicationLogResponse from(MedicationLog log) {
		return new MedicationLogResponse(
			log.getMedicationLogId(),
			log.getMedication().getMedicationId(),
			log.getMedication().getMedicationName(),
			log.getStatus(),
			log.getLoggedAt()
		);
	}
}
