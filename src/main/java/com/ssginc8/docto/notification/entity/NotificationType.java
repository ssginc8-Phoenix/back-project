package com.ssginc8.docto.notification.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotificationType {

	APPOINTMENT_CONFIRMED("예약 확정"),
	APPOINTMENT_CANCELED("예약 취소"),
	APPOINTMENT_NOSHOW("예약 노쇼"),
	QNA_RESPONSE("QNA 응답"),
	MEDICATION_ALERT("복용 알림"),
	MEDICATION_MISSED("미복용 알림"),
	GUARDIAN_INVITE("보호자 초대"),
  PAYMENT_REQUEST("결제 요청")
  ;

	private final String description;
}
