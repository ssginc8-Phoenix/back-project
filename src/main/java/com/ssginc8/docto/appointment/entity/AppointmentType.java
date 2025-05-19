package com.ssginc8.docto.appointment.entity;

import java.util.Arrays;

public enum AppointmentType {

	SCHEDULED, WALKIN, TELEMEDICINE;

	public static AppointmentType from(String input) {
		return Arrays.stream(values())
			.filter(e -> e.name().equalsIgnoreCase(input))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("유효하지 않은 예약 타입입니다.: " + input));
	}
}
