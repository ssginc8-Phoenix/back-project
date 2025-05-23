package com.ssginc8.docto.patient.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 환자 등록 요청 DTO
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PatientRequest {
	private Long userId;
	private String residentRegistrationNumber;
}