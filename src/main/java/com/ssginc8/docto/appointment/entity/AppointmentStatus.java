package com.ssginc8.docto.appointment.entity;

import java.util.Arrays;

public enum AppointmentStatus {

	// 예약완료, 대기중, 진료완료, 취소
	Requested, Confirmed, Waiting, Completed, Cancelled;

	public static AppointmentStatus from(String input) {
		return Arrays.stream(values())
			.filter(e -> e.name().equalsIgnoreCase(input))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("유효하지 않은 예약 상태입니다.: " + input));
	}
}
