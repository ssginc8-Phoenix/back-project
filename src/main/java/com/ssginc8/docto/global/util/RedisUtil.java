package com.ssginc8.docto.global.util;

import java.time.Duration;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RedisUtil {
	private final StringRedisTemplate template;

	public String getData(String key) {
		ValueOperations<String, String> valueOperations = template.opsForValue();
		return valueOperations.get(key);
	}

	public boolean existData(String key) {
		return Boolean.TRUE.equals(template.hasKey(key));
	}

	public void setDataExpire(String key, String value, Duration duration) {
		ValueOperations<String, String> valueOperations = template.opsForValue();
		valueOperations.set(key, value, duration);
	}

	public void deleteData(String key) {
		template.delete(key);
	}

	public void createRedisData(String key, String data, Duration duration) {
		if (existData(key)) {
			deleteData(key);
		}

		setDataExpire(key, data, duration);
	}
}