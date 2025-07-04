package com.ssginc8.docto.guardian.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 보호자 응답 DTO
 */
@Getter
@AllArgsConstructor
public class GuardianResponse {
	private Long patientGuardianId;
	private String name;

	public static GuardianResponse from(Long patientGuardianId, String name) {
		return new GuardianResponse(patientGuardianId, name);
	}
}
