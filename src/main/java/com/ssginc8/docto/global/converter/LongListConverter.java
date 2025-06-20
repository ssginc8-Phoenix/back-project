package com.ssginc8.docto.global.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.*;
import java.util.stream.*;

@Converter
public class LongListConverter implements AttributeConverter<List<Long>, String> {

	// 엔티티 → DB
	@Override
	public String convertToDatabaseColumn(List<Long> attribute) {
		if (attribute == null || attribute.isEmpty()) {
			return "";
		}
		return attribute.stream()
			.map(String::valueOf)
			.collect(Collectors.joining(","));
	}

	// DB → 엔티티
	@Override
	public List<Long> convertToEntityAttribute(String dbData) {
		if (dbData == null || dbData.isBlank()) {
			return new ArrayList<>();
		}
		return Arrays.stream(dbData.split(","))
			.map(String::trim)
			.map(Long::valueOf)
			.collect(Collectors.toList());
	}
}
