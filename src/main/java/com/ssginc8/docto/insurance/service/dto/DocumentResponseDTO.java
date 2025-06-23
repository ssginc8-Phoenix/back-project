// src/main/java/com/ssginc8/docto/insurance/service/dto/DocumentResponseDTO.java
package com.ssginc8.docto.insurance.service.dto;

import com.ssginc8.docto.insurance.entity.DocumentStatus;
import lombok.Builder;
import lombok.Getter;

/**
 * 단건 상태 조회 또는 목록 조회 시 반환되는 DTO
 *
 * • documentId     – 요청 식별자
 * • status         – 현재 상태
 * • rejectionReason – 반려 사유(선택)
 * • downloadUrl    – 파일 URL(첨부 후)
 */
@Getter
@Builder
public class DocumentResponseDTO {
	private Long documentId;
	private DocumentStatus status;
	private String rejectionReason;
	private String downloadUrl;
}
