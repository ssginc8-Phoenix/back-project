package com.ssginc8.docto.qna.entity;

import com.ssginc8.docto.global.error.exception.appointmentException.InvalidStatusValueException;
import java.util.Arrays;


public enum QaStatus {
	PENDING,
	COMPLETED;

	public static QaStatus from(String input) {
		return Arrays.stream(values())
			.filter(e -> e.name().equalsIgnoreCase(input))
			.findFirst()
			.orElseThrow(InvalidStatusValueException::new);
	}
}
