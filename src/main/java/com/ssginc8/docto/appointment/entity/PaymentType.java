package com.ssginc8.docto.appointment.entity;

import java.util.Arrays;

public enum PaymentType {

	ONSITE, ONLINE;

	public static PaymentType from(String input) {
		return Arrays.stream(values())
			.filter(e -> e.name().equalsIgnoreCase(input))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("유효하지 않은 결제 방법입니다.: " + input));
	}
}
