package com.ssginc8.docto.file.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Category {
	USER("user", "사용자관련"),
	HOSPITAL("hospital", "병원관련"),
	MEDICAL("medical", "진료관련"),
	INSURANCE("insurance", "보험관련");

	private final String categoryName;
	private final String categoryDescription;
}
