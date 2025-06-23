// src/main/java/com/ssginc8/docto/insurance/service/dto/DocumentResponseDTO.java
package com.ssginc8.docto.insurance.service.dto;

import com.ssginc8.docto.insurance.entity.DocumentStatus;
import lombok.Builder;
import lombok.Getter;

/**
 * 상태 조회 및 목록 조회 시 반환되는 DTO
 * • Builder 패턴 사용 (DTO에 한정)
 */
@Getter
@Builder
public class DocumentResponseDTO {

	/** 서류 요청 ID */
	private Long documentId;

	/** 현재 상태 */
	private DocumentStatus status;

	/** 반려 사유 (REJECTED 상태일 때만 값 세팅) */
	private String rejectionReason;

	/** 다운로드 URL (추후에 세팅 가능) */
	private String downloadUrl;
}
