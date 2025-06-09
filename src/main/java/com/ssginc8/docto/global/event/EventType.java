package com.ssginc8.docto.global.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EventType {
	EMAIL_VERIFICATION("이메일 인증"),
	GUARDIAN_INVITE("보호자 초대 안내");

	private final String description;
}