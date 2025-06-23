// src/main/java/com/ssginc8/docto/insurance/entity/DocumentStatus.java
package com.ssginc8.docto.insurance.entity;

/**
 * 보험 서류 요청 상태
 *
 * REQUESTED – 환자·보호자가 요청 레코드만 생성한 상태
 * APPROVED  – 관리자가 승인 처리한 상태
 * REJECTED  – 관리자가 반려 처리한 상태 (rejectionReason 필수)
 */
public enum DocumentStatus {
	REQUESTED,
	APPROVED,
	REJECTED
}
