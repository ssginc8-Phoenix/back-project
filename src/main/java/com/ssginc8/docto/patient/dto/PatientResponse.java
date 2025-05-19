package com.ssginc8.docto.patient.dto;

import com.ssginc8.docto.patient.entity.Patient;

import lombok.Builder;
import lombok.Getter;

/**
 * 환자 응답 DTO
 * - 클라이언트에게 반환되는 환자 정보
 */
@Getter
public class PatientResponse {
	private final Long patientId;
	private final Long userId;
	private final String residentRegistrationNumber;

	private PatientResponse(Long patientId, Long userId, String rrn) {
		this.patientId = patientId;
		this.userId = userId;
		this.residentRegistrationNumber = rrn;
	}

	/**
	 * Patient 엔티티를 DTO로 변환
	 */
	public static PatientResponse from(Patient patient) {
		return new PatientResponse(
			patient.getPatientId(),
			patient.getUser().getUserId(),
			patient.getResidentRegistrationNumber()
		);
	}
}