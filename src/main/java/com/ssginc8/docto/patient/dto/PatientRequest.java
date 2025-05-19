package com.ssginc8.docto.patient.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 환자 등록 요청 DTO
 */
@Getter
public class PatientRequest {
	private Long userId;
	private String residentRegistrationNumber;
}