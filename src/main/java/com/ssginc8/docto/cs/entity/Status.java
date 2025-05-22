package com.ssginc8.docto.cs.entity;

import java.util.Arrays;

public enum Status {

	WAITING, OPEN, CLOSED;

	public static Status from(String input) {
		return Arrays.stream(values())
			.filter(e -> e.name().equalsIgnoreCase(input))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("유효하지 않은 상태입니다.: " + input));
	}
}
