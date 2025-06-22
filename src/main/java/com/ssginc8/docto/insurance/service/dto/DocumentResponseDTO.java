// src/main/java/com/ssginc8/docto/insurance/service/dto/DocumentResponseDTO.java
package com.ssginc8.docto.insurance.service.dto;

import com.ssginc8.docto.insurance.entity.DocumentStatus;
import lombok.Builder;
import lombok.Getter;

/**
 * 서류 상태 조회 및 목록 조회 시 반환되는 DTO
 * - builder 사용은 DTO에 한정, 엔티티에는 사용 금지
 */
@Getter
@Builder
public class DocumentResponseDTO {
	private Long documentId;
	private DocumentStatus status;
	private String rejectionReason;
	private String downloadUrl;
}
