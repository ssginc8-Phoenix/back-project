package com.ssginc8.docto.global.event;

import lombok.Getter;

@Getter
public class EmailSendEvent {

	private final String email;
	private final String subject;
	private final String message;
	private final EventType eventType;

	private EmailSendEvent(String email, String subject, String message, EventType eventType) {
		this.email = email;
		this.subject = subject;
		this.message = message;
		this.eventType = eventType;
	}

	public static EmailSendEvent emailVerification(String email, String code) {
		return new EmailSendEvent(
			email,
			"docto 인증 이메일입니다.",
			code.toString(),
			EventType.EMAIL_VERIFICATION
		);
	}

	public static EmailSendEvent guardianInvite(String email, String inviteCode) {
		return new EmailSendEvent(
			email,
			"닥투 보호자 초대 안내 메일입니다.",
			inviteCode, // 🔥 초대코드가 본문
			EventType.GUARDIAN_INVITE
		);
	}
}