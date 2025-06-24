package com.ssginc8.docto.guardian.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PendingInviteResponse {
	private Long mappingId;    // PatientGuardian.patientGuardianId
	private String name;      // User.name
	private String email;
	private String inviteCode; // PatientGuardian.inviteCode
}
