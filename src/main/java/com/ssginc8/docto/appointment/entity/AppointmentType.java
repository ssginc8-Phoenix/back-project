package com.ssginc8.docto.appointment.entity;

import java.util.Arrays;

import com.ssginc8.docto.global.error.exception.appointmentException.InvalidTypeValueException;

public enum AppointmentType {

	SCHEDULED, IMMEDIATE;

	public static AppointmentType from(String input) {
		return Arrays.stream(values())
			.filter(e -> e.name().equalsIgnoreCase(input))
			.findFirst()
			.orElseThrow(InvalidTypeValueException::new);
	}
}
