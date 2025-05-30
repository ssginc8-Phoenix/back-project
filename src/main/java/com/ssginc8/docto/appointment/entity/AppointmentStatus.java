package com.ssginc8.docto.appointment.entity;

import java.util.Arrays;

import com.ssginc8.docto.global.error.exception.appointmentException.InvalidStatusValueException;

public enum AppointmentStatus {

	// 예약요청, 예약완료, 진료완료, 취소
	REQUESTED, CONFIRMED, COMPLETED, CANCELED;

	public static AppointmentStatus from(String input) {
		return Arrays.stream(values())
			.filter(e -> e.name().equalsIgnoreCase(input))
			.findFirst()
			.orElseThrow(InvalidStatusValueException::new);
	}
}
