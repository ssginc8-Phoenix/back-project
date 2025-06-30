package com.ssginc8.docto.cs.entity;

import java.util.Arrays;

public enum MessageType {

	TEXT, IMAGE;

	public static MessageType from(String input) {
		return Arrays.stream(values())
			.filter(e -> e.name().equalsIgnoreCase(input))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("유효하지 않은 메세지 타입입니다.: " + input));
	}
}
