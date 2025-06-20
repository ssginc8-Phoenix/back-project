package com.ssginc8.docto.global.event.guardian;

import com.ssginc8.docto.guardian.entity.PatientGuardian;
import com.ssginc8.docto.user.entity.User;

import lombok.Getter;

@Getter
public class GuardianInviteEvent {

	private final User guardian;
	private final String patientName;
	private final Long patientGuardianId;

	public GuardianInviteEvent(User guardian, String patientName, Long patientGuardianId) {
		this.guardian = guardian;
		this.patientName = patientName;
		this.patientGuardianId = patientGuardianId;
	}
}
