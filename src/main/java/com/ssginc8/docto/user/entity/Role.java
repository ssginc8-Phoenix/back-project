package com.ssginc8.docto.user.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
  
	PATIENT("ROLE_PATIENT", "환자"),
	GUARDIAN("ROLE_GUARDIAN", "보호자"),
	HOSPITAL_ADMIN("ROLE_HOSPITAL_ADMIN", "병원 관리자"),
	DOCTOR("ROLE_DOCTOR", "의사"),
	SYSTEM_ADMIN("ROLE_SYSTEM_ADMIN", "시스템 관리자");

	private final String key;
	private final String title;
}
