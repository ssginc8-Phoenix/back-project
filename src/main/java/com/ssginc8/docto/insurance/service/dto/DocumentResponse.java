// src/main/java/com/ssginc8/docto/insurance/service/dto/DocumentResponse.java
package com.ssginc8.docto.insurance.service.dto;

import com.ssginc8.docto.insurance.entity.DocumentStatus;
import com.ssginc8.docto.insurance.entity.DocumentType;

import lombok.Builder;
import lombok.Getter;

/**
 * 요청 생성 & 상태 조회 & 목록 조회 시 반환되는 DTO
 */
@Getter
@Builder
public class DocumentResponse {
	/** 요청 고유 ID */
	private final Long documentId;
	/** 현재 상태(REQUESTED, APPROVED, REJECTED) */
	private final DocumentStatus status;
	/** 반려 사유 (REJECTED 시) */
	private final String rejectionReason;
	/** (첨부 후) 다운로드용 URL */
	private final String downloadUrl;

	private Long   hospitalId;
	private String hospitalName;
	/** 문서 종류  */
	private final DocumentType type;
}
