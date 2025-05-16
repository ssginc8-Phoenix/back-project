package com.ssginc8.docto.file.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Category {
	USER("사용자관련"),
	HOSPITAL("병원관련"),
	MEDICAL("진료관련");

	private final String categoryName;
}
