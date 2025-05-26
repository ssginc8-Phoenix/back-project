package com.ssginc8.docto.medication.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 복약 상태 Enum
 * - 복용 완료: TAKEN
 * - 복용 실패: MISSED
 *
 * @JsonCreator JSON → Enum 변환 시 사용 (대소문자 무시, "taken"도 OK)
 * @JsonValue Enum → JSON 직렬화 시 사용 (TAKEN → "TAKEN"으로 출력됨)
 */
public enum MedicationStatus {
	TAKEN,
	MISSED;
	@JsonCreator
	public static MedicationStatus from(String value) {
		return MedicationStatus.valueOf(value.toUpperCase()); // 대소문자 무시하고 매핑
	}

	@JsonValue
	public String toValue() {
		return this.name();
	}
	}