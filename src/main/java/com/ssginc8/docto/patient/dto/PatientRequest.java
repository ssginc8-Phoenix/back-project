package com.ssginc8.docto.patient.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PatientRequest {
	private Long userId;
	private String residentRegistrationNumber;
}