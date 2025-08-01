package com.ssginc8.docto.guardian.dto;

import lombok.Getter;

/**
 * 보호자가 가진 환자 정보를 응답할 때 사용하는 DTO
 */
@Getter
public class PatientSummaryResponse {
	private final Long patientId;
	private final String name;
	private final String residentRegistrationNumber;
	private String address;
	private String profileImageUrl;

	private PatientSummaryResponse(Long patientId, String name, String rrn, String address) {
		this.patientId = patientId;
		this.name = name;
		this.residentRegistrationNumber = rrn;
		this.address = address;

	}

	public static PatientSummaryResponse of(Long patientId, String name, String rrn, String address) {
		return new PatientSummaryResponse(patientId, name, rrn, address);
	}

	public void setProfileImageUrl(String profileImageUrl) {
		this.profileImageUrl = profileImageUrl;
	}
}