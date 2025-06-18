package com.ssginc8.docto.global.event.guardian;

import com.ssginc8.docto.guardian.entity.PatientGuardian;

import lombok.Getter;

@Getter
public class GuardianInviteEvent {

	private final PatientGuardian patientGuardian;

	public GuardianInviteEvent(PatientGuardian patientGuardian) {
		this.patientGuardian = patientGuardian;
	}
}
