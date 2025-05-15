package com.ssginc8.docto.user.entity;

public enum Role {

	PATIENT("환자"),
	GUARDIAN("보호자"),
	HOSPITAL_ADMIN("병원 관리자"),
	DOCTOR("의사"),
	SYSTEM_ADMIN("시스템 관리자")
	;

	private final String label;

	Role(String label) {
		this.label = label;
	}
}
