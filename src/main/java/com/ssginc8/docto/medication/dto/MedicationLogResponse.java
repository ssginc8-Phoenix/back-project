package com.ssginc8.docto.medication.dto;

import com.ssginc8.docto.medication.entity.MedicationLog;
import com.ssginc8.docto.medication.entity.MedicationStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MedicationLogResponse {

	private final Long medicationLogId;
	private final Long medicationId;
	private final MedicationStatus status;
	private final LocalDateTime loggedAt;

	public MedicationLogResponse(Long medicationLogId, Long medicationId, MedicationStatus status, LocalDateTime loggedAt) {
		this.medicationLogId = medicationLogId;
		this.medicationId = medicationId;
		this.status = status;
		this.loggedAt = loggedAt;
	}

	public static MedicationLogResponse from(MedicationLog log) {
		return new MedicationLogResponse(
			log.getMedicationLogId(),
			log.getMedication().getMedicationId(),
			log.getStatus(),
			log.getLoggedAt()
		);
	}
}
