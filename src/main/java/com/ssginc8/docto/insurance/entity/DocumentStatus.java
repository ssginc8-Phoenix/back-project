// src/main/java/com/ssginc8/docto/insurance/entity/DocumentStatus.java
package com.ssginc8.docto.insurance.entity;

/**
 * 보험 서류 요청 상태 열거형
 * - REQUESTED: 서류 요청됨
 * - APPROVED: 서류 승인됨
 * - REJECTED: 서류 반려됨
 */
public enum DocumentStatus {
	REQUESTED,
	APPROVED,
	REJECTED
}
