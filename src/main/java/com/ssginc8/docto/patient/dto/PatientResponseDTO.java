package com.ssginc8.docto.patient.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PatientResponseDTO {
	private final Long patientId;
	private final Long userId;
	private final String residentRegistrationNumber;

	@Builder
	public PatientResponseDTO(Long patientId, Long userId, String residentRegistrationNumber) {
		this.patientId = patientId;
		this.userId = userId;
		this.residentRegistrationNumber = residentRegistrationNumber;
	}
}