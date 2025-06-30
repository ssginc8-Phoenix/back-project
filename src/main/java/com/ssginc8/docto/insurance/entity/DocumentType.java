// src/main/java/com/ssginc8/docto/insurance/entity/DocumentType.java
package com.ssginc8.docto.insurance.entity;

/**
 * 보험 서류 종류를 나타내는 열거형
 */
public enum DocumentType {
	MEDICAL_REPORT,   // 진료기록지
	LAB_RESULT,       // 검사 결과지
	PRESCRIPTION,     // 처방전
	CLAIM_FORM,       // 보험 청구서
	OTHER             // 기타
}
