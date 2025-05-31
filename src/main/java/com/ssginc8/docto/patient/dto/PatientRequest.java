package com.ssginc8.docto.patient.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 환자 등록 요청 DTO
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PatientRequest {
	private Long userId; // user 생성할 때 이메일로 식별
	private String residentRegistrationNumber;
}
