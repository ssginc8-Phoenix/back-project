package com.ssginc8.docto.appointment.entity;

import java.util.Arrays;

import com.ssginc8.docto.global.error.exception.appointmentException.InvalidPaymentValueException;

public enum PaymentType {

	ONSITE, ONLINE;

	public static PaymentType from(String input) {
		return Arrays.stream(values())
			.filter(e -> e.name().equalsIgnoreCase(input))
			.findFirst()
			.orElseThrow(InvalidPaymentValueException::new);
	}
}
