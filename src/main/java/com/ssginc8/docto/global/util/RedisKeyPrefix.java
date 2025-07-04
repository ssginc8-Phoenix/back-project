package com.ssginc8.docto.global.util;

import java.time.Duration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RedisKeyPrefix {
	EMAIL_AUTH("email:auth:", Duration.ofMinutes(5));

	private final String prefix;
	private final Duration ttl;

	public String key(String identifier) {
		return prefix + identifier;
	}
}
